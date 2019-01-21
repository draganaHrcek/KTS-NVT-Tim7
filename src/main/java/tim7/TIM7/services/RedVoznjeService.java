package tim7.TIM7.services;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.RasporedVoznjeDTO;
import tim7.TIM7.dto.RedVoznjeDTO;
import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.RasporedVoznjeRepository;
import tim7.TIM7.repositories.RedVoznjeRepository;

@Service
public class RedVoznjeService {
	
	@Autowired 
	RedVoznjeRepository redVoznjeRepository;
	
	@Autowired
	RasporedVoznjeRepository rasporedVoznjeRepository;
	
	@Autowired
	LinijaRepository linijaRepository;
	
	public RedVoznje findById(Long id){
		return redVoznjeRepository.findById(id).get();
	}
	
	public List<RedVoznje> findAll(){
		return redVoznjeRepository.findAll();
	}
	
	public RedVoznje save(RedVoznje redVoznje){
		return redVoznjeRepository.save(redVoznje);
	}
	
	
	public void delete(Long id){
		RedVoznje redVoznje = findById(id);
		redVoznje.setObrisan(true);
		for (RasporedVoznje rv : redVoznje.getRasporediVoznje()){
			rv.setObrisan(true);
			rasporedVoznjeRepository.save(rv);
		}
		save(redVoznje);
	}
	
	
	//za postavljanje aktuelnog reda voznje
	public RedVoznjeDTO getTrenutniRedVoznje(){
		List<RedVoznje> aktivniRedoviVoznje=redVoznjeRepository.findByObrisanFalse();
		if (aktivniRedoviVoznje == null){
			return null;
		}else{
			Calendar now = Calendar.getInstance();
			if (aktivniRedoviVoznje.size()==1){
				if (now.getTime().after(aktivniRedoviVoznje.get(0).getDatumObjavljivanja())){					
					return new RedVoznjeDTO(aktivniRedoviVoznje.get(0));
				}else{
					return null;
				}
			}else{
				RedVoznje stari;
				RedVoznje novi;
				if (aktivniRedoviVoznje.get(0).getDatumObjavljivanja().before(aktivniRedoviVoznje.get(1).getDatumObjavljivanja())){
					stari=aktivniRedoviVoznje.get(0);
					novi=aktivniRedoviVoznje.get(1);
				}else{
					stari=aktivniRedoviVoznje.get(1);
					novi=aktivniRedoviVoznje.get(0);
				}
				if (now.getTime().after(novi.getDatumObjavljivanja())){
					stari.setObrisan(true);
					save(stari);
					return new RedVoznjeDTO(novi);
				}else{
					return new RedVoznjeDTO(stari);
				}
			}
		}
		
	}
	
	//ukoliko budem radila izmenu buduceg reda voznje
	public RedVoznjeDTO getBuduciRedVoznje() {
		RedVoznjeDTO trenutniRedVoznjeDto = getTrenutniRedVoznje();
		if (trenutniRedVoznjeDto==null){
			return null;
		}else{
			List<RedVoznje> aktivniRedoviVoznje=redVoznjeRepository.findByObrisanFalse();
			if (aktivniRedoviVoznje.size()==1){
				return null;
			}else{
				RedVoznje buduciRedVoznje=redVoznjeRepository.findByIdNot(trenutniRedVoznjeDto.getId());
				return new RedVoznjeDTO(buduciRedVoznje);
			}
		}
	}
	
	
	//za dobijanje zeljenog rasporeda
	public RasporedVoznjeDTO getSpecificRasporedVoznje(DanUNedelji danUNedelji, String nazivLinije){
		Long idTrenutnogRedaVoznje = getTrenutniRedVoznje().getId();
		RedVoznje redVoznje = findById(idTrenutnogRedaVoznje);
		Linija linija = linijaRepository.findByNaziv(nazivLinije);
		RasporedVoznje rasporedVoznje = rasporedVoznjeRepository.findByDanUNedeljiAndLinijaAndRedVoznje(danUNedelji, linija, redVoznje);
		if (rasporedVoznje==null){
			return null;
		}else{
			return new RasporedVoznjeDTO(rasporedVoznje);
		}
		
	}

	
}
