package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.VoziloDTO;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.VoziloRepository;

@Service
public class VoziloService {
	
	@Autowired
	VoziloRepository voziloRepository;
	
	@Autowired
	LinijaRepository linijaRepository;
	
	public Vozilo findOne(Long id) {
		try {
			Vozilo vehicle = voziloRepository.findById(id).get();
			return vehicle;
		}catch(Exception e) {
			return null;
		}
	}
	
	public List<Vozilo> findAll(){
		return voziloRepository.findAll();
	}
	
	public Vozilo save(Vozilo vehicle) {
		return voziloRepository.save(vehicle);
	}

	public boolean addNewVehicle(VoziloDTO newVehicle) {
		Vozilo potential = findOne(newVehicle.getId());
		if(potential!=null) {
			return false;
		}
		
		potential = new Vozilo();
		potential.setObrisan(false);
		potential.setRegistracija(newVehicle.getRegistration());
		potential.setTipVozila(newVehicle.getType());
		potential.setLinija(null);
		save(potential);
		return true;
	}
	
	public boolean updateVehicle(VoziloDTO updatedVehicle) {
		Vozilo potential = findOne(updatedVehicle.getId());
		if(potential==null) {
			return false;
		}
		potential.setRegistracija(updatedVehicle.getRegistration());
		save(potential);
		return true;
	}
	
	public boolean deleteVehicle(Long id) {
		Vozilo potential = findOne(id);
		if(potential==null || potential.getLinija()!=null) {
			return false;
		}
		potential.setObrisan(true);
		save(potential);
		return true;
	}
	
	public List<VoziloDTO> getAllVehicles(){
		List<Vozilo> allVehicles = findAll();
		if(allVehicles==null || allVehicles.isEmpty()) {
			return null;
		}
		
		List<VoziloDTO> retValue = new ArrayList<VoziloDTO>();
		for(Vozilo vehicle : allVehicles) {
			if(vehicle.isObrisan()) {
				continue;
			}
			VoziloDTO vehicleDTO = new VoziloDTO(vehicle);
			retValue.add(vehicleDTO);
		}
		
		if(retValue.isEmpty()) {
			return null;
		}
		return retValue;
	}
	
	public boolean addVehicleToLine(Long lineId, Long vehicleId) {
		Vozilo vehicle = findOne(vehicleId);
		if(vehicle.getLinija()!=null) {
			return false;
		}
		Linija line = null;
		try {
			line = linijaRepository.findById(lineId).get();
		}catch(Exception e) {
			return false;
		}
		
		List<Vozilo> vehicles = line.getVozila();
		vehicle.setLinija(line);
		vehicles.add(vehicle);
		line.setVozila(vehicles);
		save(vehicle);
		linijaRepository.save(line);
		
		return true;
	}
	
	public boolean removeVehicleFromLine(Long id) {
		Vozilo vehicle = findOne(id);
		if(vehicle.getLinija()==null) {
			return false;
		}
		
		Linija line = null;
		try {
			line = linijaRepository.findById(vehicle.getLinija().getId()).get();
		}catch(Exception e) {
			return false;
		}
		vehicle.setLinija(null);
		
		List<Vozilo> vehicles = line.getVozila();
		vehicles.remove(vehicle);
		line.setVozila(vehicles);
		
		save(vehicle);
		linijaRepository.save(line);
		return true;
	}
}
