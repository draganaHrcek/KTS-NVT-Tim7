package tim7.TIM7.controllers;

import java.util.ArrayList;
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
import tim7.TIM7.dto.ZonaDTO;
import tim7.TIM7.dto.RutaDTO;
import tim7.TIM7.model.Zona;
import tim7.TIM7.services.ZonaService;

@RestController
@RequestMapping("/zone")
public class ZonaController {

	@Autowired
	ZonaService zonaService;
	
	@RequestMapping(path="/dodaj", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<Void> addNovaZona(@RequestBody ZonaDTO newZone, @RequestHeader ("X-Auth-Token") String token){
		if(zonaService.addNewZone(newZone)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/brisi/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteZona(@PathVariable Long id, @RequestHeader ("X-Auth-Token") String token){
		if(zonaService.deleteZone(id)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/mijenjaj", method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Void> updateZona(@RequestBody ZonaDTO updatedZone, @RequestHeader ("X-Auth-Token") String token){
		if(zonaService.updateZone(updatedZone)) {
			return new ResponseEntity<>(HttpStatus.OK);
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
	
@RequestMapping(value="/izlistajZone", produces = "application/json" ,method = RequestMethod.GET)
public ResponseEntity<RutaDTO> izlistajKarte(@RequestHeader ("X-Auth-Token") String token ) {
	RutaDTO rute= new RutaDTO();
	rute.setLinijeZone(new ArrayList<>());
	List<Zona> zone = zonaService.findAll();
	for (Zona zona : zone) {
		rute.getLinijeZone().add(zona.getNaziv());
	}
	return new ResponseEntity<>(rute, HttpStatus.OK);
}
}

