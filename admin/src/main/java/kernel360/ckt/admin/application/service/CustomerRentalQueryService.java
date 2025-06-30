package kernel360.ckt.admin.application.service;

import kernel360.ckt.admin.application.port.RentalRepository;
import kernel360.ckt.admin.infra.jpa.RentalJpaRepository;
import kernel360.ckt.admin.ui.dto.response.*;
import kernel360.ckt.core.domain.enums.RentalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class CustomerRentalQueryService {

    private final RentalRepository rentalRepository;

    public RentalOverviewResponse getRentalSummary(Long customerId) {
        long total = rentalRepository.countByCustomerId(customerId);
        long active = rentalRepository.countByCustomerIdAndStatus(customerId, RentalStatus.RENTED);

        // 현재 대여 1건
        CurrentRentalResponse current = rentalRepository
            .findLatestRentalByCustomerIdAndStatus(customerId, RentalStatus.RENTED)
            .map(r -> CurrentRentalResponse.builder()
                .vehicleName(r.getVehicle().getModelName())
                .licensePlate(r.getVehicle().getRegistrationNumber())
                .startDate(r.getPickupAt().toLocalDate())
                .endDate(r.getReturnAt().toLocalDate())
                .status(r.getStatus().getValue())
                .build())
            .orElse(null);

        // 전체 이력
        List<RentalHistoryResponse> history = rentalRepository.findAllByCustomerIdFetchVehicle(customerId).stream()
            .map(r -> RentalHistoryResponse.builder()
                .reservationId("R-" + r.getId())
                .vehicleName(r.getVehicle().getModelName())
                .licensePlate(r.getVehicle().getRegistrationNumber())
                .startDate(r.getPickupAt().toLocalDate())
                .endDate(r.getReturnAt().toLocalDate())
                .status(r.getStatus().getValue())
                .build())
            .toList();

        return new RentalOverviewResponse((int) total, (int) active, current, history);
    }
}
