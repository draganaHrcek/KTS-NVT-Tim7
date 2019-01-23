package tim7.TIM7.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;

@Repository
public interface RasporedVoznjeRepository extends JpaRepository<RasporedVoznje, Long>{
	public RasporedVoznje findByDanUNedeljiAndLinijaAndRedVoznjeAndObrisanFalse(DanUNedelji danUNedelji, Linija linija, RedVoznje redVoznje);
	public List<RasporedVoznje> findByRedVoznjeAndObrisanFalse(RedVoznje redVoznje);
}
