package kernel360.ckt.admin.controller;

import kernel360.ckt.admin.dto.request.CompanyCreateRequest;
import kernel360.ckt.admin.dto.response.CompanyCreateResponse;
import kernel360.ckt.admin.service.CompanyService;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
@RestController
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    ResponseEntity<CompanyCreateResponse> createCompany(@RequestBody CompanyCreateRequest request) {
        CompanyEntity companyEntity = companyService.create(request);
        return ResponseEntity.ok(
            CompanyCreateResponse.from(companyEntity)
        );
    }

}
