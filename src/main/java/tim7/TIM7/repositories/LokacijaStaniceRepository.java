package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.LokacijaStanice;
@Repository
public interface LokacijaStaniceRepository extends JpaRepository<LokacijaStanice, Long> {

}
