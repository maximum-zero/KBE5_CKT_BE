package kernel360.ckt.admin.application;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kernel360.ckt.admin.application.command.CreateVehicleCommand;
import kernel360.ckt.admin.application.command.VehicleKeywordCommand;
import kernel360.ckt.admin.ui.dto.request.VehicleUpdateRequest;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.RentalStatus;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import kernel360.ckt.core.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

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

    public List<VehicleEntity> searchKeyword(VehicleKeywordCommand command) {
        final String keyword = command.getKeyword();
        return vehicleRepository.findAvailableVehicles(keyword, RentalStatus.RENTED);
    }
}
