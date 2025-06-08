package kernel360.ckt.collector.infra.repository.jpa;

import kernel360.ckt.collector.application.repository.RouteRepository;
import kernel360.ckt.core.domain.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Long>, RouteRepository {
}
