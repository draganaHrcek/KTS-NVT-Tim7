package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.ZonaDTO;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.ZonaRepository;

@Service
public class ZonaService {

	@Autowired
	ZonaRepository zonaRepository;

	
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
	
	public boolean addNewZone(ZonaDTO newZone) {
		Zona potential = findOne(newZone.getId());
		if(potential!=null) {
			return false;
		}
		potential = new Zona();
		potential.setId(newZone.getId());
		potential.setNaziv(newZone.getName());
		potential.setObrisan(false);
		save(potential);
		return true;
	}
	
	public boolean updateZone(ZonaDTO updatedZone) {
		Zona potential = findOne(updatedZone.getId());
		if(potential==null) {
			return false;
		}
		
		potential.setNaziv(updatedZone.getName());
		save(potential);
		return true;
	}
	
	//kad moze da se obrise zona?
	public boolean deleteZone(Long id) {
		Zona potential = findOne(id);
		if(potential==null) {
			return false;
		}
		
		potential.setObrisan(true);
		save(potential);
		return true;
	}
	
	public List<ZonaDTO> getAllZones(){
		List<Zona> allZones = findAll();
		if(allZones==null || allZones.isEmpty()) {
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
		if(retValue.isEmpty()) {
			return null;
		}
		return retValue;
	}
}
