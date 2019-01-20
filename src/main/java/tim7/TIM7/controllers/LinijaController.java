package tim7.TIM7.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.RutaDTO;
import tim7.TIM7.model.Linija;
import tim7.TIM7.services.LinijaService;

@RestController
@RequestMapping("/linije")
public class LinijaController {

	@Autowired
	LinijaService linijaService;
	
		
	@RequestMapping(value="/izlistajLinije", produces = "application/json" ,method = RequestMethod.GET)
	public ResponseEntity<RutaDTO> izlistajKarte(@RequestHeader ("X-Auth-Token") String token ) {
		RutaDTO rute= new RutaDTO();
		rute.setLinijeZone(new ArrayList<>());
		List<Linija> linije = linijaService.findAll();
		for (Linija linija : linije) {
			rute.getLinijeZone().add(linija.getNaziv());
		}
		return new ResponseEntity<>(rute, HttpStatus.OK);
	}
}
