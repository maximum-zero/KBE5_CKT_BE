package kernel360.ckt.admin.infra.repository.jpa;

import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.core.repository.RouteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Long>, RouteRepository {

}
