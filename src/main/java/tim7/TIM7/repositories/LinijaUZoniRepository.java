package tim7.TIM7.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.LinijaUZoni;
import tim7.TIM7.model.Zona;

public interface LinijaUZoniRepository extends JpaRepository<LinijaUZoni, Long>{
	
	public List<LinijaUZoni> findByLinijaAndObrisanFalse(Linija line);
	
	public List<LinijaUZoni> findByZonaAndObrisanFalse(Zona zone);


}
