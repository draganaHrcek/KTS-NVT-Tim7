package tim7.TIM7.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.StanicaULiniji;

public interface StanicaULinijiRepository extends JpaRepository<StanicaULiniji, Long>{

	public List<StanicaULiniji> findByLinijaAndObrisanFalse(Linija line);
	
	public List<StanicaULiniji> findByStanicaAndObrisanFalse(Stanica station);
}
