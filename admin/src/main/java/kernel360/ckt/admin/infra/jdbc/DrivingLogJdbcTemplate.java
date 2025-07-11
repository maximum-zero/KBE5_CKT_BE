package kernel360.ckt.admin.infra.jdbc;

import kernel360.ckt.admin.application.service.command.DrivingLogListCommand;
import kernel360.ckt.admin.application.service.dto.DrivingLogListDto;
import kernel360.ckt.admin.application.service.dto.RouteSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class DrivingLogJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;

    public DrivingLogJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 필터링 조건과 페이징을 적용하여 DrivingLog 정보를 DTO 형태로 조회합니다.
     */
    public Page<DrivingLogListDto> findAll(DrivingLogListCommand command, Pageable pageable) {
        // 필터링 조건에 사용될 공통 WHERE 절과 파라미터 빌드
        SqlConditions sqlConditions = buildCommonConditions(command);
        List<Object> commonParams = sqlConditions.params();
        String commonWhereClauseForCount = sqlConditions.commonWhereClause(); // 카운트 쿼리용 (기존 commonWhereClause)

        // 총 카운트 조회 쿼리
        String countSql = buildCountQuery(commonWhereClauseForCount);
        Long total = jdbcTemplate.queryForObject(countSql, Long.class, commonParams.toArray());

        // 결과가 없으면 빈 페이지 반환
        if (total == null || total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0L);
        }

        // 메인 데이터 조회 쿼리 (변경된 buildMainDataQuery 호출)
        String mainDataSql = buildMainDataQuery(sqlConditions); // SqlConditions 객체 자체를 넘김

        // 메인 쿼리에 전달할 최종 파라미터 리스트 구성
        List<Object> finalQueryParams = new ArrayList<>();
        finalQueryParams.addAll(commonParams); // commonParams는 mainDataSql에도 그대로 사용됨
        finalQueryParams.add(pageable.getPageSize());
        finalQueryParams.add(pageable.getOffset());

        log.info("mainDataSql > {}", mainDataSql);
        log.info("Pageable Page Size: {}, Offset: {}", pageable.getPageSize(), pageable.getOffset());
        log.info("Final Query Parameters (including pageSize, offset): {}", finalQueryParams);

        // 쿼리 실행 및 ResultSet 매핑
        Map<Long, DrivingLogListDto> drivingLogDtoMap = new LinkedHashMap<>();
        jdbcTemplate.query(mainDataSql, rs -> {
            Long drivingLogId = rs.getLong("dl_id");
            DrivingLogListDto drivingLogDto = drivingLogDtoMap.get(drivingLogId);

            if (drivingLogDto == null) {
                drivingLogDto = new DrivingLogListDto(
                    drivingLogId,
                    rs.getString("v_model_name"),
                    rs.getString("v_registration_number"),
                    null, null, null, null, null,
                    rs.getString("c_customer_name"),
                    new ArrayList<>()
                );
                drivingLogDtoMap.put(drivingLogId, drivingLogDto);
            }

            // RouteSummaryDto 매핑
            Long routeId = rs.getObject("rt_id", Long.class);
            if (routeId != null) {
                RouteSummaryDto routeSummaryDto = mapRouteSummaryDto(rs);
                // 중복 라우트 추가 방지 (Join 결과로 여러 라우트 행이 나올 수 있으므로)
                if (drivingLogDto.routes().stream().noneMatch(r -> Objects.equals(r.id(), routeSummaryDto.id()))) {
                    drivingLogDto.routes().add(routeSummaryDto);
                }
            }
        }, finalQueryParams.toArray());

        // 최종 DTO 리스트 생성 및 집계 값 설정
        List<DrivingLogListDto> results = drivingLogDtoMap.values().stream()
            .map(this::aggregateRouteDataToDto)
            .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, total);
    }

    /**
     * 필터링 조건에 따른 WHERE 절과 파라미터를 빌드합니다.
     * 이 메서드는 count 쿼리용 문자열 WHERE 절과, main 쿼리 inner SELECT 절 조립을 위한 구조화된 조건 목록을 반환합니다.
     */
    private SqlConditions buildCommonConditions(DrivingLogListCommand command) {
        StringBuilder commonWhereClauseBuilder = new StringBuilder("WHERE 1=1 "); // count 쿼리용 문자열 WHERE 절
        List<Object> commonParams = new ArrayList<>();
        List<Condition> conditions = new ArrayList<>(); // main 쿼리 inner SELECT 절 조립용 구조화된 조건 목록

        // 'COMPLETED' 상태 조건 (리터럴 값)
        commonWhereClauseBuilder.append("AND dl.status = 'COMPLETED' ");
        conditions.add(new Condition("dl", "status", "=", false)); // false: 파라미터 사용 안 함

        if (StringUtils.hasText(command.vehicleNumber())) {
            commonWhereClauseBuilder.append("AND v.registration_number LIKE ? ");
            commonParams.add(command.vehicleNumber() + "%");
            conditions.add(new Condition("v", "registration_number", "LIKE", true)); // true: 파라미터 사용
        }
        if (command.startDate() != null) {
            commonWhereClauseBuilder.append("AND rt.start_at >= ? ");
            commonParams.add(command.startDate());
            conditions.add(new Condition("rt", "start_at", ">=", true));
        }
        if (command.endDate() != null) {
            commonWhereClauseBuilder.append("AND rt.end_at <= ? ");
            LocalDateTime adjustedEndDate = command.endDate()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
            commonParams.add(adjustedEndDate);
            conditions.add(new Condition("rt", "end_at", "<=", true));
        }
        return new SqlConditions(conditions, commonParams, commonWhereClauseBuilder.toString());
    }

    /**
     * 총 카운트를 조회하는 SQL 쿼리를 빌드합니다.
     */
    private String buildCountQuery(String commonWhereClause) {
        return "SELECT COUNT(DISTINCT dl.id) " +
            "FROM driving_log dl " +
            "JOIN vehicle v ON dl.vehicle_id = v.id " +
            "LEFT JOIN rental r ON dl.rental_id = r.id " +
            "LEFT JOIN customer c ON r.customer_id = c.id " +
            "LEFT JOIN route rt ON dl.id = rt.driving_log_id " +
            commonWhereClause; // 여기는 기존 commonWhereClause를 그대로 사용
    }

    /**
     * 메인 데이터 조회 SQL 쿼리를 빌드합니다.
     * 이 메서드는 SqlConditions 객체의 'conditions' 목록을 사용하여 inner 쿼리의 WHERE 절을 직접 조립합니다.
     */
    private String buildMainDataQuery(SqlConditions sqlConditions) {
        StringBuilder innerWhereClauseBuilder = new StringBuilder("WHERE 1=1 "); // inner 쿼리용 WHERE 절 조립 시작

        // SqlConditions의 구조화된 조건 목록을 순회하며 WHERE 절을 조립
        for (Condition cond : sqlConditions.conditions()) {
            // inner 쿼리에 맞는 별칭 생성
            String innerAlias = cond.alias() + "_inner";
            innerWhereClauseBuilder.append("AND ")
                .append(innerAlias).append(".")
                .append(cond.column())
                .append(" ").append(cond.operator()).append(" ");

            // 조건이 파라미터를 사용하는지 여부에 따라 ? 또는 리터럴 값 추가
            if (cond.isParam()) {
                innerWhereClauseBuilder.append("? ");
            } else {
                // 현재는 'dl.status = 'COMPLETED'' 조건만 리터럴이므로 직접 처리
                if ("status".equals(cond.column()) && "dl".equals(cond.alias())) {
                    innerWhereClauseBuilder.append("'COMPLETED' ");
                }
                // 향후 다른 리터럴 조건이 추가될 경우 여기에 로직을 확장해야 합니다.
            }
        }
        String innerWhereClause = innerWhereClauseBuilder.toString();

        // 로그를 통해 새로 조립된 innerWhereClause 확인
        log.info("Original commonWhereClause (used for count query): {}", sqlConditions.commonWhereClause());
        log.info("Transformed innerWhereClause (built directly for main query): {}", innerWhereClause);

        return "SELECT " +
            "dl.id AS dl_id, " +
            "v.id AS v_id, v.model_name AS v_model_name, v.registration_number AS v_registration_number, " +
            "r.id AS r_id, r.pickup_at AS r_pickup_at, r.return_at AS r_return_at, " +
            "c.id AS c_id, c.customer_name AS c_customer_name, " +
            "rt.id AS rt_id, rt.status AS rt_status, rt.start_lat AS rt_start_lat, rt.start_lon AS rt_start_lon, " +
            "rt.end_lat AS rt_end_lat, rt.end_lon AS rt_end_lon, " +
            "rt.start_odometer AS rt_start_odometer, rt.end_odometer AS rt_end_odometer, " +
            "rt.total_distance AS rt_total_distance, rt.start_at AS rt_start_at, rt.end_at AS rt_end_at " +
            "FROM driving_log dl " +
            "JOIN (" +
            "SELECT DISTINCT dl_inner.id, dl_inner.created_at " +
            "FROM driving_log dl_inner " +
            "JOIN vehicle v_inner ON dl_inner.vehicle_id = v_inner.id " +
            "LEFT JOIN rental r_inner ON dl_inner.rental_id = r_inner.id " +
            "LEFT JOIN customer c_inner ON r_inner.customer_id = c_inner.id " +
            "LEFT JOIN route rt_inner ON dl_inner.id = rt_inner.driving_log_id " +
            innerWhereClause + // 새로 조립된 innerWhereClause 사용
            " ORDER BY dl_inner.created_at DESC " +
            " LIMIT ? OFFSET ? " +
            ") AS paged_dl ON dl.id = paged_dl.id " +
            "JOIN vehicle v ON dl.vehicle_id = v.id " +
            "LEFT JOIN rental r ON dl.rental_id = r.id " +
            "LEFT JOIN customer c ON r.customer_id = c.id " +
            "LEFT JOIN route rt ON dl.id = rt.driving_log_id " +
            "ORDER BY dl.created_at DESC, rt.end_at DESC";
    }

    /**
     * ResultSet에서 RouteSummaryDto 를 매핑합니다.
     */
    private RouteSummaryDto mapRouteSummaryDto(ResultSet rs) throws SQLException {
        return new RouteSummaryDto(
            rs.getObject("rt_id", Long.class),
            rs.getObject("rt_start_at") != null ? rs.getTimestamp("rt_start_at").toLocalDateTime() : null,
            rs.getObject("rt_end_at") != null ? rs.getTimestamp("rt_end_at").toLocalDateTime() : null,
            rs.getObject("rt_start_odometer", Long.class),
            rs.getObject("rt_end_odometer", Long.class),
            rs.getObject("rt_total_distance", Long.class)
        );
    }

    /**
     * routes 데이터를 집계하여 전체 운행 요약 정보를 설정합니다.
     */
    private DrivingLogListDto aggregateRouteDataToDto(DrivingLogListDto dto) {
        LocalDateTime firstRouteStartAt = null;
        LocalDateTime lastRouteEndAt = null;
        Long overallStartOdometer = null;
        Long overallEndOdometer = null;
        Long overallTotalDistance = 0L;

        if (!dto.routes().isEmpty()) {
            // 시작 시간 기준으로 정렬 (null은 마지막으로)
            dto.routes().sort(Comparator.comparing(RouteSummaryDto::startAt, Comparator.nullsLast(Comparator.naturalOrder())));

            firstRouteStartAt = dto.routes().stream()
                .map(RouteSummaryDto::startAt)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

            lastRouteEndAt = dto.routes().stream()
                .map(RouteSummaryDto::endAt)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

            overallStartOdometer = dto.routes().stream()
                .map(RouteSummaryDto::startOdometer)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

            overallEndOdometer = dto.routes().stream()
                .map(RouteSummaryDto::endOdometer)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

            overallTotalDistance = dto.routes().stream()
                .map(RouteSummaryDto::totalDistance)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
        }

        return new DrivingLogListDto(
            dto.id(),
            dto.vehicleModelName(),
            dto.vehicleRegistrationNumber(),
            firstRouteStartAt,
            lastRouteEndAt,
            overallStartOdometer,
            overallEndOdometer,
            overallTotalDistance,
            dto.customerName(),
            dto.routes()
        );
    }

    /**
     * SQL WHERE 절의 개별 조건 구성을 위한 내부 레코드 클래스.
     * 각 조건의 테이블 별칭, 컬럼, 연산자, 파라미터 사용 여부를 정의합니다.
     */
    private record Condition(
        String alias,
        String column,
        String operator,
        boolean isParam // 이 조건이 물음표(?) 파라미터를 사용하는지 여부 (예: LIKE ?, >= ? 등)
    ) {}

    /**
     * SQL WHERE 절과 해당 파라미터를 담는 내부 레코드 클래스.
     * SQL 쿼리 조건과 파라미터 묶음을 반환하는 데 사용됩니다.
     */
    private record SqlConditions(
        List<Condition> conditions,
        List<Object> params,
        String commonWhereClause
    ) {}
}
