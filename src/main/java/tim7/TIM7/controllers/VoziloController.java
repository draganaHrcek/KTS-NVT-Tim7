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

import tim7.TIM7.dto.VoziloDTO;
import tim7.TIM7.services.VoziloService;

@RestController
@RequestMapping("/vozila")
public class VoziloController {

	@Autowired
	VoziloService voziloService;
	
	@RequestMapping(path="/dodaj", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<List<VoziloDTO>> addNovoVozilo(@RequestBody VoziloDTO newVehicle, @RequestHeader ("X-Auth-Token") String token){
		if(voziloService.addNewVehicle(newVehicle)) {
			List<VoziloDTO> retValue = voziloService.getAllVehicles();
			return new ResponseEntity<List<VoziloDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/brisi/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<List<VoziloDTO>> deleteVozilo(@PathVariable Long id, @RequestHeader ("X-Auth-Token") String token){
		if(voziloService.deleteVehicle(id)) {
			List<VoziloDTO> retValue = voziloService.getAllVehicles();
			return new ResponseEntity<List<VoziloDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/mijenjaj", method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<List<VoziloDTO>> updateVozilo(@RequestBody VoziloDTO updatedVehicle, @RequestHeader ("X-Auth-Token") String token){
		if(voziloService.updateVehicle(updatedVehicle)) {
			List<VoziloDTO> retValue = voziloService.getAllVehicles();
			return new ResponseEntity<List<VoziloDTO>>(retValue, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	//provjeri ako je prazna tabela
	@RequestMapping(path="/sve", method = RequestMethod.GET)
	public ResponseEntity<List<VoziloDTO>> getSvaVozila(){
		List<VoziloDTO> retValue = voziloService.getAllVehicles();
		if(retValue==null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}else {
			return new ResponseEntity<List<VoziloDTO>>(retValue, HttpStatus.OK);
		}
	}
	/*
	@RequestMapping(path="/dodajULiniju/{lineId}/{vehicleId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> dodajVoziloULiniju(@PathVariable Long lineId, @PathVariable Long vehicleId, @RequestHeader ("X-Auth-Token") String token){
		if(voziloService.addVehicleToLine(lineId, vehicleId)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path="/izbaciIzLinije/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> izbaciVoziloIzLinije(@PathVariable Long id, @RequestHeader ("X-Auth-Token") String token){
		if(voziloService.removeVehicleFromLine(id)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}*/
}
