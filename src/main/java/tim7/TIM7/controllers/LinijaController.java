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

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.model.Linija;
import tim7.TIM7.services.LinijaService;

@RestController
@RequestMapping("/linije")
public class LinijaController {

	@Autowired
	LinijaService linijaService;
	
	@RequestMapping(path= "/sve" ,method=RequestMethod.GET)
	public ResponseEntity<List<LinijaDTO>> getSveLinije(){
		List<LinijaDTO> retValue = linijaService.getAllLines();
		if (retValue==null || retValue.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<List<LinijaDTO>>(retValue, HttpStatus.OK);
	}
	
	@RequestMapping(path="/brisi/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteJednaLinija(@PathVariable Long id, @RequestHeader ("X-Auth-Token") String token){
		Linija line = linijaService.deleteOneLine(id);
		if (line==null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(path="/dodaj", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Void> addNovaLinija(@RequestBody LinijaDTO newLine, @RequestHeader ("X-Auth-Token") String token){
		if(!linijaService.addNewLine(newLine)) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(path="/mijenjaj", method=RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Void> updateLinije(@RequestBody LinijaDTO updatedLine, @RequestHeader ("X-Auth-Token") String token){
		if(linijaService.updateLine(updatedLine)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@RequestMapping(path= "/sveJedneZone/{id}" ,method=RequestMethod.GET)
	public ResponseEntity<List<LinijaDTO>> getLinijeJedneZone(@PathVariable Long id){
		List<LinijaDTO> retValue = linijaService.getLinesFromOneZone(id);
		if (retValue==null || retValue.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<List<LinijaDTO>>(retValue, HttpStatus.OK);
	}
	

	@RequestMapping(value="/zaCenovnik", produces = "application/json" ,method = RequestMethod.GET)
	public ResponseEntity<List<LinijaDTO>> getLinije(@RequestHeader ("X-Auth-Token") String token ) {
		
		List<Linija> linije = linijaService.findAll();
		List<LinijaDTO> linijeDTO = new ArrayList<LinijaDTO>();
		
		for (Linija linija : linije) {
			linijeDTO.add(new LinijaDTO(linija));
		}
		
		return new ResponseEntity<List<LinijaDTO>>(linijeDTO, HttpStatus.OK);
	}
	

}

