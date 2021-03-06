package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Stanica;
@Repository
public interface StanicaRepository extends JpaRepository<Stanica, Long> {
	public Stanica findByOznaka(String oznaka);
}
