package kernel360.ckt.admin.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kernel360.ckt.admin.application.command.CreateVehicleCommand;
import kernel360.ckt.admin.infra.repository.jpa.RentalJpaRepository;
import kernel360.ckt.admin.ui.dto.request.VehicleUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.ControlTowerSummaryResponse;
import kernel360.ckt.admin.ui.dto.response.GpsPointResponse;
import kernel360.ckt.admin.ui.dto.response.RunningVehicleResponse;
import kernel360.ckt.core.domain.entity.RentalEntity;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import kernel360.ckt.core.repository.RentalRepository;
import kernel360.ckt.core.repository.VehicleRepository;
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
        return vehicleRepository.save(command.toEntity());
    }

    @Transactional
    public VehicleEntity update(Long id, VehicleUpdateRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));

        if (request.status() != null) {
            vehicle.updateStatus(request.status());
        }
        if (request.memo() != null) {
            vehicle.updateMemo(request.memo());
        }

        return vehicle;
    }

    public Page<VehicleEntity> findAll(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    public Page<VehicleEntity> searchVehicles(VehicleStatus status, String keyword, Pageable pageable) {
        return vehicleRepository.search(status, keyword, pageable);
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
