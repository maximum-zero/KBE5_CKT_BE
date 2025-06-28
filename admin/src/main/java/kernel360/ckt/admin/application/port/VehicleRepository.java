package kernel360.ckt.admin.application.port;

import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 차량 도메인의 저장소 역할을 담당하는 포트 인터페이스입니다.
 * 다양한 조회 조건 및 식별자를 기준으로 차량 데이터를 다룹니다.
 */
public interface VehicleRepository {

    /**
     * 차량 정보를 저장하거나 수정합니다.
     * @param vehicleEntity 저장할 차량 엔티티
     * @return 저장된 차량 엔티티
     */
    VehicleEntity save(VehicleEntity vehicleEntity);

    /**
     * 차량 차량 목록을 페이징하여 조회합니다.
     * @param companyId 조회할 회사
     * @param status 차량 상태
     * @param keyword 검색 키워드 (차량 번호, 모델명 등)
     * @param pageable 페이징 정보
     * @return 조건에 부합하는 차량 목록 (Page)
     */
    Page<VehicleEntity> findAll(Long companyId, VehicleStatus status, String keyword, Pageable pageable);

    /**
     * 차량 ID로 차량을 조회합니다.
     * @param vehicleId 차량 ID
     * @return 존재할 경우 차량 엔티티, 존재하지 않을 경우 빈 Optional
     */
    Optional<VehicleEntity> findById(Long vehicleId, Long companyId);

    /**
     * 차량 등록 번호로 차량을 조회합니다.
     * @param registrationNumber 차량 등록 번호
     * @return 존재할 경우 차량 엔티티, 존재하지 않을 경우 빈 Optional
     */
    Optional<VehicleEntity> findByRegistrationNumber(Long companyId, String registrationNumber);

    /**
     * 전체 차량 수를 반환합니다.
     * @return 차량 개수
     */
    long count();

    /**
     * 예약가능한 차량을 조회합니다.
     * @param companyId 조회할 회사
     * @param keyword 차량 번호 or 모델명
     * @param pickupAt 픽업 시간
     * @param returnAt 반납 시간
     */
    List<VehicleEntity> searchAvailableVehiclesByKeyword(Long companyId, String keyword, LocalDateTime pickupAt, LocalDateTime returnAt);
}
