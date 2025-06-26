package kernel360.ckt.collector.infra;

import kernel360.ckt.collector.application.port.VehicleEventRepository;
import kernel360.ckt.collector.infra.jpa.VehicleEventJpaRepository;
import kernel360.ckt.core.domain.entity.VehicleEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VehicleEventRepositoryAdapter implements VehicleEventRepository {
    private final VehicleEventJpaRepository vehicleEventJpaRepository;

    @Override
    public VehicleEventEntity save(VehicleEventEntity vehicleEvent) {
        return vehicleEventJpaRepository.save(vehicleEvent);
    }

    @Override
    public Optional<VehicleEventEntity> findFirstByVehicleIdOrderByCreatedAtDesc(Long vehicleId) {
        return vehicleEventJpaRepository.findFirstByVehicleIdOrderByCreatedAtDesc(vehicleId);
    }
}
