package tim7.TIM7.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<RedVoznjeDTO> getBuduciRedVoznje(@RequestHeader ("X-Auth-Token") String token){
		RedVoznjeDTO redVoznjeDto = redVoznjeService.getBuduciRedVoznje();
		if (redVoznjeDto == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<RedVoznjeDTO>(redVoznjeDto, HttpStatus.OK);
		}
	}
	
	
	//dobavljanje odredjenog rasporeda voznje za trenutni red voznje
	@RequestMapping(path="/raspored", consumes = MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.POST)
	public ResponseEntity<RasporedVoznjeDTO> getSpecificRasporedVoznje(@RequestBody RasporedVoznjeDTO rasporedVoznje){
		RasporedVoznjeDTO odredjenRasporedVoznje= redVoznjeService.getSpecificRasporedVoznje(rasporedVoznje.getDanUNedelji(), rasporedVoznje.getNazivLinije());
		if (odredjenRasporedVoznje==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<RasporedVoznjeDTO>(odredjenRasporedVoznje,HttpStatus.OK);
		}
	}
	
	
	//posto ovo moze da radi samo admin da li je potrebno da prosledim token
	@RequestMapping(path="/kreirajNovi", method=RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> createRedVoznje(@RequestHeader ("X-Auth-Token") String token, @RequestBody RedVoznjeDTO redVoznjeDto){
		String statusKreiranja=redVoznjeService.createRedVoznje(redVoznjeDto.getDatumObjavljivanja());
		if (statusKreiranja.equals("POSTOJI")){
			return new ResponseEntity<>("Vec postoji kreiran buduci red voznje", HttpStatus.ALREADY_REPORTED);
		}else if(statusKreiranja.equals("LOS DATUM")){
			return new ResponseEntity<>("Najraniji datum pocetka je sutrasnji", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>("Red voznje uspesno kreiran", HttpStatus.CREATED);
		}
	}
	
	@RequestMapping(path="/obrisiBuduci", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteBuduciRedVoznje(@RequestHeader ("X-Auth-Token") String token){
		String statusBrisanja=redVoznjeService.deleteBuduciRedVoznje();
		if (statusBrisanja.equals("NE POSTOJI")){
			return new ResponseEntity<>("Ne postoji buduci red voznje", HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>("Buduci red voznje uspesno obrisan", HttpStatus.ACCEPTED);
		}
	}
	
}