package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Vozilo;
@Repository
public interface VoziloRepository extends JpaRepository<Vozilo, Long> {

}
