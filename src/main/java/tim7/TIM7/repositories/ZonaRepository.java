package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Zona;
@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {

}
