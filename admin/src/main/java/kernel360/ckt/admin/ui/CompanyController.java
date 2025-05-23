package kernel360.ckt.admin.ui;

import kernel360.ckt.admin.ui.dto.request.CompanyCreateRequest;
import kernel360.ckt.admin.ui.dto.request.CompanyUpdateRequest;
import kernel360.ckt.admin.ui.dto.response.CompanyCreateResponse;
import kernel360.ckt.admin.application.CompanyService;
import kernel360.ckt.admin.ui.dto.response.CompanyListResponse;
import kernel360.ckt.admin.ui.dto.response.CompanyResponse;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    ResponseEntity<CompanyListResponse> selectCompanyList() {
        final List<CompanyEntity> companyListEntity = companyService.findAll();
        return ResponseEntity.ok(
            CompanyListResponse.from(companyListEntity)
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<CompanyResponse> selectCompany(@PathVariable Long id) {
        final CompanyEntity companyEntity = companyService.findById(id);
        return ResponseEntity.ok(
            CompanyResponse.from(companyEntity)
        );
    }

    @PostMapping
    ResponseEntity<CompanyCreateResponse> createCompany(@RequestBody CompanyCreateRequest request) {
        final CompanyEntity companyEntity = companyService.create(request);
        return ResponseEntity.ok(
            CompanyCreateResponse.from(companyEntity)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyUpdateRequest request) {
        final CompanyEntity companyEntity = companyService.update(id, request);
        return ResponseEntity.ok(
            CompanyResponse.from(companyEntity)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.ok(null);
    }

}
