package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.StanicaDTO;
import tim7.TIM7.dto.VoziloDTO;
import tim7.TIM7.dto.ZonaDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.LinijaUZoni;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.StanicaULiniji;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.LinijaUZoniRepository;
import tim7.TIM7.repositories.StanicaRepository;
import tim7.TIM7.repositories.StanicaULinijiRepository;
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
	
	@Autowired
	LinijaUZoniRepository luzRepository;
	
	@Autowired
	StanicaULinijiRepository sulRepository;
	
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
		
		for(StanicaULiniji sul : linija.getStanice()) {
			if(sul.isObrisan()) {
				continue;
			}
			sul.setObrisan(true);
			sulRepository.save(sul);
		}
		
		for(LinijaUZoni luz : linija.getZone()) {
			if(luz.isObrisan()) {
				continue;
			}
			luz.setObrisan(true);
			luzRepository.save(luz);
		}
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
		potential.setNaziv(newLine.getName());
		potential.setObrisan(false);
		
		save(potential);

		//dodaj vozila
		for(VoziloDTO vehicleDTO : newLine.getVehicles()) {
			try {
				Vozilo vehicle = voziloRepository.findById(vehicleDTO.getId()).get();
				potential.getVozila().add(vehicle);
			}catch(Exception e) {
				continue;
			}			
		}
		
		//dodaj stanice
		for(StanicaDTO stationDTO : newLine.getStations()) {
			Stanica station=null;
			try {
				station = stanicaRepository.findById(stationDTO.getId()).get();

			}catch(Exception e) {
				station = new Stanica();
				station.setObrisan(false);
				station.setOznaka(stationDTO.getName());
				station.setLongituda(stationDTO.getLongitude());
				station.setLatituda(stationDTO.getLatitude());

			}finally {
				StanicaULiniji sul = new StanicaULiniji();
				sul.setLinija(potential);
				sul.setStanica(station);
				sulRepository.save(sul);	
			}
		}
		
		//dodaj zone
		for(ZonaDTO zoneDTO : newLine.getZones()) {
			Zona zone=null;
			try {
				zone = zonaRepository.findById(zoneDTO.getId()).get();

			}catch(Exception e) {
				zone = new Zona();
				zone.setObrisan(false);
				zone.setNaziv(zoneDTO.getName());

			}finally {
				LinijaUZoni luz = new LinijaUZoni();
				luz.setLinija(potential);
				luz.setZona(zone);
				luzRepository.save(luz);	
			}	
		}
		return true;
	}
	
	public boolean updateLine(LinijaDTO updatedLine){
		Linija potential = findOne(updatedLine.getId());
		if(potential==null || potential.isObrisan()) {
			return false;
		}
		potential.setNaziv(updatedLine.getName());

		//vozila
		List<Vozilo> vehicles = new ArrayList<Vozilo>();
		for(VoziloDTO vehicleDTO : updatedLine.getVehicles()) {
			try {
				Vozilo vehicle = voziloRepository.findById(vehicleDTO.getId()).get();
				vehicles.add(vehicle);
			}catch(Exception e) {
				continue;
			}
		}
		potential.setVozila(vehicles);
		
		save(potential);
		
		//zone
		List<LinijaUZoni> before = luzRepository.findByLinijaAndObrisanFalse(potential);
		List<ZonaDTO> currentZones = new ArrayList<ZonaDTO>();
		for(LinijaUZoni luz : before) {
			ZonaDTO tempZone = new ZonaDTO(luz.getZona());
			if(!updatedLine.getZones().contains(tempZone)) {
				luz.setObrisan(true);
				luzRepository.save(luz);
			}else {
				currentZones.add(tempZone);
			}
		}
		for(ZonaDTO zoneDTO : updatedLine.getZones()) {
			if(currentZones.contains(zoneDTO)) {
				continue;
			}else {
				LinijaUZoni luz = new LinijaUZoni();
				luz.setLinija(potential);
				luz.setZona(zonaRepository.findById(zoneDTO.getId()).get());
				luzRepository.save(luz);
			}
		}
		//stanice
		List<StanicaULiniji> stationsBefore = sulRepository.findByLinijaAndObrisanFalse(potential);
		List<StanicaDTO> currentStations = new ArrayList<StanicaDTO>();
		for(StanicaULiniji sul : stationsBefore) {
			StanicaDTO tempStation = new StanicaDTO(sul.getStanica());
			if(!updatedLine.getStations().contains(tempStation)) {
				sul.setObrisan(true);
				sulRepository.save(sul);
			}else {
				currentStations.add(tempStation);
			}
		}
		for(StanicaDTO stationDTO : updatedLine.getStations()) {
			if(currentStations.contains(stationDTO)) {
				continue;
			}else {
				StanicaULiniji sul = new StanicaULiniji();
				sul.setLinija(potential);
				sul.setStanica(stanicaRepository.findById(stationDTO.getId()).get());
				sulRepository.save(sul);
			}
		}
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
		Zona zone = null;
		try{
			zone = zonaRepository.findById(id).get();
		}catch(Exception e) {
			return null;
		}
		List<LinijaUZoni> all = luzRepository.findByZonaAndObrisanFalse(zone);
		List<LinijaDTO> retValue = new ArrayList<LinijaDTO>();
		for(LinijaUZoni zul : all) {
			LinijaDTO lin = new LinijaDTO(zul.getLinija());
			retValue.add(lin);
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
	/*
	public List<ZonaDTO> getZonesOfLine(Linija line) {
		List<ZonaDTO> retValue = new ArrayList<ZonaDTO>();
		for(LinijeJedneZone ljz : ljzRepository.findAll()) {
			if(ljz.getLinije().contains(line) && !ljz.getZona().isObrisan()) {
				retValue.add(new ZonaDTO(ljz.getZona()));
			}
		}
		return retValue;
	}
	
	//dobavlja zone iz baze na osnovu zonaDTO objekata; vraca listu
	//parametri: newLine - dto objekat iz kojeg izvlacimo podatke, currentLine - objekat linije u koji dodajemo podatke
	public List<Zona> getZonesFromDTO(UpdatedLinijaDTO newLine, Linija currentLine){
		List<Zona> retValue = new ArrayList<Zona>();
		
		for(ZonaDTO zoneDTO : newLine.getZones()) {
			Zona zone = zonaRepository.findById(zoneDTO.getId()).get();
			retValue.add(zone);
			
			//dodaje se trenutna linija u svaku zonu
			List<Linija> linesOfThisZone = zone.getLinije();
			linesOfThisZone.add(currentLine);
			zone.setLinije(linesOfThisZone);
			zonaRepository.save(zone);
		}
		return retValue;
	}
	
	public List<Stanica> getStationsFromDTO(UpdatedLinijaDTO newLine){
		List<Stanica> retValue = new ArrayList<Stanica>();
		for(StanicaDTO stationDTO : newLine.getStations()) {
			Stanica station = stanicaRepository.findById(stationDTO.getId()).get();
			retValue.add(station);
		}
		return retValue;
	}
	*/
}