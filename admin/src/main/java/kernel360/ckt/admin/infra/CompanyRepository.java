package kernel360.ckt.admin.infra;

import kernel360.ckt.core.domain.entity.CompanyEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    CompanyEntity save(CompanyEntity companyEntity);
    void deleteById(Long companyId);
    List<CompanyEntity> findAll();
    Optional<CompanyEntity> findById(Long companyId);
}
