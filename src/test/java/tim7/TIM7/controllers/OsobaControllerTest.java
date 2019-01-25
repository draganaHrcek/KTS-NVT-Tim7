package tim7.TIM7.controllers;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import tim7.TIM7.dto.KorisnikDTO;
import tim7.TIM7.dto.KorisnikTokenDTO;
import tim7.TIM7.dto.LoginDTO;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.security.TokenUtils;
import tim7.TIM7.services.OsobaService;
import tim7.TIM7.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)

public class OsobaControllerTest {
	private static final String URL_PREFIX = "/osoba";

	private MockMvc mockMvc;

	
	@Autowired
	private WebApplicationContext webApplicationContext;	
	
	@Autowired
	OsobaService osobaServ;
	
	@Autowired
	OsobaController osobaCon;
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private Korisnik  korisnik1, korisnik2;
	
	  @PostConstruct
	    public void setup() {
	    	this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	    	
	    	
	    	if(korisnik1 == null){
	    		korisnik1 = new Korisnik();
	    		korisnik1.setKorIme("KorIme");
	    		korisnik1.setIme("StaroIme");
	    		korisnik1.setPrezime("StaroPrezime");
				korisnik1.setEmail("stariMail@gmail.com");
				korisnik1.setLozinka(encoder.encode("12345678"));
	    	}
	    	
	    	if(osobaServ.findByUsername("KorIme")==null) {
	    		osobaServ.save(korisnik1);
	    	}
	    	
	    	if(korisnik2 == null){
	    		korisnik2= new Korisnik();
	    		korisnik2.setKorIme("KorIme2");
	    		korisnik2.setLozinka(encoder.encode("11111111"));
	    		korisnik2.setPrezime("Hrcek");
	    		korisnik2.setEmail("dragana.hrcek@gmail.com");
	    		korisnik2.setIme("Dragana");
	    		korisnik2.setStatus(StatusKorisnika.valueOf("STUDENT"));
	    	}
	    	if(osobaServ.findByUsername("KorIme2")==null) {
	    		
	    		osobaServ.save(korisnik2);
	    	}
	    }
	 
    @Test
    public void registracijaPostojecegKorisnika() throws Exception {
    	
    	KorisnikDTO korDTO = new KorisnikDTO();
    	korDTO.setKorIme("KorisnikTest");
		
		Korisnik postojeci = new Korisnik();
		postojeci.setKorIme("KorisnikTest");
    	osobaServ.save(postojeci);
    	
    	mockMvc.perform(post(URL_PREFIX + "/registracija").contentType(MediaType.APPLICATION_JSON_VALUE).content( TestUtil.json(korDTO))).andExpect(status().isFound());
	     
    }
    @Test
    public void registracijaNeispravnoPotvrdjenaLozinka() throws Exception {
		
		KorisnikDTO korDTO = new KorisnikDTO();
		korDTO.setLozinka1("12345678");
		korDTO.setLozinka2("87654321");
		
    	mockMvc.perform(post(URL_PREFIX + "/registracija").contentType(MediaType.APPLICATION_JSON_VALUE).content(TestUtil.json(korDTO))).andExpect(status().isNotModified());
	     
    }
    @Test
    public void regsitracijaUspesna() throws Exception {
    	

		KorisnikDTO korDTO = new KorisnikDTO();
		korDTO.setIme("Dragana");
		korDTO.setEmail("dragana.hrcek@gmail.com");
		korDTO.setPrezime("Hrcek");
		korDTO.setKorIme("dragana123");
		korDTO.setLozinka1("12345678");
		korDTO.setLozinka2("12345678");
		
    	mockMvc.perform(post(URL_PREFIX + "/registracija").contentType(MediaType.APPLICATION_JSON_VALUE).content(TestUtil.json(korDTO))).andExpect(status().isCreated());
	     
    }
    
    @Test
    public void izmenaLicnihPodataka() throws Exception {
		
		KorisnikDTO korDTO = new KorisnikDTO();
		korDTO.setIme("NovoIme");
		korDTO.setEmail("novEmail@gmail.com");
		korDTO.setPrezime("NovoPrezime");
		
    	String token = TestUtil.generateToken(korisnik1.getKorIme());
      	mockMvc.perform(post(URL_PREFIX + "/izmenaPodataka").header("X-Auth-Token", token).contentType(MediaType.APPLICATION_JSON).content(TestUtil.json(korDTO))).andExpect(status().isOk());
      	
      	Korisnik kor= (Korisnik) osobaServ.findByUsername("KorIme");
      	assertEquals(kor.getPrezime() , "NovoPrezime");
		assertEquals(kor.getIme() , "NovoIme");
		assertEquals(kor.getEmail() , "novEmail@gmail.com");
    }

    @Test
    public void izmenaLozinkeNeispravnaTrenutna() throws Exception {
    	
    	KorisnikDTO korDTO = new KorisnikDTO();
    	korDTO.setTrenutnaLozinka("pogresnaTrenutnaLozinka");
		
    	String token = TestUtil.generateToken(korisnik1.getKorIme());
      	mockMvc.perform(post(URL_PREFIX + "/izmenaLozinke").header("X-Auth-Token", token).contentType(MediaType.APPLICATION_JSON).content(TestUtil.json(korDTO))).andExpect(status().isNotModified());
    }

    @Test
    public void izmenaLozinkeNijePotvrdjenaNovaLozinka() throws Exception {
	
		KorisnikDTO korDTO = new KorisnikDTO();
		korDTO.setTrenutnaLozinka("11111111");
		korDTO.setLozinka1("87654321");
		korDTO.setLozinka2("blabla");
		
    	String token = TestUtil.generateToken(korisnik2.getKorIme());
      	mockMvc.perform(post(URL_PREFIX + "/izmenaLozinke").header("X-Auth-Token", token).contentType(MediaType.APPLICATION_JSON).content(TestUtil.json(korDTO))).andExpect(status().isBadRequest());
    }

    @Test
    public void uspesnaIzmenaLozinke() throws Exception {
		
		KorisnikDTO korDTO = new KorisnikDTO();
		korDTO.setTrenutnaLozinka("12345678");
		korDTO.setLozinka1("87654321");
		korDTO.setLozinka2("87654321");
   
    	String token = TestUtil.generateToken(korisnik1.getKorIme());
      	mockMvc.perform(post(URL_PREFIX + "/izmenaLozinke").header("X-Auth-Token", token).contentType(MediaType.APPLICATION_JSON).content(TestUtil.json(korDTO))).andExpect(status().isOk());
   
      	Korisnik kor= (Korisnik) osobaServ.findByUsername(korisnik1.getKorIme());
      	assertTrue(encoder.matches("87654321", kor.getLozinka()));
    }
    @Test
    public void uspesanLogina() throws Exception {
		
		LoginDTO loginDTO= new LoginDTO();
		loginDTO.setUsername("KorIme2");
		loginDTO.setPassword("11111111");

      	mockMvc.perform(post(URL_PREFIX + "/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(TestUtil.json(loginDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.token").value(notNullValue(String.class)));
   
    }

	@Test
    public void neuspesanLogin() throws Exception {
		LoginDTO neispravanLogin=null;
      	mockMvc.perform(post(URL_PREFIX + "/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(TestUtil.json(neispravanLogin))).andExpect(status().isBadRequest());
   
   

    }
	
	@Test
	 public void uzmiPrijavljenogKorisnika() throws Exception {
		
		String token = TestUtil.generateToken(korisnik2.getKorIme());
		
		mockMvc.perform(get(URL_PREFIX + "/prijavljenKorisnik").header("X-Auth-Token", token))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(jsonPath("$.ime").value("Dragana"))
        .andExpect(jsonPath("$.prezime").value("Hrcek"))
        .andExpect(jsonPath("$.email").value("dragana.hrcek@gmail.com"))
        .andExpect(jsonPath("$.korIme").value("KorIme2"))
        .andExpect(jsonPath("$.status").value("STUDENT"))
        .andExpect(jsonPath("$.uloga").value("KORISNIK"));
   
   

    }
	
	

}
