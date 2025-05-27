package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.application.VehicleService;
import kernel360.ckt.admin.application.command.CreateVehicleCommand;
import kernel360.ckt.admin.ui.dto.request.VehicleCreateRequest;
import kernel360.ckt.admin.ui.dto.request.VehicleUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.*;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.VehicleEntity;
import kernel360.ckt.core.domain.enums.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
@RestController
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    CommonResponse<VehicleResponse> create(@RequestBody VehicleCreateRequest request) {
        final CreateVehicleCommand command = request.toCommand();
        final VehicleEntity vehicleEntity = vehicleService.create(command);
        return CommonResponse.success(VehicleResponse.from(vehicleEntity));
    }

    @PutMapping("/{id}")
    CommonResponse<VehicleUpdateResponse> updateVehicle(
        @PathVariable Long id,
        @RequestBody VehicleUpdateRequest request
    ) {
        final VehicleEntity updated = vehicleService.update(id, request);
        return CommonResponse.success(VehicleUpdateResponse.from(updated));
    }

    @GetMapping
    CommonResponse<VehicleListResponse> getAllVehicles(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) VehicleStatus status,
        @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VehicleEntity> vehiclePage = vehicleService.searchVehicles(status, keyword, pageable);
        return CommonResponse.success(VehicleListResponse.from(vehiclePage));
    }

    @GetMapping("/{id}")
    CommonResponse<VehicleResponse> selectVehicle(@PathVariable Long id) {
        final VehicleEntity vehicleEntity = vehicleService.findById(id);
        return CommonResponse.success(VehicleResponse.from(vehicleEntity));
    }

    @DeleteMapping("/{id}")
    CommonResponse<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.delete(id);
        return CommonResponse.success(null);
    }
}
