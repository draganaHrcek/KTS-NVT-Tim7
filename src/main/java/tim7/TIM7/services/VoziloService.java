package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.VoziloDTO;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.TipVozila;
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
	
	public Vozilo findByName(String name) {
		try {
			Vozilo vehicle = voziloRepository.findByRegistracija(name);
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
	
	public Linija getLineById(Long id) {
		try {
			Linija line = linijaRepository.findById(id).get();
			return line;
		}catch(Exception e) {
			return null;
		}
	}

	public boolean addNewVehicle(VoziloDTO newVehicle) {
		Vozilo potential = findOne(newVehicle.getId());
		if(potential!=null) {
			return false;
		}
		
		potential = findByName(newVehicle.getRegistration());
		if(potential!=null) {
			return false;
		}
		
		potential = new Vozilo();
		potential.setObrisan(false);
		potential.setRegistracija(newVehicle.getRegistration());
		potential.setTipVozila(TipVozila.valueOf(newVehicle.getType()));
		if(newVehicle.getLineId()!=null) {
			Linija line = getLineById(newVehicle.getLineId());
			if (line==null || line.isObrisan()) {
				return false;
			}else {
				potential.setLinija(line);
			}
		}else {
			potential.setLinija(null);
		}
		save(potential);
		return true;
	}
	
	public boolean updateVehicle(VoziloDTO updatedVehicle) {
		Vozilo potential = findOne(updatedVehicle.getId());
		if(potential==null) {
			return false;
		}
		
		potential.setRegistracija(updatedVehicle.getRegistration());
		
		if(!potential.getTipVozila().equals(TipVozila.valueOf(updatedVehicle.getType()))) {
			potential.setTipVozila(TipVozila.valueOf(updatedVehicle.getType()));
		}
		
		if(potential.getLinija()!=null) {
			if(potential.getLinija().getId()!=updatedVehicle.getLineId()) {
				//remove vehicle from old line
				Linija line = potential.getLinija();
				List<Vozilo> lineVehicle = line.getVozila();
				lineVehicle.remove(potential);
				line.setVozila(lineVehicle);
				linijaRepository.save(line);
				
				//add vehicle to new line
				if(updatedVehicle.getLineId()==null) {
					potential.setLinija(null);
				}else {
					line = getLineById(updatedVehicle.getLineId());
					if (line==null || line.isObrisan()) {
						return false;
					}else {
						potential.setLinija(line);
						lineVehicle = line.getVozila();
						lineVehicle.add(potential);
						line.setVozila(lineVehicle);
						linijaRepository.save(line);
					}
				}
			}
		}else {
			if(updatedVehicle.getLineId()!=null) {
				Linija line = getLineById(updatedVehicle.getLineId());
				if (line==null || line.isObrisan()) {
					return false;
				}else {
					potential.setLinija(line);
					List<Vozilo> lineVehicles = line.getVozila();
					lineVehicles.add(potential);
					line.setVozila(lineVehicles);
					linijaRepository.save(line);
				}
			}
		}
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
		if(allVehicles==null) {
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
		return retValue;
	}
}
