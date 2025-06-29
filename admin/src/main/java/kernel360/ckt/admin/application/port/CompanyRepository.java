package kernel360.ckt.admin.application.port;

import kernel360.ckt.core.domain.entity.CompanyEntity;

import java.util.List;
import java.util.Optional;

/**
 * 회사 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface CompanyRepository {

    /**
     * 회사를 저장하거나 수정합니다.
     *
     * @param companyEntity 저장할 회사 엔티티
     * @return 저장된 회사 엔티티
     */
    CompanyEntity save(CompanyEntity companyEntity);

    /**
     * 모든 회사를 조회합니다.
     *
     * @return 전체 회사 엔티티 목록
     */
    List<CompanyEntity> findAll();

    /**
     * 회사 ID로 회사를 조회합니다.
     *
     * @param companyId 조회할 회사 ID
     * @return 존재할 경우 회사 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<CompanyEntity> findById(Long companyId);

    /**
     * 회사 ID로 회사를 삭제합니다.
     *
     * @param companyId 삭제할 회사 ID
     */
    void deleteById(Long companyId);

    /**
     * 회사 email 로 회사를 조회합니다.
     * @param email 조회할 회사 ID
     * @return 존재할 경우 회사 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<CompanyEntity> findByEmail(String email);
}
