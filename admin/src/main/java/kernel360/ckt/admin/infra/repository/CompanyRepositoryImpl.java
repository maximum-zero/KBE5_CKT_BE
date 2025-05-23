package kernel360.ckt.admin.infra.repository;

import kernel360.ckt.admin.infra.repository.jpa.CompanyJpaRepository;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import kernel360.ckt.core.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;

    @Override
    public List<CompanyEntity> findAll() {
        return companyJpaRepository.findAll();
    }

    @Override
    public Optional<CompanyEntity> findById(Long companyId) {
        return companyJpaRepository.findById(companyId);
    }

    @Override
    public CompanyEntity save(CompanyEntity companyEntity) {
        return companyJpaRepository.save(companyEntity);
    }

    @Override
    public CompanyEntity update(CompanyEntity companyEntity) {
        return companyJpaRepository.save(companyEntity);
    }

    @Override
    public void delete(Long companyId) {
        companyJpaRepository.deleteById(companyId);
    }
}
