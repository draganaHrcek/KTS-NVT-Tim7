package tim7.TIM7.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.RegistracijaDTO;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.services.OsobaService;

@RestController
@RequestMapping("/osoba")
public class OsobaController {
	
	@Autowired
	OsobaService osobaService;

	@RequestMapping(path= "/registracija" ,method=RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registracija(@RequestBody RegistracijaDTO korisnik) {
		
		Osoba noviKorisnik  = osobaService.findByUsername(korisnik.getKorIme());
		
		if(korisnik.getLozinka1().equals(korisnik.getLozinka2()) && noviKorisnik == null) {
			
			noviKorisnik = new Korisnik();
			kreirajKorisnika(korisnik, (Korisnik)noviKorisnik);
			osobaService.save((Korisnik)noviKorisnik);
			return new ResponseEntity<>( HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	
	}

	private void kreirajKorisnika(RegistracijaDTO registracija, Korisnik noviKorisnik) {
		
		noviKorisnik.setEmail(registracija.getEmail());
		noviKorisnik.setIme(registracija.getIme());
		noviKorisnik.setPrezime(registracija.getPrezime());
		noviKorisnik.setLozinka(registracija.getLozinka1());
		noviKorisnik.setKorIme(registracija.getKorIme());
		noviKorisnik.setKarte(new ArrayList<Karta> ());
		noviKorisnik.setLokacijaDokumenta(null);
		noviKorisnik.setStatus(null);
	
	}
	
}
