package tim7.TIM7.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.KorisnikDTO;
import tim7.TIM7.dto.LoginDTO;
import tim7.TIM7.dto.KorisnikDTO;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.security.TokenUtils;
import tim7.TIM7.services.OsobaService;

@RestController
@RequestMapping("/osoba")
public class OsobaController {
	
	@Autowired
	OsobaService osobaService;
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(path= "/registracija" ,method=RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registracija(@RequestBody KorisnikDTO korisnik) {
		
		Osoba noviKorisnik  = osobaService.findByUsername(korisnik.getKorIme());
		
		if(korisnik.getLozinka1().equals(korisnik.getLozinka2()) && noviKorisnik == null) {
			
			noviKorisnik = new Korisnik();
			kreirajKorisnika(korisnik, (Korisnik)noviKorisnik);
			osobaService.save((Korisnik)noviKorisnik);
			return new ResponseEntity<>( HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	
	}

	private void kreirajKorisnika(KorisnikDTO registracija, Korisnik noviKorisnik) {
		
		noviKorisnik.setEmail(registracija.getEmail());
		noviKorisnik.setIme(registracija.getIme());
		noviKorisnik.setPrezime(registracija.getPrezime());
		noviKorisnik.setLozinka(registracija.getLozinka1());
		noviKorisnik.setKorIme(registracija.getKorIme());
		noviKorisnik.setKarte(new ArrayList<Karta> ());
		noviKorisnik.setLokacijaDokumenta(null);
		
	
	}
	

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
        	// Perform the authentication
        	UsernamePasswordAuthenticationToken token = 
        			new UsernamePasswordAuthenticationToken(
					loginDTO.getUsername(), loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload user details so we can generate token
            UserDetails details = userDetailsService.
            		loadUserByUsername(loginDTO.getUsername());
            return new ResponseEntity<String>(
            		tokenUtils.generateToken(details), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>("Invalid login", HttpStatus.BAD_REQUEST);
        }
	}
	
	@RequestMapping(value="/izmenaPodataka", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity<List<KartaDTO>> izmenaPodataka(@RequestHeader ("X-Auth-Token") String token, KorisnikDTO korisnik ) {
		
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		
		kor.setIme(korisnik.getIme());
		kor.setPrezime(korisnik.getPrezime());
		kor.setEmail(korisnik.getEmail());
		
		osobaService.save(kor);
		
		return new ResponseEntity<>( HttpStatus.OK);
	}
	@RequestMapping(value="/izmenaLozinke", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity<List<KartaDTO>> izmenaLozinke(@RequestHeader ("X-Auth-Token") String token, KorisnikDTO korisnik ) {
		
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
				
		if(korisnik.getLozinka1().equals(korisnik.getLozinka2())) {
			kor.setLozinka(korisnik.getLozinka1());
			osobaService.save(kor);
			return new ResponseEntity<>( HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		
	
	}
	
	
}
