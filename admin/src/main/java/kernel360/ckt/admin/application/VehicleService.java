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

    public List<RunningVehicleResponse> getRunningVehicles() {
        return rentalRepository.findRentalsByStatus(RentalStatus.RENTED).stream()
            .map(RunningVehicleResponse::from)
            .toList();
    }

    public Optional<GpsPointResponse> getLastTracePoint(Long vehicleId) {
        return rentalJpaRepository.findLatestTraceJsonByVehicleId(vehicleId)
            .flatMap(json -> {
                try {
                    JsonNode traceArray = objectMapper.readTree(json);
                    if (traceArray.isEmpty()) return Optional.empty();

                    JsonNode last = traceArray.get(traceArray.size() - 1);
                    return Optional.of(new GpsPointResponse(
                        last.get("lat").asText(),
                        last.get("lon").asText(),
                        last.get("ang").asText(),
                        last.get("spd").asText()
                    ));
                } catch (Exception e) {
                    return Optional.empty();
                }
            });
    }


}
