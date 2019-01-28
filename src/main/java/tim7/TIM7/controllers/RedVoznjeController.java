package tim7.TIM7.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.RasporedVoznjeDTO;
import tim7.TIM7.dto.RedVoznjeDTO;
import tim7.TIM7.services.RasporedVoznjeService;
import tim7.TIM7.services.RedVoznjeService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/redVoznje")
public class RedVoznjeController {

	@Autowired 
	RedVoznjeService redVoznjeService;
	
	@Autowired 
	RasporedVoznjeService rasporedVoznjeService;
	
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
		RedVoznjeDTO trenutniRedVoznje = redVoznjeService.getTrenutniRedVoznje();
		RasporedVoznjeDTO odredjenRasporedVoznje= rasporedVoznjeService.getSpecificRasporedVoznje(rasporedVoznje.getDanUNedelji(), rasporedVoznje.getNazivLinije(), trenutniRedVoznje);
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
	
	
	@RequestMapping(path="/izmeniBuduci", method=RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> changeBuduciRedVoznje(@RequestHeader ("X-Auth-Token") String token, @RequestBody RedVoznjeDTO redVoznjeDto){
		String statusIzmene=redVoznjeService.changeBuduciRedVoznje(redVoznjeDto.getDatumObjavljivanja());
		if (statusIzmene.equals("NE POSTOJI")){
			return new ResponseEntity<>("Ne postoji buduci red voznje", HttpStatus.NOT_FOUND);
		}else if(statusIzmene.equals("LOS DATUM")){
			return new ResponseEntity<>("Najraniji datum pocetka je sutrasnji", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>("Uspesno izmenjen datum", HttpStatus.ACCEPTED);
		}
	}
	
	
	@RequestMapping(path="/kreirajRaspored", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<String> createRasporedVoznje(@RequestHeader ("X-Auth-Token") String token, @RequestBody RasporedVoznjeDTO rasporedVoznjeDto){
		RedVoznjeDTO buduciRedVoznje=redVoznjeService.getBuduciRedVoznje();
		String statusKreiranja=rasporedVoznjeService.createRasporedVoznje(rasporedVoznjeDto, buduciRedVoznje);
		if (statusKreiranja.equals("NE POSTOJI")){
			return new ResponseEntity<>("Ne postoji buduci red voznje", HttpStatus.NOT_FOUND);
		}else if (statusKreiranja.equals("VEC POSTOJI")){
			return new ResponseEntity<>("Vec postoji raspored voznje za zadatu liniju i dan u nedelji", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>("Uspesno kreiran raspored voznje", HttpStatus.CREATED);
		}
	}
	
	@RequestMapping(path="/obrisiRaspored/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteRasporedVoznje(@RequestHeader ("X-Auth-Token") String token, @PathVariable Long id){
		RedVoznjeDTO buduciRedVoznje=redVoznjeService.getBuduciRedVoznje();
		String statusBrisanja=rasporedVoznjeService.deleteRasporedVoznje(id, buduciRedVoznje);
		if (statusBrisanja.equals("NE POSTOJI")){
			//mogao je da postane trenutni u medjuvremenu
			return new ResponseEntity<>("Nemoguce obrisati izabrani raspored", HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>("Uspesno brisanje rasporeda", HttpStatus.ACCEPTED);
		}
	}
	
	@RequestMapping(path="/izmeniRasporedVremena", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<String> changeRasporedVoznje(@RequestHeader ("X-Auth-Token") String token, @RequestBody RasporedVoznjeDTO rasporedVoznjeDto){
		RedVoznjeDTO buduciRedVoznje=redVoznjeService.getBuduciRedVoznje();
		String statusIzmene=rasporedVoznjeService.changeRasporedVoznje(rasporedVoznjeDto.getId(), rasporedVoznjeDto.getVremena(), buduciRedVoznje);
		if (statusIzmene.equals("NE POSTOJI")){
			//mogao je da postane trenutni u medjuvremenu
			return new ResponseEntity<>("Nemoguce izmeniti izabrani raspored", HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>("Uspesna izmena rasporeda", HttpStatus.ACCEPTED);
		}
	}
	
	@RequestMapping(path="/dobaviBuduceRasporede", method=RequestMethod.GET)
	public ResponseEntity<List<RasporedVoznjeDTO>> getBuduciNeobrisaniRasporedi(@RequestHeader ("X-Auth-Token") String token){
		RedVoznjeDTO buduciRedVoznje=redVoznjeService.getBuduciRedVoznje();
		List<RasporedVoznjeDTO> buduciRasporedi=rasporedVoznjeService.getNeobrisaniRasporedi(buduciRedVoznje);
		if (buduciRasporedi==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>(buduciRasporedi,HttpStatus.OK);
		}
	}
	
	//za popunjavanje postojecig rasporeda trenutnih
	@RequestMapping(path="/dobaviTrenutneRasporede", method=RequestMethod.GET)
	public ResponseEntity<List<RasporedVoznjeDTO>> getTrenutniNeobrisaniRasporedi(){
		RedVoznjeDTO trenutniRedVoznje=redVoznjeService.getTrenutniRedVoznje();
		List<RasporedVoznjeDTO> trenutniRasporedi=rasporedVoznjeService.getNeobrisaniRasporedi(trenutniRedVoznje);
		if (trenutniRasporedi==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>(trenutniRasporedi,HttpStatus.OK);
		}
	}
}
