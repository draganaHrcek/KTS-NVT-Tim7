package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.StanicaDTO;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.StanicaULiniji;
import tim7.TIM7.repositories.StanicaRepository;
import tim7.TIM7.repositories.StanicaULinijiRepository;

@Service
public class StanicaService {

	@Autowired
	StanicaRepository stanicaRepository;
	
	@Autowired 
	StanicaULinijiRepository sulRepository;
	
	public List<Stanica> findAll() {
		return stanicaRepository.findAll();
	}
	
	public Stanica save(Stanica station) {
		return stanicaRepository.save(station);
	}
	
	public Stanica findOne(Long id){
		try {
			Stanica station = stanicaRepository.findById(id).get();
			return station;
		}catch(Exception e) {
			return null;
		}
	}
	
	public boolean addNewStation(StanicaDTO newStationDTO) {
		Stanica potential = findOne(newStationDTO.getId());
		if (potential!=null) {
			return false;
		}
		potential = new Stanica();
		potential.setId(newStationDTO.getId());
		potential.setLatituda(newStationDTO.getLatitude());
		potential.setLongituda(newStationDTO.getLongitude());
		potential.setObrisan(false);
		potential.setOznaka(newStationDTO.getName());
		save(potential);
		return true;
	}
	
	public boolean deleteStation(Long id) {
		Stanica potential = findOne(id);
		if(potential==null) {
			return false;
		}
		
		potential.setObrisan(true);
		save(potential);
		List<StanicaULiniji> list = sulRepository.findByStanicaAndObrisanFalse(potential);
		for(StanicaULiniji sul : list) {
			sul.setObrisan(true);
			sulRepository.save(sul);
		}
		return false;
	}
	
	public boolean updateStation(StanicaDTO updatedStation) {
		Stanica potential = findOne(updatedStation.getId());
		if(potential==null) {
			return false;
		}
		
		potential.setOznaka(updatedStation.getName());
		save(potential);
		return true;
	}
	
	public List<StanicaDTO> getAllStations(){
		List<Stanica> allStations = findAll();
		if(allStations==null) {
			return null;
		}
		List<StanicaDTO> retValue = new ArrayList<StanicaDTO>();
		for(Stanica station : allStations) {
			if(station.isObrisan()) {
				continue;
			}
			StanicaDTO stationDTO = new StanicaDTO(station);
			retValue.add(stationDTO);
		}
		return retValue;
	}	

}
