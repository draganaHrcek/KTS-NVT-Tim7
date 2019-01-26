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

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.UpdatedZonaDTO;
import tim7.TIM7.dto.ZonaDTO;
import tim7.TIM7.services.ZonaService;

@RestController
@RequestMapping("/zone")
public class ZonaController {

	@Autowired
	ZonaService zonaService;
	
	@RequestMapping(path="/dodaj", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<List<ZonaDTO>> addNovaZona(@RequestBody UpdatedZonaDTO newZone, @RequestHeader ("X-Auth-Token") String token){
		if(zonaService.addNewZone(newZone)) {
			List<ZonaDTO> retValue = zonaService.getAllZones();
			return new ResponseEntity<List<ZonaDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/brisi/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<List<ZonaDTO>> deleteZona(@RequestHeader ("X-Auth-Token") String token, @PathVariable Long id){
		if(zonaService.deleteZone(id)) {
			List<ZonaDTO> retValue = zonaService.getAllZones();
			return new ResponseEntity<List<ZonaDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/mijenjaj", method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<List<ZonaDTO>> updateZona(@RequestHeader ("X-Auth-Token") String token, @RequestBody UpdatedZonaDTO updatedZone){
		if(zonaService.updateZone(updatedZone)) {
			List<ZonaDTO> retValue = zonaService.getAllZones();
			return new ResponseEntity<List<ZonaDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	//provjeri ako je prazna tabela
	@RequestMapping(path="/sve", method = RequestMethod.GET)
	public ResponseEntity<List<ZonaDTO>> getSveZone(){
		List<ZonaDTO> retValue = zonaService.getAllZones();
		if(retValue==null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}else {
			return new ResponseEntity<List<ZonaDTO>>(retValue, HttpStatus.OK);
		}
	}

}

