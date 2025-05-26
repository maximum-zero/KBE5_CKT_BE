package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.ui.dto.request.CompanyCreateRequest;
import kernel360.ckt.admin.ui.dto.request.CompanyUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.CompanyCreateResponse;
import kernel360.ckt.admin.application.CompanyService;
import kernel360.ckt.admin.ui.dto.response.CompanyListResponse;
import kernel360.ckt.admin.ui.dto.response.CompanyResponse;
import kernel360.ckt.core.common.response.CommonResponse;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    CommonResponse<CompanyListResponse> selectCompanyList() {
        final List<CompanyEntity> companyListEntity = companyService.findAll();
        return CommonResponse.success(CompanyListResponse.from(companyListEntity));
    }

    @GetMapping("/{id}")
    CommonResponse<CompanyResponse> selectCompany(@PathVariable Long id) {
        final CompanyEntity companyEntity = companyService.findById(id);
        return CommonResponse.success(CompanyResponse.from(companyEntity));
    }

    @PostMapping
    CommonResponse<CompanyCreateResponse> createCompany(@RequestBody CompanyCreateRequest request) {
        final CompanyEntity companyEntity = companyService.create(request);
        return CommonResponse.success(CompanyCreateResponse.from(companyEntity));
    }

    @PutMapping("/{id}")
    CommonResponse<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyUpdateRequest request) {
        final CompanyEntity companyEntity = companyService.update(id, request);
        return CommonResponse.success(CompanyResponse.from(companyEntity));
    }

    @DeleteMapping("/{id}")
    CommonResponse<Void> deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
        return CommonResponse.success(null);
    }

}
