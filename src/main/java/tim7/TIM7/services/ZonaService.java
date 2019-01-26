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
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.ZonaRepository;

@Service
public class ZonaService {

	@Autowired
	ZonaRepository zonaRepository;
	
	@Autowired
	LinijaRepository linijaRepository;
	
	@Autowired 
	CenovnikRepository cjenovnikRepository;

	
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
		List<Linija> lines = new ArrayList<Linija>();
		potential.setLinije(lines);
		potential.setObrisan(false);
		save(potential);
		for(LinijaDTO line : newZone.getLines()) {
			Linija linija = getLineById(line.getId());
			if(linija==null || linija.isObrisan()) {
				return false;
			}
			List<Zona> zones = linija.getZone();
			zones.add(potential);
			linija.setZone(zones);
			lines.add(linija);
			linijaRepository.save(linija);
		}
		return true;
	}
	
	public boolean updateZone(UpdatedZonaDTO updatedZone) {
		Zona potential = findOne(updatedZone.getId());
		if(potential==null) {
			return false;
		}
		
		potential.setNaziv(updatedZone.getName());
		
		List<Linija> lines = new ArrayList<Linija>();
		for(LinijaDTO line : updatedZone.getLines()) {
			Linija linija = getLineById(line.getId());
			if(linija==null || linija.isObrisan()) {
				return true;
			}
			lines.add(linija);
			List<Zona> zones = linija.getZone();
			if(!zones.contains(potential)) {
				zones.add(potential);
				linija.setZone(zones);
				linijaRepository.save(linija);
			}
		}
		
		for(Long lineId : updatedZone.getRemovedLinesIds()) {
			Linija linija = getLineById(lineId);
			if(linija==null || linija.isObrisan()) {
				return true;
			}
			List<Zona> zones = linija.getZone();
			zones.remove(potential);
			linija.setZone(zones);
			linijaRepository.save(linija);
		}
		potential.setLinije(lines);
		save(potential);
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
