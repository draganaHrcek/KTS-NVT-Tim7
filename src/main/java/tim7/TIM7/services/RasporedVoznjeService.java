package tim7.TIM7.services;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.RasporedVoznjeDTO;
import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;
import tim7.TIM7.repositories.RasporedVoznjeRepository;

@Service
public class RasporedVoznjeService {

	@Autowired
	RasporedVoznjeRepository rasporedVoznjeRepository;
	
	public RasporedVoznje findById(Long id){
		return rasporedVoznjeRepository.findById(id).get();
	}
	
	public List<RasporedVoznje> findAll(){
		return rasporedVoznjeRepository.findAll();
	}
	
	public RasporedVoznje save(RasporedVoznje rv){
		return rasporedVoznjeRepository.save(rv);
	}
	
	public void delete(Long id){
		RasporedVoznje rasporedVoznje = findById(id);
		rasporedVoznje.setObrisan(true);
		save(rasporedVoznje);
	}
	
	/*
	public RasporedVoznjeDTO createRasporedVoznje(RedVoznje redVoznje, DanUNedelji danUNedelji, List<LocalTime> vremena, Linija linija){
		return new RasporedVoznjeDTO();
	}*/
}
