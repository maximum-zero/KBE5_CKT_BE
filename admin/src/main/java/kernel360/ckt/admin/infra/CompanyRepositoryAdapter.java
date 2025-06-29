package kernel360.ckt.admin.infra;

import kernel360.ckt.admin.application.port.CompanyRepository;
import kernel360.ckt.admin.infra.jpa.CompanyJpaRepository;
import kernel360.ckt.core.domain.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CompanyRepositoryAdapter implements CompanyRepository {
    private final CompanyJpaRepository companyJpaRepository;

    @Override
    public CompanyEntity save(CompanyEntity companyEntity) {
        return companyJpaRepository.save(companyEntity);
    }


    @Override
    public List<CompanyEntity> findAll() {
        return companyJpaRepository.findAll();
    }

    @Override
    public Optional<CompanyEntity> findById(Long companyId) {
        return companyJpaRepository.findById(companyId);
    }

    @Override
    public void deleteById(Long companyId) {
        companyJpaRepository.deleteById(companyId);
    }

    @Override
    public Optional<CompanyEntity> findByEmail(String email) {
        return companyJpaRepository.findByEmail(email);
    }
}
