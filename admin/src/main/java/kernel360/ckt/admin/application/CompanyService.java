package kernel360.ckt.admin.application;

import kernel360.ckt.admin.application.command.CreateCompanyCommand;
import kernel360.ckt.admin.application.command.UpdateCompanyCommand;
import kernel360.ckt.admin.ui.dto.request.CompanyCreateRequest;
import kernel360.ckt.admin.ui.dto.request.CompanyUpdateRequest;
import kernel360.ckt.core.common.error.CompanyErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyEntity create(CreateCompanyCommand createCompanyCommand) {
        return companyRepository.save(createCompanyCommand.toEntity());
    }

    public CompanyEntity findById(Long companyId) {
        return companyRepository.findById(companyId)
            .orElseThrow(() -> new CustomException(CompanyErrorCode.COMPANY_NOT_FOUND));
    }

    public List<CompanyEntity> findAll() {
        return companyRepository.findAll();
    }

    public CompanyEntity update(Long companyId, UpdateCompanyCommand updateCompanyCommand) {
        final CompanyEntity companyEntity = findById(companyId);
        companyEntity.update(updateCompanyCommand.getName(), updateCompanyCommand.getCeoName(), updateCompanyCommand.getTelNumber());
        return companyRepository.save(companyEntity);
    }

    public void delete(Long companyId) {
        companyRepository.deleteById(companyId);
    }

    public CompanyEntity findMyCompany(Long companyId) { return findById(companyId); }
}
