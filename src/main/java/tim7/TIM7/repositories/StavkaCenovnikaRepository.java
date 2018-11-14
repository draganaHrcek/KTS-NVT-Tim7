package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.StavkaCenovnika;
@Repository
public interface StavkaCenovnikaRepository extends JpaRepository<StavkaCenovnika, Long> {

}
