package kernel360.ckt.collector.infra;

import java.util.Optional;
import kernel360.ckt.collector.application.port.VehicleRepository;
import kernel360.ckt.collector.infra.jpa.VehicleJpaRepository;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VehicleRepositoryAdapter implements VehicleRepository {
    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public Optional<VehicleEntity> findById(Long vehicleId) {
        return vehicleJpaRepository.findById(vehicleId);
    }

    @Override
    public VehicleEntity save(VehicleEntity vehicle) {
        return vehicleJpaRepository.save(vehicle);
    }


}
