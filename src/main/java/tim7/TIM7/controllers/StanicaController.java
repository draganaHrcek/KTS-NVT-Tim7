package tim7.TIM7.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.StanicaDTO;
import tim7.TIM7.services.StanicaService;

@RestController
@RequestMapping("/stanice")
public class StanicaController {

	@Autowired
	StanicaService stanicaService;
	
	/*
	@RequestMapping(path="/jednaLinija", method=RequestMethod.GET)
	public ResponseEntity<List<StanicaDTO>> getStaniceJedneLinije(){
		List<StanicaDTO> retValue = stanicaService.getAllStations();
		if (retValue==null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<List<StanicaDTO>>(retValue, HttpStatus.OK);
	}*/
	
	@RequestMapping(path="/dodaj", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<List<StanicaDTO>> addNovaStanica(@RequestBody StanicaDTO newStation, @RequestHeader ("X-Auth-Token") String token){
		if(stanicaService.addNewStation(newStation)) {
			List<StanicaDTO> retValue = stanicaService.getAllStations();
			return new ResponseEntity<List<StanicaDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/brisi/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<List<StanicaDTO>> deleteStanica(@PathVariable Long id, @RequestHeader ("X-Auth-Token") String token){
		if(stanicaService.deleteStation(id)) {
			List<StanicaDTO> retValue = stanicaService.getAllStations();
			return new ResponseEntity<List<StanicaDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/mijenjaj", method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<List<StanicaDTO>> updateStanica(@RequestBody StanicaDTO updatedStation, @RequestHeader ("X-Auth-Token") String token){
		if(stanicaService.updateStation(updatedStation)) {
			List<StanicaDTO> retValue = stanicaService.getAllStations();
			return new ResponseEntity<List<StanicaDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	//provjeri ako je prazna tabela
	@RequestMapping(path="/sve", method = RequestMethod.GET)
	public ResponseEntity<List<StanicaDTO>> getSveStanice(){
		List<StanicaDTO> retValue = stanicaService.getAllStations();
		if(retValue==null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}else {
			return new ResponseEntity<List<StanicaDTO>>(retValue, HttpStatus.OK);
		}
	}
}
