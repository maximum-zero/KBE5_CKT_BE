package kernel360.ckt.admin.application.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kernel360.ckt.admin.application.service.command.CreateRentalCommand;
import kernel360.ckt.admin.application.service.command.RentalRetrieveCommand;
import kernel360.ckt.admin.application.service.command.RentalListCommand;
import kernel360.ckt.admin.application.port.DrivingLogRepository;
import kernel360.ckt.admin.application.service.command.RentalUpdateStatusCommand;
import kernel360.ckt.core.common.error.*;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.admin.application.port.CompanyRepository;
import kernel360.ckt.admin.application.port.CustomerRepository;
import kernel360.ckt.admin.application.port.RentalRepository;
import kernel360.ckt.admin.application.port.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CompanyRepository companyRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final DrivingLogRepository drivingLogRepository;

    /**
     * 새로운 예약을 생성합니다.
     *
     * @param command 렌탈 생성에 필요한 데이터를 담고 있는 {@link CreateRentalCommand} 객체
     *
     * @return 생성된 {@link RentalEntity} 객체
     * @throws CustomException 회사, 차량, 또는 고객 정보를 찾을 수 없거나, 차량이 해당 시간에 이용 불가능한 경우 발생
     */
    @Transactional
    public RentalEntity createRental(CreateRentalCommand command) {
        log.info("예약 생성 요청 - {}", command);

        final CompanyEntity company = findCompanyById(command.companyId());
        log.info("회사 정보 조회 - {}", company);

        final VehicleEntity vehicle = findVehicleById(command.vehicleId());
        log.info("차량 정보 조회 - {}", vehicle);

        final CustomerEntity customer = findCustomerById(command.customerId());
        log.info("고객 정보 조회 - {}", customer);

        ensureVehicleIsAvailable(vehicle, command.pickupAt(), command.returnAt());
        log.info("예약 가능한 차량 검증 - 차량 ID: {}, 기간({}) ~ ({})", vehicle.getId(), command.pickupAt(), command.returnAt());

        final RentalEntity rental = command.toRentalEntity(company, vehicle, customer);
        log.debug("예약 객체 생성 - 예약 ID: {}", rental.getId());

        final RentalEntity savedRental = rentalRepository.save(rental);
        log.info("예약 완료 - 예약 ID: {}", savedRental.getId());

        return savedRental;
    }

    /**
     * 예약 목록을 조회합니다.
     *
     * @param command 검색 조건 {@link RentalListCommand} 객체
     * @param pageable  페이징 및 정렬 정보 객체
     *
     * @return 검색 조건과 페이징에 따라 조회된 예약 {@link RentalEntity} 목록
     */
    public Page<RentalEntity> retrieveRentals(RentalListCommand command, Pageable pageable) {
        log.debug("예약 목록 요청 - {}, 페이지 : {}", command, pageable);

        return rentalRepository.findAll(
            command.companyId(), command.status(), command.keyword(), command.startAt(), command.endAt(), pageable
        );
    }

    /**
     * 예약의 상세 정보를 조회 API.
     *
     * @param command 조회할 렌탈의 정보 {@link RentalRetrieveCommand} 객체
     *
     * @return 조회된 예약 {@link RentalEntity} 객체
     * @throws CustomException 해당 ID의 렌탈 정보를 찾을 수 없는 경우 발생
     */
    public RentalEntity retrieveRental(RentalRetrieveCommand command) {
        log.debug("예약 상세 요청 - {}", command);

        return rentalRepository.findById(command.id())
            .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND, command.id()));
    }


    /**
     * 예약의 상태를 업데이트합니다.
     *
     * @param command 예약 상태를 변경할 {@link RentalUpdateStatusCommand} 객체
     *
     * @return 상태가 업데이트된 예약 {@link RentalEntity} 객체
     * @throws CustomException 해당 ID의 렌탈 정보를 찾을 수 없는 경우 발생
     */
    @Transactional
    public RentalEntity updateRentalStatus(RentalUpdateStatusCommand command) {
        log.info("예약 상태 변경 요청 - {}", command);

        final RentalEntity rental = rentalRepository.findById(command.id())
            .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND, command.id()));
        log.info("예약 정보 조회 - {}", rental);

        rental.changeStatus(command.status());
        log.info("예약 상태 변경 - 변경할 상태 : {}, 변경된 예약 : {}", command.status(), rental);

        if (rental.getStatus() == RentalStatus.RETURNED) {
            final DrivingLogEntity drivingLog = drivingLogRepository.findByRental(rental)
                .orElseThrow(() -> new CustomException(DrivingLogErrorCode.DRIVING_LOG_NOT_FOUND, command.id()));
            log.info("운행일지 정보 조회 - {}", drivingLog);

            drivingLog.completed();
            log.info("운행일지 상태 변경 - 변경된 운행 일지 : {}", drivingLog);

            final DrivingLogEntity savedDrivingLog = drivingLogRepository.save(drivingLog);
            log.info("운행일지 상태 변경 완료 - 운행일지 ID: {}", savedDrivingLog.getId());
        }

        final RentalEntity savedRental = rentalRepository.save(rental);
        log.info("예약 상태 변경 완료 - 예약 ID: {}", savedRental.getId());

        return savedRental;
    }

    /**
     * 주어진 ID로 회사 정보를 조회합니다.
     *
     * @param companyId 조회할 회사의 고유 ID
     * @return 조회된 {@link CompanyEntity} 객체
     * @throws CustomException 해당 ID의 회사 정보를 찾을 수 없는 경우 발생
     */
    private CompanyEntity findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
            .orElseThrow(() -> new CustomException(CompanyErrorCode.COMPANY_NOT_FOUND, companyId));
    }

    /**
     * 주어진 ID로 차량 정보를 조회합니다.
     *
     * @param vehicleId 조회할 차량의 고유 ID
     * @return 조회된 {@link VehicleEntity} 객체
     * @throws CustomException 해당 ID의 차량 정보를 찾을 수 없는 경우 발생
     */
    private VehicleEntity findVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new CustomException(VehicleErrorCode.VEHICLE_NOT_FOUND, vehicleId));
    }

    /**
     * 주어진 ID로 고객 정보를 조회합니다.
     *
     * @param customerId 조회할 고객의 고유 ID
     * @return 조회된 {@link CustomerEntity} 객체
     * @throws CustomException 해당 ID의 고객 정보를 찾을 수 없는 경우 발생
     */
    private CustomerEntity findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(CustomerErrorCode.CUSTOMER_NOT_FOUND, customerId));
    }

    /**
     * 특정 차량이 주어진 픽업 및 반납 시간 동안 예약 가능한지 확인합니다.
     * 차량이 이미 '대기 중(PENDING)' 또는 '대여 중(RENTED)' 상태로 겹치는 예약이 있다면 {@link CustomException}을 발생시킵니다.
     *
     * @param vehicle  확인할 {@link VehicleEntity} 객체
     * @param pickupAt 대여 시작 시간
     * @param returnAt 반납 예정 시간
     * @throws CustomException 차량이 이미 해당 시간에 예약되어 있어 이용할 수 없는 경우 발생
     */
    private void ensureVehicleIsAvailable(VehicleEntity vehicle, LocalDateTime pickupAt, LocalDateTime returnAt) {
        // '대기 중' 또는 '대여 중' 상태인 렌탈 목록을 정의하여 해당 상태의 겹치는 예약을 검사합니다.
        final List<RentalStatus> availableStatuses = Arrays.asList(RentalStatus.PENDING, RentalStatus.RENTED);

        // 주어진 차량, 상태 목록, 픽업/반납 시간을 기준으로 겹치는 렌탈 정보를 조회합니다.
        final List<RentalEntity> overlappingRentals = rentalRepository.findOverlappingRentalsByVehicleAndStatuses(
            vehicle, availableStatuses, pickupAt, returnAt
        );

        if (!overlappingRentals.isEmpty()) {
            throw new CustomException(RentalErrorCode.RENTAL_VEHICLE_UNAVAILABLE);
        }
    }
}
