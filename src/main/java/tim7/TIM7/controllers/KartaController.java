package tim7.TIM7.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.OdgovorDTO;
import tim7.TIM7.dto.OdobrenjeKarteDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.security.TokenUtils;
import tim7.TIM7.services.CenovnikService;
import tim7.TIM7.services.KartaService;
import tim7.TIM7.services.LinijaService;
import tim7.TIM7.services.OsobaService;
import tim7.TIM7.services.ZonaService;

@RestController
@RequestMapping("/karte")
public class KartaController {

	@Autowired
	KartaService kartaService;


	@Autowired
	LinijaService linijaService;
	
	@Autowired
	CenovnikService cenovnikService;
	
	@Autowired
	ZonaService zonaService;

	
	@Autowired
	OsobaService osobaService;
	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value="/izlistajKarte", produces = "application/json" ,method = RequestMethod.GET)
	public ResponseEntity<List<KartaDTO>> izlistajKarte(@RequestHeader ("X-Auth-Token") String token ) {
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		ArrayList<KartaDTO> karteDTO=kartaService.findAllUserTickets(kor);
		return new ResponseEntity<>(karteDTO, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/kupovinaKarte", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity kupovinaKarte(@RequestHeader ("X-Auth-Token") String token, @RequestBody KartaDTO karta )
	{
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		
		if(kartaService.kartaExist(karta, kor)) {
			
			return new ResponseEntity<>( HttpStatus.NOT_MODIFIED);
		};
		
		double cena= kartaService.cenaKarte(karta,kor);
		kartaService.createNewTicket(karta, kor, cena);
		
		return new ResponseEntity<>( HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/proveriKartu/{tipVozila}/{nazivLinije}/{kod}" ,method = RequestMethod.POST)
	public ResponseEntity<OdgovorDTO> checkKarta(@RequestHeader ("X-Auth-Token") String token, @PathVariable String tipVozila, @PathVariable String nazivLinije, @PathVariable String kod){
		OdgovorDTO odgovor = kartaService.checkKarta(TipVozila.valueOf(tipVozila), nazivLinije, kod);
		//System.out.println(statusProvere+" "+ tipVozila+" "+ nazivLinije+" "+ kod);
		if (odgovor==null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>(odgovor, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/odobriKartu/{idKarte}/{statusOdobrenja}", method = RequestMethod.POST)
	public ResponseEntity<OdgovorDTO> verifyKarta(@RequestHeader ("X-Auth-Token") String token, @PathVariable Long idKarte, @PathVariable String statusOdobrenja){
		OdgovorDTO odgovor=kartaService.verifyKarta(idKarte, statusOdobrenja);
		if (odgovor!=null){
			return new ResponseEntity<>(odgovor, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value="/dobaviNeodobreneKarte", method = RequestMethod.GET)
	public ResponseEntity<List<OdobrenjeKarteDTO>> getNeodobreneKarte(@RequestHeader ("X-Auth-Token") String token){
		List<OdobrenjeKarteDTO> neodobreneKarte=kartaService.getNeodobreneKarte();
		if (neodobreneKarte==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>(neodobreneKarte, HttpStatus.OK);
		}
	}
	
}
