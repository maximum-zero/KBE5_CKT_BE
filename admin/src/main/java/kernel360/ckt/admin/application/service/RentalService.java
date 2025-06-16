package kernel360.ckt.admin.application.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kernel360.ckt.admin.application.service.command.CreateRentalCommand;
import kernel360.ckt.admin.application.service.command.RentalListCommand;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.entity.DrivingLogEntity;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.admin.application.port.CompanyRepository;
import kernel360.ckt.admin.application.port.CustomerRepository;
import kernel360.ckt.admin.application.port.DrivingLogRepository;
import kernel360.ckt.admin.application.port.RentalRepository;
import kernel360.ckt.admin.application.port.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 새로운 렌탈(예약)을 생성하는 메서드.
     * Command 객체로부터 필요한 정보를 받아 엔티티를 조회하고, 유효성 검사 후 렌탈 엔티티를 저장한다.
     *
     * @param command 렌탈 생성을 위한 데이터를 담고 있는 CreateRentalCommand 객체
     * @return 생성 및 저장된 RentalEntity 객체
     * @throws NoSuchElementException 존재하지 않는 회사, 차량, 고객 ID가 전달될 경우 발생
     * @throws IllegalStateException 해당 시간대에 이미 다른 예약이 존재하여 차량을 대여할 수 없을 경우 발생
     */
    @Transactional
    public RentalEntity createRental(CreateRentalCommand command) {
        final CompanyEntity company = findCompanyById(command.getCompanyId());
        final VehicleEntity vehicle = findVehicleById(command.getVehicleId());
        final CustomerEntity customer = findCustomerById(command.getCustomerId());

        ensureVehicleIsAvailable(vehicle, command.getPickupAt(), command.getReturnAt());

        final RentalEntity rental = RentalEntity.create(
            company,
            vehicle,
            customer,
            command.getPickupAt(),
            command.getReturnAt(),
            command.getMemo()
        );

        return rentalRepository.save(rental);
    }

    /**
     * 특정 조건에 맞는 렌탈(예약) 목록을 페이징하여 조회합니다.
     * Company ID, 렌탈 상태, 키워드, 시간 범위 등을 기준으로 필터링할 수 있습니다.
     *
     * @param command 렌탈 목록 검색 조건을 담고 있는 RentalListCommand 객체
     * @param pageable 페이징 및 정렬 정보를 담은 객체
     * @return 검색 조건에 해당하는 RentalEntity의 페이징된 목록
     */
    public Page<RentalEntity> searchRentals(RentalListCommand command, Pageable pageable) {
        return rentalRepository.findAll(
            command.getCompanyId(),
            command.getStatus(),
            command.getKeyword(),
            command.getStartAt(),
            command.getEndAt(),
            pageable
        );
    }

    /**
     * ID를 사용하여 단일 렌탈 엔티티를 조회합니다.
     * 해당 ID의 렌탈이 존재하지 않을 경우 {@link NoSuchElementException}을 발생시킵니다.
     *
     * @param id 조회할 렌탈의 ID
     * @return 해당 ID를 가진 렌탈 엔티티
     * @throws NoSuchElementException 해당 ID의 렌탈이 존재하지 않을 경우
     */
    public RentalEntity findById(Long id) {
        return rentalRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다: " + id));
    }

    /**
     * 렌탈(예약)의 상태를 변경합니다.
     * 상태 변경 시 비즈니스 규칙을 준수하며, 필요 시 운행일지 상태를 업데이트합니다.
     *
     * @param id     변경할 렌탈의 ID
     * @param status 변경하고자 하는 새로운 렌탈 상태
     * @return 상태가 업데이트된 RentalEntity 객체
     * @throws NoSuchElementException 렌탈, 운행 일지를 찾을 수 없는 경우
     * @throws IllegalStateException 유효하지 않은 상태 전환이 요청될 경우 (예: RENTED -> PENDING)
     */
    @Transactional
    public RentalEntity updateRentalStatus(Long id, RentalStatus status) {
        final RentalEntity rental = rentalRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다: " + id));

        rental.changeStatus(status);
        if (rental.getStatus() == RentalStatus.RETURNED) {
            final DrivingLogEntity drivingLog = drivingLogRepository.findByRental(rental)
                .orElseThrow(() -> new NoSuchElementException("운행 일지를 찾을 수 없습니다: " + id));

            drivingLog.completed();
            drivingLogRepository.save(drivingLog);
        }

        return rentalRepository.save(rental);
    }


    /**
     * ID로 회사를 조회하고, 없으면 예외를 발생시킵니다.
     *
     * @param companyId 조회할 회사의 ID
     * @return 조회된 CompanyEntity
     * @throws NoSuchElementException 회사를 찾을 수 없는 경우
     */
    private CompanyEntity findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
            .orElseThrow(() -> new NoSuchElementException("회사 정보를 찾을 수 없습니다: " + companyId));
    }

    /**
     * ID로 차량을 조회하고, 없으면 예외를 발생시킵니다.
     *
     * @param vehicleId 조회할 차량의 ID
     * @return 조회된 VehicleEntity
     * @throws NoSuchElementException 차량을 찾을 수 없는 경우
     */
    private VehicleEntity findVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new NoSuchElementException("차량 정보를 찾을 수 없습니다: " + vehicleId));
    }

    /**
     * ID로 고객을 조회하고, 없으면 예외를 발생시킵니다.
     *
     * @param customerId 조회할 고객의 ID
     * @return 조회된 CustomerEntity
     * @throws NoSuchElementException 고객을 찾을 수 없는 경우
     */
    private CustomerEntity findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new NoSuchElementException("고객 정보를 찾을 수 없습니다: " + customerId));
    }

    /**
     * 특정 차량이 주어진 시간에 대여 가능한지/예약 가능한지 확인합니다.
     * 중복된 예약이 있으면 예외를 발생시킵니다.
     *
     * @param vehicle  확인할 차량 엔티티
     * @param pickupAt 픽업 시간
     * @param returnAt 반납 시간
     * @throws IllegalStateException 해당 시간대에 이미 다른 활성 예약이 존재하는 경우
     */
    private void ensureVehicleIsAvailable(VehicleEntity vehicle, LocalDateTime pickupAt, LocalDateTime returnAt) {
        // PENDING (대기 중) 또는 RENTED (대여 중) 상태의 겹치는 예약을 검사합니다.
        final List<RentalStatus> activeOrPendingStatuses = Arrays.asList(RentalStatus.PENDING, RentalStatus.RENTED);
        final List<RentalEntity> overlappingRentals = rentalRepository.findOverlappingRentalsByVehicleAndStatuses(
            vehicle,
            activeOrPendingStatuses,
            pickupAt,
            returnAt
        );

        if (!overlappingRentals.isEmpty()) {
            throw new IllegalStateException("해당 차량은 이미 예약이 존재합니다.");
        }
    }
}
