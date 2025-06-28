package kernel360.ckt.admin.application.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import kernel360.ckt.admin.application.service.command.*;
import kernel360.ckt.admin.application.port.DrivingLogRepository;
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
     * @param command 예약 생성에 필요한 데이터를 담고 있는 {@link CreateRentalCommand} 객체
     *
     * @return 생성된 {@link RentalEntity} 객체
     * @throws CustomException 회사, 차량, 또는 고객 정보를 찾을 수 없거나, 차량이 해당 시간에 이용 불가능한 경우 발생
     */
    @Transactional
    public RentalEntity createRental(CreateRentalCommand command) {
        log.info("예약 생성 요청 - 요청값 : {}", command);

        final CompanyEntity company = findCompanyById(command.companyId());
        log.info("회사 정보 조회 - 회사 ID : {}", company.getId());

        final VehicleEntity vehicle = findVehicleById(command.companyId(), command.vehicleId());
        log.info("차량 정보 조회 - 차량 ID : {}", vehicle.getId());

        final CustomerEntity customer = findCustomerById(command.customerId());
        log.info("고객 정보 조회 - 고객 ID : {}", customer.getId());

        ensureVehicleIsAvailable(vehicle, command.pickupAt(), command.returnAt(), null);
        log.info("예약 가능한 차량 검증 - 차량 ID: {}, 기간({}) ~ ({})", vehicle.getId(), command.pickupAt(), command.returnAt());

        final RentalEntity rental = command.toRentalEntity(company, vehicle, customer);
        log.debug("예약 객체 생성 - 예약 : {}", rental);

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
     * @param command 조회할 예약의 정보 {@link RentalRetrieveCommand} 객체
     *
     * @return 조회된 예약 {@link RentalEntity} 객체
     * @throws CustomException 해당 ID의 예약 정보를 찾을 수 없는 경우 발생
     */
    public RentalEntity retrieveRental(RentalRetrieveCommand command) {
        log.debug("예약 상세 요청 - 요청값 : {}", command);

        return rentalRepository.findById(command.id())
            .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND, command.id()));
    }

    /**
     * 예약을 변경합니다.
     * 대기중 (PENDING) 상태에서만 정보를 변경할 수 있습니다.
     *
     * @param command 예약 변경에 필요한 데이터를 담고 있는 {@link RentalUpdateCommand} 객체
     *
     * @return 변경된 {@link RentalEntity} 객체
     * @throws CustomException 회사, 차량, 또는 고객 정보를 찾을 수 없거나, 차량이 해당 시간에 이용 불가능한 경우 발생
     */
    @Transactional
    public RentalEntity updateRental(RentalUpdateCommand command) {
        log.info("예약 정보 변경 요청 - 요청값 : {}", command);

        final RentalEntity rental = rentalRepository.findById(command.id())
            .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND, command.id()));
        log.info("예약 정보 조회 - 예약 : {}", rental);

        log.debug("정보 변경할 예약의 상태 : {}", rental.getStatus());
        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new CustomException(RentalErrorCode.RENTAL_UPDATE_NOT_ALLOWED, rental.getStatus().name());
        }

        final LocalDateTime updatedPickupAt = command.pickupAt() != null ? command.pickupAt() : rental.getPickupAt();
        final LocalDateTime updatedReturnAt = command.returnAt() != null ? command.returnAt() : rental.getReturnAt();
        CompanyEntity updatedCompany = rental.getCompany();
        VehicleEntity updatedVehicle = rental.getVehicle();
        CustomerEntity updatedCustomer = rental.getCustomer();

        boolean checkedAvailability = false;
        if (command.companyId() != null && !command.companyId().equals(rental.getCompany().getId())) {
            updatedCompany = findCompanyById(command.companyId());
            log.info("회사 정보 변경 - 회사 ID: {}", updatedCompany.getId());
        }

        if (command.vehicleId() != null && !command.vehicleId().equals(rental.getVehicle().getId())) {
            updatedVehicle = findVehicleById(command.companyId(), command.vehicleId());
            log.info("차량 정보 변경 - 차량 ID: {}", updatedVehicle.getId());
            checkedAvailability = true;
        }

        if (command.customerId() != null && !command.customerId().equals(rental.getCustomer().getId())) {
            updatedCustomer = findCustomerById(command.customerId());
            log.info("고객 정보 변경 - 고객 ID: {}", updatedCustomer.getId());
        }

        if (!updatedPickupAt.equals(rental.getPickupAt()) || !updatedReturnAt.equals(rental.getReturnAt())) {
            log.info("예약 기간 변경 - 픽업시간: {} ~ 반납시간: {}", updatedPickupAt, updatedReturnAt);
            checkedAvailability = true;
        }

        if (checkedAvailability) {
            ensureVehicleIsAvailable(updatedVehicle, updatedPickupAt, updatedReturnAt, rental.getId());
            log.info("예약 가능한 차량 재검증 - 차량 ID: {}, 기간(픽업시간: {} ~ 반납시간: {})", updatedVehicle.getId(), updatedPickupAt, updatedReturnAt);
        }

        rental.update(updatedCompany, updatedVehicle, updatedCustomer, updatedPickupAt, updatedReturnAt, command.memo());
        log.info("예약 정보 변경 - 예약 ID: {}, 변경된 예약: {}", rental.getId(), rental);

        final RentalEntity updatedRental = rentalRepository.save(rental);
        log.info("예약 정보 변경 완료 - 예약 ID: {}", updatedRental.getId());

        return updatedRental;
    }

    /**
     * 예약의 메모를 변경합니다.
     *
     * @param command 예약의 메모 변경에 필요한 데이터를 담고 있는 {@link RentalUpdateMemoCommand} 객체
     *
     * @return 변경된 {@link RentalEntity} 객체
     * @throws CustomException 회사, 차량, 또는 고객 정보를 찾을 수 없거나, 차량이 해당 시간에 이용 불가능한 경우 발생
     */
    @Transactional
    public RentalEntity updateRentalMemo(RentalUpdateMemoCommand command) {
        log.info("예약의 메모 변경 요청 - 요청값 : {}", command);

        final RentalEntity rental = rentalRepository.findById(command.id())
            .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND, command.id()));
        log.info("변경할 예약 정보 조회 - 예약 : {}", rental);

        rental.updateMemo(command.memo());
        log.info("변경된 예약의 객체 - 예약 : {}", rental);

        final RentalEntity updatedRental = rentalRepository.save(rental);
        log.info("예약 정보 변경 완료 - 예약 ID: {}", updatedRental.getId());

        return updatedRental;
    }

    /**
     * 예약의 상태를 업데이트합니다.
     *
     * @param command 예약 상태를 변경할 {@link RentalUpdateStatusCommand} 객체
     *
     * @return 상태가 업데이트된 예약 {@link RentalEntity} 객체
     * @throws CustomException 해당 ID의 예약 정보를 찾을 수 없는 경우 발생
     */
    @Transactional
    public RentalEntity updateRentalStatus(RentalUpdateStatusCommand command) {
        log.info("예약 상태 변경 요청 - 요청값 : {}", command);

        final RentalEntity rental = rentalRepository.findById(command.id())
            .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND, command.id()));
        log.info("예약 정보 조회 - 예약 : {}", rental);

        rental.changeStatus(command.status());
        log.info("예약 상태 변경 - 변경할 상태 : {}, 변경된 예약 : {}", command.status(), rental);

        if (rental.getStatus() == RentalStatus.RETURNED) {
            final DrivingLogEntity drivingLog = drivingLogRepository.findByRental(rental)
                .orElse(null);

            // 운행일지가 있으면 처리
            if (drivingLog != null && drivingLog.getId() != null) {
                log.info("해당 예약에 해당하는 운행일지가 존재 - 운행일지 ID : {}", drivingLog.getId());

                drivingLog.completed();
                log.info("운행일지 상태 변경 - 변경된 운행 일지 : ID {}, 상태: {}", drivingLog.getId(), drivingLog.getStatus());

                final DrivingLogEntity savedDrivingLog = drivingLogRepository.save(drivingLog);
                log.info("운행일지 상태 변경 완료 - 운행일지 ID: {}", savedDrivingLog.getId());
            }
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
     * @param companyId 조회할 회사의 고유 ID
     * @param vehicleId 조회할 차량의 고유 ID
     * @return 조회된 {@link VehicleEntity} 객체
     * @throws CustomException 해당 ID의 차량 정보를 찾을 수 없는 경우 발생
     */
    private VehicleEntity findVehicleById(Long companyId, Long vehicleId) {
        return vehicleRepository.findById(companyId, vehicleId)
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
     * 특정 차량이 주어진 픽업 및 반납 시간 동안 다른 예약과 겹치지 않고 예약 가능한지 확인합니다.
     *
     * @param vehicle 확인할 {@link VehicleEntity} 객체
     * @param pickupAt 대여 시작 시간
     * @param returnAt 반납 예정 시간
     * @param excludeRentalId 검사에서 제외할 현재 예약의 ID
     * @throws CustomException 차량이 이미 해당 시간에 예약되어 있어 이용할 수 없는 경우 발생
     */
    private void ensureVehicleIsAvailable(VehicleEntity vehicle, LocalDateTime pickupAt, LocalDateTime returnAt, Long excludeRentalId) {
        final List<RentalStatus> activeStatuses = Arrays.asList(RentalStatus.PENDING, RentalStatus.RENTED);

        List<RentalEntity> overlappingRentals;
        if (excludeRentalId != null) {
            overlappingRentals = rentalRepository.findOverlappingRentalsByVehicleAndStatusesExcludingRental(
                vehicle, activeStatuses, pickupAt, returnAt, excludeRentalId
            );
        } else {
            overlappingRentals = rentalRepository.findOverlappingRentalsByVehicleAndStatusesExcludingRental(
                vehicle, activeStatuses, pickupAt, returnAt, null
            );
        }

        if (!overlappingRentals.isEmpty()) {
            throw new CustomException(RentalErrorCode.RENTAL_VEHICLE_UNAVAILABLE);
        }
    }
}
