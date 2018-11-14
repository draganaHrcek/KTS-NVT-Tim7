package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Osoba;
@Repository
public interface OsobaRepository extends JpaRepository<Osoba, Long> {
	
	Osoba findByKorIme(String korIme);

}
