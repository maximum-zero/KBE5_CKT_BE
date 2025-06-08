package kernel360.ckt.collector.infra.repository.jpa;

import kernel360.ckt.collector.application.repository.RentalRepository;
import kernel360.ckt.core.domain.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, Long>, RentalRepository {
}
