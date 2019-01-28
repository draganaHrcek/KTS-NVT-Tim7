package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.UpdatedZonaDTO;
import tim7.TIM7.dto.ZonaDTO;
import tim7.TIM7.helper.SortCenovniciByDate;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.LinijaUZoni;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.LinijaUZoniRepository;
import tim7.TIM7.repositories.ZonaRepository;

@Service
public class ZonaService {

	@Autowired
	ZonaRepository zonaRepository;
	
	@Autowired
	LinijaRepository linijaRepository;
	
	@Autowired 
	CenovnikRepository cjenovnikRepository;
	
	@Autowired 
	LinijaUZoniRepository luzRepository;

	
	public Zona findOne(Long id) {
		try {
			Zona zone = zonaRepository.findById(id).get();
			return zone;
		}catch(Exception e) {
			return null;
		}
	}

	public List<Zona> findAll() {
		return zonaRepository.findAll();
	}
	public Zona findByName(String name) {
		return zonaRepository.findByNaziv( name);
	}

	public Zona save(Zona zona) {
		return zonaRepository.save(zona);
	}
	
	public Linija getLineById(Long id) {
		try {
			Linija line = linijaRepository.findById(id).get();
			return line;
		}catch(Exception e) {
			return null;
		}
	}
	
	public boolean addNewZone(UpdatedZonaDTO newZone) {
		Zona potential = findOne(newZone.getId());
		if(potential!=null) {
			return false;
		}
		potential = new Zona();
		potential.setNaziv(newZone.getName());
		potential.setObrisan(false);
		
		save(potential);
		
		
		
		for (LinijaDTO linDTO : newZone.getLines()) {
			try {
				Linija line = linijaRepository.findById(linDTO.getId()).get();
				LinijaUZoni luz = new LinijaUZoni();
				luz.setLinija(line);
				luz.setZona(potential);
				luzRepository.save(luz);
			}catch(Exception e) {
				return false;
			}
		}
		return true;
	}
	
	public boolean updateZone(UpdatedZonaDTO updatedZone) {
		Zona potential = findOne(updatedZone.getId());
		if(potential==null) {
			return false;
		}
		
		potential.setNaziv(updatedZone.getName());
		save(potential);
		
		List<LinijaUZoni> before = luzRepository.findByZonaAndObrisanFalse(potential);
		List<LinijaDTO> current = new ArrayList<LinijaDTO>();
		for(LinijaUZoni luz : before) {
			LinijaDTO lin = new LinijaDTO(luz.getLinija());
			if(updatedZone.getLines().contains(lin)) {
				current.add(lin);
			}else {
				luz.setObrisan(true);
				luzRepository.save(luz);
			}
		}
		for(LinijaDTO linDTO : updatedZone.getLines()) {
			if(current.contains(linDTO)) {
				continue;
			}else {
				LinijaUZoni luz = new LinijaUZoni();
				luz.setLinija(linijaRepository.findById(linDTO.getId()).get());
				luz.setZona(potential);
				luzRepository.save(luz);
			}
		}
		

		return true;
	}
	
	//kad moze da se obrise zona?
	public boolean deleteZone(Long id) {
		Zona potential = findOne(id);
		if(potential==null) {
			return false;
		}	
		
		
		Cenovnik current = getTrenutni();
		for(StavkaCenovnika sc : current.getStavke()) {
			if(sc.getStavka().getZona().getId()==id) {
				return false;
			}
		}
		
		potential.setObrisan(true);
		save(potential);
		
		for(LinijaUZoni luz : potential.getLinije()) {
			if(luz.isObrisan()) {
				continue;
			}
			luz.setObrisan(true);
			luzRepository.save(luz);
		}
		return true;
	}
	
	public List<ZonaDTO> getAllZones(){
		List<Zona> allZones = findAll();
		if(allZones==null) {
			return null;
		}
		
		List<ZonaDTO> retValue = new ArrayList<ZonaDTO>();
		for(Zona zone : allZones) {
			if(zone.isObrisan()) {
				continue;
			}
			ZonaDTO zdto = new ZonaDTO(zone);
			retValue.add(zdto);
		}
		return retValue;
	}
	
	
	
	//za provjere dozvole brisanja 
	public Cenovnik getTrenutni() {
		deleteIstekli();
		try{
			ArrayList<Cenovnik> cenovnici = (ArrayList<Cenovnik>) cjenovnikRepository.findAllByObrisanFalse();
			Collections.sort(cenovnici, new SortCenovniciByDate());
			return cenovnici.get(0);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public void deleteIstekli(){
		ArrayList<Cenovnik> cenovnici = (ArrayList<Cenovnik>) cjenovnikRepository.findAllByObrisanFalse();
		Collections.sort(cenovnici, new SortCenovniciByDate());
		Date now = Calendar.getInstance().getTime();
		for(int i = 0; i< cenovnici.size(); i++){
			if(i!= cenovnici.size()-1 &&
					cenovnici.get(i).getDatumObjavljivanja().before(now) &&
					cenovnici.get(i+1).getDatumObjavljivanja().before(now)){
				cenovnici.get(i).setObrisan(true);
				cjenovnikRepository.save(cenovnici.get(i));
			}
			else{
				break;
			}
		}
	}
}
