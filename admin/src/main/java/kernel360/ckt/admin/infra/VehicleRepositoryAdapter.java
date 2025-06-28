package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.VehicleRepository;
import kernel360.ckt.admin.infra.jpa.VehicleJpaRepository;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VehicleRepositoryAdapter implements VehicleRepository {
    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public VehicleEntity save(VehicleEntity vehicleEntity) {
        return vehicleJpaRepository.save(vehicleEntity);
    }

    @Override
    public Page<VehicleEntity> findAll(VehicleStatus status, String keyword, Pageable pageable) {
        return vehicleJpaRepository.findAll(status, keyword, pageable);
    }

    @Override
    public Optional<VehicleEntity> findById(Long vehicleId) {
        return vehicleJpaRepository.findByIdAndDeleteYnFalse(vehicleId);
    }

    @Override
    public Optional<VehicleEntity> findByRegistrationNumber(String registrationNumber) {
        return vehicleJpaRepository.findByRegistrationNumberAndDeleteYnFalse(registrationNumber);
    }

    @Override
    public long count() {
        return vehicleJpaRepository.count();
    }

    @Override
    public List<VehicleEntity> searchAvailableVehiclesByKeyword(String keyword, LocalDateTime pickupAt, LocalDateTime returnAt) {
        return vehicleJpaRepository.searchAvailableVehiclesByKeyword(keyword, pickupAt, returnAt);
    }
}
