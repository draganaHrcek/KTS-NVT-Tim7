package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Stavka;
@Repository
public interface StavkaRepository extends JpaRepository<Stavka, Long> {

}
