package tim7.TIM7.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import tim7.TIM7.dto.LoginDTO;
import tim7.TIM7.dto.RegistracijaDTO;
import tim7.TIM7.dto.UlogovanDTO;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.security.TokenUtils;
import tim7.TIM7.services.OsobaService;
import tim7.TIM7.storage.StorageFileNotFoundException;
import tim7.TIM7.storage.StorageService;

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
	
	private final StorageService storageService;

    @Autowired
    public OsobaController(StorageService storageService) {
        this.storageService = storageService;
    }

	@RequestMapping(path= "/registracija" ,method=RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registracija(@RequestBody RegistracijaDTO korisnik) {
		
		Osoba noviKorisnik  = osobaService.findByUsername(korisnik.getKorIme());
		
		if(noviKorisnik != null) {
			return new ResponseEntity<>(HttpStatus.FOUND);
		}
		
		if(korisnik.getLozinka1().equals(korisnik.getLozinka2())) {
			
			osobaService.createNewUser(korisnik);
			return new ResponseEntity<>( HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	
	}

	

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<UlogovanDTO> login(@RequestBody LoginDTO loginDTO) {
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
          
            
            
            Osoba osoba= osobaService.findByUsername(loginDTO.getUsername());
    		UlogovanDTO korisnik= osobaService.findUlogovanog(osoba);
    		korisnik.setToken(tokenUtils.generateToken(details));
            return new ResponseEntity<UlogovanDTO>(korisnik, HttpStatus.OK);
            
        } catch (Exception ex) {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
	}
	
	@RequestMapping(value="/izmenaPodataka", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity izmenaPodataka(@RequestHeader ("X-Auth-Token") String token,@RequestBody UlogovanDTO korisnik ) {
		
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		
		kor.setIme(korisnik.getIme());
		kor.setPrezime(korisnik.getPrezime());
		kor.setEmail(korisnik.getEmail());
		
		osobaService.save(kor);
		
		return new ResponseEntity<>( HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/izmenaLozinke", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity izmenaLozinke(@RequestHeader ("X-Auth-Token") String token,@RequestBody RegistracijaDTO korisnik ) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		Osoba kor = osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		
		if (!encoder.matches(korisnik.getTrenutnaLozinka(), kor.getLozinka())) {
			
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
			
		}
		if(korisnik.getLozinka1().equals(korisnik.getLozinka2())) {
			kor.setLozinka(encoder.encode(korisnik.getLozinka1()));
			osobaService.save(kor);
			return new ResponseEntity<>( HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	
	}

	   @GetMapping("/files")
	    public String listUploadedFiles(Model model) throws IOException {

	        model.addAttribute("files", storageService.loadAll().map(
	                path -> MvcUriComponentsBuilder.fromMethodName(OsobaController.class,
	                        "serveFile", path.getFileName().toString()).build().toString())
	                .collect(Collectors.toList()));

	        return "uploadForm";
	    }

	    @GetMapping("/files/{filename:.+}")
	    @ResponseBody
	    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

	        Resource file = storageService.loadAsResource(filename);
	        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	    }

		@RequestMapping(value="/files", consumes = "multipart/form-data" ,method = RequestMethod.POST, produces="text/plain")
	    public ResponseEntity<String> handleFileUpload(@RequestPart MultipartFile file) {
	        storageService.store(file);
	        
			return new ResponseEntity<String>( file.getOriginalFilename(),HttpStatus.OK);
	    }

	    @ExceptionHandler(StorageFileNotFoundException.class)
	    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
	        return ResponseEntity.notFound().build();
	    }
		
	

	
	
	
}
