package tim7.TIM7.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.RasporedVoznjeDTO;
import tim7.TIM7.dto.RedVoznjeDTO;
import tim7.TIM7.services.RedVoznjeService;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/redVoznje")
public class RedVoznjeController {

	@Autowired 
	RedVoznjeService redVoznjeService;
	
	@RequestMapping(path="/trenutni", method=RequestMethod.GET)
	public ResponseEntity<RedVoznjeDTO> getTrenutniRedVoznje(){
		RedVoznjeDTO redVoznjeDto = redVoznjeService.getTrenutniRedVoznje();
		if (redVoznjeDto == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<RedVoznjeDTO>(redVoznjeDto, HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(path="/buduci", method=RequestMethod.GET)
	public ResponseEntity<RedVoznjeDTO> getBuduciRedVoznje(){
		RedVoznjeDTO redVoznjeDto = redVoznjeService.getBuduciRedVoznje();
		if (redVoznjeDto == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<RedVoznjeDTO>(redVoznjeDto, HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(path="/raspored", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RasporedVoznjeDTO> getSpecificRasporedVoznje(@RequestBody RasporedVoznjeDTO rasporedVoznje){
		RasporedVoznjeDTO odredjenRasporedVoznje= redVoznjeService.getSpecificRasporedVoznje(rasporedVoznje.getDanUNedelji(), rasporedVoznje.getNazivLinije());
		if (odredjenRasporedVoznje==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<RasporedVoznjeDTO>(odredjenRasporedVoznje,HttpStatus.OK);
		}
	}
	
	
	/*
	@RequestMapping(path="/kreirajNovi", method=RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> createRedVoznje(RedVoznjeDTO redVoznjeDto){
		
	}*/
}
