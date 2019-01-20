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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.KorisnikDTO;
import tim7.TIM7.dto.KorisnikTokenDTO;
import tim7.TIM7.dto.LoginDTO;
import tim7.TIM7.dto.TokenDTO;
import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Kondukter;
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
		
		if(noviKorisnik != null) {
			return new ResponseEntity<>(HttpStatus.FOUND);
		}
		
		if(korisnik.getLozinka1().equals(korisnik.getLozinka2())) {
			
			noviKorisnik = new Korisnik();
			kreirajKorisnika(korisnik, (Korisnik)noviKorisnik);
			osobaService.save((Korisnik)noviKorisnik);
			return new ResponseEntity<>( HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	
	}

	private void kreirajKorisnika(KorisnikDTO registracija, Korisnik noviKorisnik) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		noviKorisnik.setEmail(registracija.getEmail());
		noviKorisnik.setIme(registracija.getIme());
		noviKorisnik.setPrezime(registracija.getPrezime());
		noviKorisnik.setLozinka(encoder.encode(registracija.getLozinka1()));
		noviKorisnik.setKorIme(registracija.getKorIme());
		noviKorisnik.setKarte(new ArrayList<Karta> ());
		noviKorisnik.setLokacijaDokumenta(null);
		noviKorisnik.setStatus(null);

	
	}
	

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
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
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(tokenUtils.generateToken(details));
            return new ResponseEntity<TokenDTO>(tokenDTO, HttpStatus.OK);
            
        } catch (Exception ex) {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
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
	@RequestMapping(value="/prijavljenKorisnik", produces = "application/json",method = RequestMethod.GET)
	public ResponseEntity<KorisnikTokenDTO> prijavljenKorisnik(@RequestHeader ("X-Auth-Token") String token ) {
		

		KorisnikTokenDTO kor= new KorisnikTokenDTO();
		Osoba o= osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		
		kor.setKorIme(o.getKorIme());
		kor.setEmail(o.getEmail());
		kor.setIme(o.getIme());
		kor.setPrezime(o.getPrezime());
		
		if (o instanceof Korisnik) {
			kor.setUloga("KORISNIK");
			if(((Korisnik) o).getStatus()!=null) {
				kor.setStatus(((Korisnik) o).getStatus().toString());
			}
		}else if(o instanceof Administrator) {
			
			kor.setUloga("ADMINISTRATOR");
			
		}else if (o instanceof Kondukter) {
			kor.setUloga("KONDUKTER");
			
			
		}else {
			
			
			kor.setUloga("VERIFIKATOR");
		}
		
		return new ResponseEntity<KorisnikTokenDTO>( kor,HttpStatus.OK);
		
		}
		

	
	
	
}
