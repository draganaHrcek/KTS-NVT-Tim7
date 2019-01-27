package tim7.TIM7.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.CenovnikDTO;
import tim7.TIM7.dto.LinijeZoneTipovi;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.services.CenovnikService;
import tim7.TIM7.services.LinijaService;

@RestController
@RequestMapping("/cenovnici")
public class CenovnikController {
	
	@Autowired
	CenovnikService cenovnikService;
	
	@Autowired
	LinijaService linijaService;
	
	@RequestMapping(path= "/trenutni" ,method=RequestMethod.GET)
	public ResponseEntity<CenovnikDTO> getTrenutniCenovnik() {
		Cenovnik cenovnik = cenovnikService.getTrenutni();
		if(cenovnik == null){
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		CenovnikDTO cenovnikDto = cenovnikService.getTrenutniCenovnik(cenovnik);
		return new ResponseEntity<CenovnikDTO>(cenovnikDto, HttpStatus.OK);
	
	}
	
	@RequestMapping(path="", method=RequestMethod.POST, consumes = "application/json", produces = "text/plain")
	public ResponseEntity<String> addCenovnik(
			@RequestBody CenovnikDTO cenovnikDto,
			@RequestHeader ("X-Auth-Token") String token){
		
		String success = cenovnikService.addCenovnik(cenovnikDto);
		if(!success.contains("Greska!")){
			return new ResponseEntity<String>(success, HttpStatus.OK);	
		}
		else{
			return new ResponseEntity<String>(success,
					HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(value="/zaCenovnik", produces = "application/json" ,method = RequestMethod.GET)
	public ResponseEntity<LinijeZoneTipovi> getLinije(@RequestHeader ("X-Auth-Token") String token ) {
		
		LinijeZoneTipovi response = cenovnikService.getSveZaCenovnik();
		
		return new ResponseEntity<LinijeZoneTipovi>(response, HttpStatus.OK);
	
	}

}
