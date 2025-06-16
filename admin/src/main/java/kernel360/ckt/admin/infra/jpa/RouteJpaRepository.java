package kernel360.ckt.admin.infra.jpa;

import kernel360.ckt.core.domain.entity.RouteEntity;
import kernel360.ckt.admin.infra.RouteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Long>, RouteRepository {

}
