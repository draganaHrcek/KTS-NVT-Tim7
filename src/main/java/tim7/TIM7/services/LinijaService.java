package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.StanicaDTO;
import tim7.TIM7.dto.UpdatedLinijaDTO;
import tim7.TIM7.dto.VoziloDTO;
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

	@Autowired
	VoziloRepository voziloRepository;
	
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
		if(linija==null || linija.isObrisan()) {
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
	public boolean addNewLine(UpdatedLinijaDTO newLine) {
		Linija potential = findOne(newLine.getId());
		if(potential!=null) {
			return false;
		}
		potential = new Linija();
		potential.setNaziv(newLine.getName());
		potential.setObrisan(false);
		potential.setZone(getZonesFromDTO(newLine.getZones()));
		potential.setVozila(getVehiclesFromDTO(newLine.getVehicles()));
		potential.setStanice(getStationsFromDTO(newLine.getStations()));
		save(potential);
		saveZonesAndStationsAndVehicles(potential);
		return true;
	}
	
	public boolean updateLine(UpdatedLinijaDTO updatedLine){
		Linija potential = findOne(updatedLine.getId());
		if(potential==null || potential.isObrisan()) {
			return false;
		}
		potential.setNaziv(updatedLine.getName());
		
//		potential.setZone(getZones(updatedLine));
//		potential.setVozila((getVehicles(updatedLine));
//		potential.setStanice(getStation(updatedLine));
		
		
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
			List<Linija> linije = zone.getLinije();
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
	
	public List<Vozilo> getVehiclesFromDTO(List<VoziloDTO> vehicles){
		List<Vozilo> retValue = new ArrayList<Vozilo>();
		for(VoziloDTO vehicleDTO : vehicles) {
			Vozilo vehicle = voziloRepository.findById(vehicleDTO.getId()).get();
			retValue.add(vehicle);
		}
		return retValue;
	}
	
//	public List<Zona> getZones(UpdatedLinijaDTO updatedLine) {
//
//	}
//	
//	public List<Vozilo> getVehicles(UpdatedLinijaDTO updatedLine){
//		
//	}
//	
//	public List<Stanica> getStations(UpdatedLinijaDTO updatedLine){
//		
//	}
	
	public boolean saveZonesAndStationsAndVehicles(Linija line) {
		for(Zona zone : line.getZone()) {
			List<Linija> linije = zone.getLinije();
			linije.add(line);
			zone.setLinije(linije);
			zonaRepository.save(zone);
		}
		for(Stanica station : line.getStanice()) {
			List<Linija> linije = station.getLinije();
			linije.add(line);
			station.setLinije(linije);
			stanicaRepository.save(station);
		}
		for(Vozilo vehicle : line.getVozila()) {
			Linija linija = vehicle.getLinija();
			vehicle.setLinija(linija);
			voziloRepository.save(vehicle);
		}
		return true;
	}
	
}
