package kernel360.ckt.collector.application.service;

import jakarta.persistence.EntityNotFoundException;
import kernel360.ckt.collector.application.port.VehicleRepository;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleEntity findById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 차량입니다." + vehicleId));
    }
}
