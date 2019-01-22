package tim7.TIM7.services;

import java.time.LocalTime;
import java.util.ArrayList;
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
public class RasporedVoznjeService {

	@Autowired
	RasporedVoznjeRepository rasporedVoznjeRepository;
	
	@Autowired
	RedVoznjeRepository redVoznjeRepository;
	
	@Autowired
	LinijaRepository linijaRepository;
	
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
	
	
	//za dobijanje zeljenog rasporeda
	public RasporedVoznjeDTO getSpecificRasporedVoznje(DanUNedelji danUNedelji, String nazivLinije, RedVoznjeDTO redVoznjeDto){
		if (redVoznjeDto==null){
			return null;
		}
		Long idRedaVoznje=redVoznjeDto.getId();
		RedVoznje redVoznje = redVoznjeRepository.findById(idRedaVoznje).get();
		Linija linija = linijaRepository.findByNaziv(nazivLinije);
		RasporedVoznje rasporedVoznje = rasporedVoznjeRepository.findByDanUNedeljiAndLinijaAndRedVoznjeAndObrisanFalse(danUNedelji, linija, redVoznje);
		if (rasporedVoznje==null){
			return null;
		}else{
			return new RasporedVoznjeDTO(rasporedVoznje);
		}
		
	}

	//za kreiranje novog rasporeda voznje u buducem redu voznje
	public String createRasporedVoznje(RasporedVoznjeDTO rasporedVoznjeDto, RedVoznjeDTO buduciRedVoznje){
		if (buduciRedVoznje==null){
			return "NE POSTOJI";
		}else{
			RedVoznje buduci=redVoznjeRepository.findById(buduciRedVoznje.getId()).get();
			Linija linija = linijaRepository.findByNaziv(rasporedVoznjeDto.getNazivLinije());
			RasporedVoznje noviRaspored=rasporedVoznjeRepository.findByDanUNedeljiAndLinijaAndRedVoznjeAndObrisanFalse(rasporedVoznjeDto.getDanUNedelji(), linija, buduci);
			if (noviRaspored==null){
				noviRaspored=new RasporedVoznje(rasporedVoznjeDto.getDanUNedelji(), rasporedVoznjeDto.getVremena(), linija, buduci, false);
				save(noviRaspored);
				return "KREIRAN";
			}else{
				return "VEC POSTOJI";
			}
		}
	}
	
	
	//za brisanje rasporeda voznje
	public String deleteRasporedVoznje(Long id, RedVoznjeDTO buduciRedVoznje){
		if (buduciRedVoznje==null){
			return "NE POSTOJI";
		}else{
			RasporedVoznje zaBrisanje=rasporedVoznjeRepository.findById(id).get();
			zaBrisanje.setObrisan(true);
			rasporedVoznjeRepository.save(zaBrisanje);
			return "OBRISAN";
		}
	}
	
	
	//za izmenu vremena rasporeda voznje
	public String changeRasporedVoznje(Long id, List<LocalTime> vremena, RedVoznjeDTO buduciRedVoznje){
		if (buduciRedVoznje==null){
			return "NE POSTOJI";
		}else{
			RasporedVoznje zaIzmenu=rasporedVoznjeRepository.findById(id).get();
			zaIzmenu.setVremena(vremena);
			rasporedVoznjeRepository.save(zaIzmenu);
			return "IZMENJEN";
		}
	}
	
	//za izlistavanje svih neobrisanih rasporeda voznje unutar reda voznje
	public List<RasporedVoznjeDTO> getNeobrisaniRasporedi(RedVoznjeDTO redVoznje){
		if (redVoznje==null){
			return null;
		}else{
			RedVoznje red=redVoznjeRepository.findById(redVoznje.getId()).get();
			List<RasporedVoznje> rasporediVoznje=rasporedVoznjeRepository.findByRedVoznjeAndObrisanFalse(red);
			List<RasporedVoznjeDTO> neobrisani=new ArrayList<RasporedVoznjeDTO>();
			if (rasporediVoznje.size()==0){
				return null;
			}else{
				for (RasporedVoznje rv:rasporediVoznje){
					neobrisani.add(new RasporedVoznjeDTO(rv));
				}
				return neobrisani;
			}
			
			
		}
	}
}
