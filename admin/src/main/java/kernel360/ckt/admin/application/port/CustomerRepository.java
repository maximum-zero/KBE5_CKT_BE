package kernel360.ckt.admin.application.port;

import kernel360.ckt.core.domain.entity.CustomerEntity;
import kernel360.ckt.core.domain.enums.CustomerStatus;
import kernel360.ckt.core.domain.enums.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 고객 도메인에 대한 저장소 역할을 하는 포트 인터페이스입니다.
 * 인프라 계층(JPA, JDBC 등)에서 해당 인터페이스를 구현하여 실제 데이터 액세스를 수행합니다.
 */
public interface CustomerRepository {

    /**
     * 고객 정보를 저장하거나 수정합니다.
     *
     * @param customerEntity 저장할 고객 엔티티
     * @return 저장된 고객 엔티티
     */
    CustomerEntity save(CustomerEntity customerEntity);

    /**
     * 고객 상태 및 키워드 기반으로 고객 목록을 페이징 조회합니다.
     *
     * @param status 필터링할 고객 상태
     * @param keyword 검색 키워드 (이름 또는 전화번호)
     * @param pageable 페이징 정보
     * @return 조건에 맞는 고객 목록
     */
    Page<CustomerEntity> findAll(CustomerStatus status, String keyword, Pageable pageable);

    /**
     * 고객 ID로 고객을 조회합니다.
     *
     * @param id 조회할 고객 ID
     * @return 존재할 경우 고객 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<CustomerEntity> findById(Long id);

    /**
     * 운전면허번호로 고객을 조회합니다.
     *
     * @param licenseNumber 운전면허번호
     * @return 존재할 경우 고객 엔티티, 존재하지 않을 경우 빈 Optional 객체
     */
    Optional<CustomerEntity> findByLicenseNumber(String licenseNumber);

    /**
     * 전체 고객 수를 조회합니다.
     *
     * @return 전체 고객 수
     */
    long countTotal();

    /**
     * 고객 유형별(INIDIVIDUAL 또는 CORPORATE) 고객 수를 조회합니다.
     *
     * @param type 고객 유형 ("INDIVIDUAL" 또는 "CORPORATE")
     * @return 해당 유형의 고객 수
     */
    long countByType(CustomerType type);

    /**
     * Keyword에 맞는 고객을 조회합니다.
     * @param customerNameKeyword
     * @param phoneNumberKeyword
     * @return
     */
    List<CustomerEntity> findByCustomerNameContainingOrPhoneNumberContaining(String customerNameKeyword, String phoneNumberKeyword);

    /**
     * ID 및 삭제 여부(deleteYn)를 기준으로 고객을 조회합니다.
     *
     * @param id        고객 ID
     * @param deleteYn  삭제 여부 ('N' 또는 'Y')
     * @return 조건에 맞는 고객 정보
     */
    Optional<CustomerEntity> findByIdAndDeleteYn(Long id, String deleteYn);

    /**
     * 고객 상태 및 삭제 여부(deleteYn)를 기준으로 페이징된 고객 목록을 조회합니다.
     *
     * @param status     고객 상태
     * @param deleteYn   삭제 여부 ('N' 또는 'Y')
     * @param pageable   페이징 정보
     * @return 조건에 맞는 고객 페이지
     */
    Page<CustomerEntity> findAllByStatusAndDeleteYn(CustomerStatus status, String deleteYn, Pageable pageable);
}
