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
import tim7.TIM7.dto.KorisnikDTO;
import tim7.TIM7.services.CenovnikService;

@RestController
@RequestMapping("/cenovnici")
public class CenovnikController {
	
	@Autowired
	CenovnikService cenovnikService;
	
	@RequestMapping(path= "/trenutni" ,method=RequestMethod.GET)
	public ResponseEntity<CenovnikDTO> getTrenutniCenovnik() {
		CenovnikDTO cenovnik = cenovnikService.getTrenutniCenovnik();
		if(cenovnik == null){
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
				
		return new ResponseEntity<CenovnikDTO>(cenovnik, HttpStatus.OK);
	
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<CenovnikDTO> editCenovnik(
			@RequestBody CenovnikDTO cenovnikDto,
			@RequestHeader ("X-Auth-Token") String token){
		
		
		return new ResponseEntity<CenovnikDTO>(cenovnikDto, HttpStatus.OK);
		
	}

}
