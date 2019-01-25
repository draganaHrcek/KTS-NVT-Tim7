package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.StanicaDTO;
import tim7.TIM7.dto.ZonaDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.StanicaRepository;
import tim7.TIM7.repositories.VoziloRepository;
import tim7.TIM7.repositories.ZonaRepository;

@Service
public class LinijaService {

	@Autowired
	LinijaRepository linijaRepository;

	@Autowired
	CenovnikRepository cenovnikRepository;
	
	@Autowired
	ZonaRepository zonaRepository;
	
	@Autowired
	StanicaRepository stanicaRepository;

	public Linija findOne(Long id) {
		try {
			Linija line = linijaRepository.findById(id).get();
			return line;
		}catch(Exception e) {
			return null;
		}
	}

	public List<Linija> findAll() {
		return linijaRepository.findAll();
	}
	public Linija findByName(String name) {
		return linijaRepository.findByNaziv( name);
	}

	public Linija save(Linija linija) {
		return linijaRepository.save(linija);
	}

	//check if exists; if it is currently in cjenovnik; if it is currently in redvoznje
	public Linija deleteOneLine(Long id) {
		Linija linija=findOne(id);
		if(linija==null) {
			return null;
		}
		Cenovnik current = getTrenutniCenovnik();
		
		if (current!=null) {
			for(StavkaCenovnika item : current.getStavke()) {
				if (item.getStavka().getLinija().getId()==id) {
					return null;
				}
			}
		}
		
		linija.setObrisan(true);
		Linija deletedLine = save(linija);
		return deletedLine;
	}
	
	//checks if already exists
	public boolean addNewLine(LinijaDTO newLine) {
		Linija potential = findOne(newLine.getId());
		if(potential!=null) {
			return false;
		}
		potential = new Linija();
		potential.setId(newLine.getId());
		potential.setNaziv(newLine.getName());
		potential.setObrisan(false);
		potential.setZone(getZonesFromDTO(newLine.getZones()));
		potential.setStanice(getStationsFromDTO(newLine.getStations()));
		save(potential);
		return true;
	}
	
	public boolean updateLine(LinijaDTO updatedLine){
		Linija potential = findOne(updatedLine.getId());
		if(potential==null) {
			return false;
		}
		potential.setNaziv(updatedLine.getName());
		save(potential);
		return true;
	}
	
	public List<LinijaDTO> getAllLines(){
		List<Linija> allLines = findAll();
		if (allLines==null) {
			return null;
		}
		List<LinijaDTO> retValue = new ArrayList<LinijaDTO>();
		for (Linija lin : allLines) {
			if (lin.isObrisan()) {
				continue;
			}
			LinijaDTO linDTO = new LinijaDTO(lin);
			retValue.add(linDTO);
		}
		if(retValue.isEmpty()) {
			return null;
		}
		return retValue;
	}
	
	public List<LinijaDTO> getLinesFromOneZone(Long id){
		List<Linija> allLines = findAll();
		if (allLines==null) {
			return null;
		}
		List<LinijaDTO> retValue = new ArrayList<LinijaDTO>();
		for (Linija lin : allLines) {
			if (lin.isObrisan()) {
				continue;
			}
			for(Zona zone : lin.getZone()) {
				if(zone.getId()==id) {
					LinijaDTO linDTO = new LinijaDTO(lin);
					retValue.add(linDTO);
					break;
				}
			}
		}
		if(retValue.isEmpty()) {
			return null;
		}
		return retValue;		
	}
	
	public Cenovnik getTrenutniCenovnik() {
		Calendar now = Calendar.getInstance();
		for(Cenovnik cenovnik : cenovnikRepository.findAll()){
			if(cenovnik.getDatumObjavljivanja().before(now.getTime()) &&
					cenovnik.getDatumIsteka().after(now.getTime())){
				return cenovnik;
			}
		}
		return null;
	}
	
	public List<Zona> getZonesFromDTO(List<ZonaDTO> list){
		List<Zona> retValue = new ArrayList<Zona>();
		for(ZonaDTO zoneDTO : list) {
			Zona zone = zonaRepository.findById(zoneDTO.getId()).get();
			retValue.add(zone);
		}
		return retValue;
	}
	
	public List<Stanica> getStationsFromDTO(List<StanicaDTO> stations){
		List<Stanica> retValue = new ArrayList<Stanica>();
		for(StanicaDTO stationDTO : stations) {
			Stanica station = stanicaRepository.findById(stationDTO.getId()).get();
			retValue.add(station);
		}
		return retValue;
	}
	
	
}
