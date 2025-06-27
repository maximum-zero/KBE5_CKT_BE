package kernel360.ckt.admin.ui.dto.response;

import kernel360.ckt.admin.application.service.dto.DrivingLogListDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record DrivingLogListResponse(
    List<DrivingLogListDto> list,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public static DrivingLogListResponse from(Page<DrivingLogListDto> pageData) {
        List<DrivingLogListDto> drivingLogResponseList = pageData.getContent().stream().toList();
        return new DrivingLogListResponse(
            drivingLogResponseList,
            pageData.getNumber(),
            pageData.getSize(),
            pageData.getTotalElements(),
            pageData.getTotalPages()
        );
    }
}
