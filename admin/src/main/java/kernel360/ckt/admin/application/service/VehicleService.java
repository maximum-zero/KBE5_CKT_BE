package kernel360.ckt.admin.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kernel360.ckt.admin.application.service.command.CreateVehicleCommand;
import kernel360.ckt.admin.application.service.command.UpdateVehicleCommand;
import kernel360.ckt.admin.infra.jpa.RentalJpaRepository;
import kernel360.ckt.admin.ui.dto.response.ControlTowerSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.GpsPointResponse;
import kernel360.ckt.admin.ui.dto.response.RunningVehicleResponse;
import kernel360.ckt.core.common.error.VehicleErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import kernel360.ckt.admin.application.port.RentalRepository;
import kernel360.ckt.admin.application.port.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final RentalJpaRepository rentalJpaRepository;
    private final ObjectMapper objectMapper;

    public VehicleEntity create(CreateVehicleCommand command) {
        vehicleRepository.findByRegistrationNumber(command.getRegistrationNumber())
            .ifPresent(vehicle -> {
                throw new CustomException(VehicleErrorCode.DUPLICATE_REGISTRATION_NUMBER);
            });

        return vehicleRepository.save(command.toEntity());
    }

    @Transactional
    public VehicleEntity update(Long id, UpdateVehicleCommand command) {
        VehicleEntity vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new CustomException(VehicleErrorCode.VEHICLE_NOT_FOUND));

        vehicle.updateVehicle(
            command.getModelYear(),
            command.getManufacturer(),
            command.getModelName(),
            command.getBatteryVoltage(),
            command.getFuelType(),
            command.getTransmissionType(),
            command.getMemo()
        );

        return vehicleRepository.save(vehicle);
    }

    public Page<VehicleEntity> searchVehicles(VehicleStatus status, String keyword, Pageable pageable) {
        return vehicleRepository.findAll(status, keyword, pageable);
    }

    public VehicleEntity findById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + vehicleId));
    }

    public void delete(Long vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

    public ControlTowerSummaryResponse getControlTowerSummary() {
        long total = vehicleRepository.count();
        long running = rentalRepository.countVehiclesByStatus(RentalStatus.RENTED);
        long stopped = total - running;
        return ControlTowerSummaryResponse.of((int) total, (int) running, (int) stopped);
    }

    public List<RunningVehicleResponse> getVehicleLocations() {
        List<RentalEntity> runningRentals = rentalJpaRepository.findRentedRentals();

        return runningRentals.stream()
            .map(rental -> {
                Long vehicleId = rental.getVehicle().getId();
                Optional<String> traceJsonOpt = rentalJpaRepository.findLatestTraceJsonByVehicleId(vehicleId);

                GpsPointResponse location = traceJsonOpt
                    .map(this::parseGpsFromTraceJson)
                    .orElse(null);

                return RunningVehicleResponse.from(rental, location);
            })
            .toList();
    }

    private GpsPointResponse parseGpsFromTraceJson(String traceJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(traceJson);

            if (root.isArray() && root.size() > 0) {
                JsonNode last = root.get(root.size() - 1); // 마지막 위치 정보

                return new GpsPointResponse(
                    last.path("lat").asText(null),
                    last.path("lon").asText(null),
                    last.path("ang").asText(null),
                    last.path("spd").asText(null)
                );
            }
        } catch (Exception e) {
            // 로그 남기거나 무시
        }
        return null;
    }

}
