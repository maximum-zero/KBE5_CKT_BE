package kernel360.ckt.admin.service;

import kernel360.ckt.admin.dto.request.CompanyCreateRequest;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyEntity create(CompanyCreateRequest companyCreateRequest) {
        return companyRepository.save(companyCreateRequest.toEntity());
    }

}
