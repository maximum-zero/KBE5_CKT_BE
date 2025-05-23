package kernel360.ckt.admin.application;

import kernel360.ckt.admin.ui.dto.request.CompanyCreateRequest;
import kernel360.ckt.admin.infra.repository.CompanyRepositoryImpl;
import kernel360.ckt.admin.ui.dto.request.CompanyUpdateRequest;
import kernel360.ckt.core.common.CompanyErrorCode;
import kernel360.ckt.core.common.CustomException;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepositoryImpl companyRepositoryImpl;

    public CompanyEntity create(CompanyCreateRequest companyCreateRequest) {
        return companyRepositoryImpl.save(companyCreateRequest.toEntity());
    }

    public CompanyEntity findById(Long companyId) {
        return companyRepositoryImpl.findById(companyId)
            .orElseThrow(() -> new CustomException(CompanyErrorCode.COMPANY_NOT_FOUND));
    }

    public List<CompanyEntity> findAll() {
        return companyRepositoryImpl.findAll();
    }

    public CompanyEntity update(Long companyId, CompanyUpdateRequest companyUpdateRequest) {
        final CompanyEntity companyEntity = findById(companyId);
        companyEntity.update(companyUpdateRequest.name(), companyUpdateRequest.ceoName(), companyUpdateRequest.telNumber());
        return companyRepositoryImpl.update(companyEntity);
    }

    public void delete(Long companyId) {
        companyRepositoryImpl.delete(companyId);
    }

}
