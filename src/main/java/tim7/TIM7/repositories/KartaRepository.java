package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Karta;
@Repository
public interface KartaRepository extends JpaRepository<Karta, Long> {
	public Karta findByKod(String kod);
}
