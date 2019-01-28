package tim7.TIM7.services;



import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.TIM7.dto.RegistracijaDTO;
import tim7.TIM7.dto.UlogovanDTO;
import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Kondukter;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.Verifikator;
import tim7.TIM7.repositories.OsobaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class OsobaServiceTest {
	
	@Autowired 
	private OsobaService osobaServ;
	
	@MockBean
	private OsobaRepository osobaRep;
	
	private Korisnik korisnik, korisnikBezStatusa;
	private Administrator admin;
	private Verifikator verifikator;
	private Kondukter kondukter;
	
	@Before
	public void setUp() {
		
		korisnik= new Korisnik();
		korisnik.setKorIme("KorIme1");
		korisnik.setIme("Ime1");
		korisnik.setPrezime("Prezime1");
		korisnik.setEmail("korisnik@gmail.com");
		korisnik.setLozinka("123");
		korisnik.setStatus(StatusKorisnika.valueOf("STUDENT"));
		
		
		
		korisnikBezStatusa= new Korisnik();
		korisnikBezStatusa.setKorIme("KorIme2");
		korisnikBezStatusa.setIme("Ime2");
		korisnikBezStatusa.setPrezime("Prezime2");
		korisnikBezStatusa.setEmail("korisnik@gmail.com");
		korisnikBezStatusa.setLozinka("999");
		
		
		when(osobaRep.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(korisnik));
		when(osobaRep.findById(2l)).thenReturn(Optional.ofNullable(null));
		when(osobaRep.findByKorIme("KorIme1")).thenReturn(korisnik);
		
		admin= new Administrator();
		admin.setKorIme("Admin");
		admin.setIme("AdminIme");
		admin.setPrezime("AdminPrezime");
		admin.setEmail("admin@gmail.com");
		admin.setLozinka("321");
		
		
		verifikator= new Verifikator();
		verifikator.setKorIme("Verifikator");
		verifikator.setIme("VerifikatorIme");
		verifikator.setPrezime("VerifikatorPrezime");
		verifikator.setEmail("verifikator@gmail.com");
		verifikator.setLozinka("555");
		
		
		kondukter= new Kondukter();
		kondukter.setKorIme("Kondukter");
		kondukter.setIme("KondukterIme");
		kondukter.setPrezime("KondukterPrezime");
		kondukter.setEmail("kondukter@gmail.com");
		kondukter.setLozinka("666");
		
		
		ArrayList<Osoba> korisnici= new ArrayList<>();
		korisnici.add(korisnik);
		korisnici.add(admin);
		
		when(osobaRep.findAll()).thenReturn(korisnici);
	
	}
	
	@Test
	public void findOne ()  {
		
		Osoba osoba= osobaServ.findOne(1L);
		assertThat(osoba).isNotNull();
		
		assertEquals(osoba.getKorIme() , "KorIme1");
		assertEquals(osoba.getIme() , "Ime1");
		assertEquals(osoba.getPrezime() , "Prezime1");
		assertEquals(osoba.getEmail() , "korisnik@gmail.com");
		assertEquals(osoba.getLozinka() , "123");
		
		verify(osobaRep, times(1)).findById(1l);		
		
		
	}

	@Test
	public void findAll ()  {
		
		ArrayList<Osoba> korisnici= (ArrayList<Osoba>) osobaServ.findAll();
		assertThat(korisnici).isNotNull();
		
		assertEquals(korisnici.size(), 2);
		verify(osobaRep, times(1)).findAll();		
		
	}
	
	@Test
	public void findOneThatNotExist()  {
		
		Osoba osoba= osobaServ.findOne(2l);
		
		assertEquals(osoba ,null);
	
		verify(osobaRep, times(1)).findById(2l);		
		
		
	}
	@Test
	public void findAllEmptyList ()  {
		when(osobaRep.findAll()).thenReturn(new ArrayList());
		
		ArrayList<Osoba> korisnici= (ArrayList<Osoba>) osobaServ.findAll();
		assertThat(korisnici).isEmpty();
		
		verify(osobaRep, times(1)).findAll();		
		
	}
	@Test
	public void save ()  {
		
		Korisnik korisnik  = new Korisnik();
		korisnik.setKorIme("NoviKorisnik");
		korisnik.setIme("Dragana");
		korisnik.setEmail("dragana.hrecek@gmail");
		korisnik.setPrezime("Hrcek");
		
		osobaServ.save(korisnik);
		
		verify(osobaRep, times(1)).save(korisnik);
		
		
	}
	@Test
	public void findByUsername ()  {
		
		Korisnik kor= (Korisnik) osobaServ.findByUsername("KorIme1");
		assertThat(kor).isNotNull();
		
		assertEquals(kor.getKorIme() , "KorIme1");
		assertEquals(kor.getIme() , "Ime1");
		assertEquals(kor.getPrezime() , "Prezime1");
		assertEquals(kor.getEmail() , "korisnik@gmail.com");
		assertEquals(kor.getLozinka() , "123");
	
		verify(osobaRep, times(1)).findByKorIme("KorIme1");		
		
	}
	
	@Test
	public void delete ()  {
		
		osobaServ.delete(1l);
		
		Osoba kor =  osobaServ.findOne(1l);
		assertEquals(kor.isObrisan() , true);
		
		
		ArgumentCaptor<Osoba> argument1 = ArgumentCaptor.forClass(Osoba.class);
		verify(osobaRep).save(argument1.capture());
		
		
		// jednom se pozove u delete metodi koja se testira, jednom sada u testu  kada trazim korisnika
		ArgumentCaptor<Long> argument2 = ArgumentCaptor.forClass(Long.class);
		verify(osobaRep,times(2)).findById(argument2.capture());
		
		
		
	}
	@Test
	public void createNewUser ()  {
		
		 RegistracijaDTO korDTO = new RegistracijaDTO();
		 korDTO.setKorIme("dragana123");
		 korDTO.setIme("Dragana");
		 korDTO.setPrezime("Hrcek");
		 korDTO.setEmail("dragana.hrcek@gmail.com");
		 korDTO.setLozinka1("12345678");
		 korDTO.setLozinka2("12345678");
		
		osobaServ.createNewUser(korDTO);
		
		
		ArgumentCaptor<Osoba> argument = ArgumentCaptor.forClass(Osoba.class);
		verify(osobaRep).save(argument.capture());
		
		Osoba kor =  argument.getValue();
		assertEquals(kor.getKorIme() , "dragana123");
		assertEquals(kor.getIme() , "Dragana");
		assertEquals(kor.getPrezime() , "Hrcek");
		assertEquals(kor.getEmail() , "dragana.hrcek@gmail.com");
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		assertEquals(encoder.matches("12345678", kor.getLozinka()) ,true);
		
		
	}

	@Test
	public void findUlogovanogKorisnik ()  {
		
		UlogovanDTO ulogovan = osobaServ.findUlogovanog(korisnik);
		assertEquals(ulogovan.getKorIme() , "KorIme1");
		assertEquals(ulogovan.getIme() , "Ime1");
		assertEquals(ulogovan.getPrezime() , "Prezime1");
		assertEquals(ulogovan.getEmail() , "korisnik@gmail.com");
		assertEquals(ulogovan.getStatus() , "STUDENT");
		assertEquals(ulogovan.getUloga() , "KORISNIK");
		
		
		
		
	}
	@Test
	public void findUlogovanogKorisnikBezStatusa ()  {
		
		UlogovanDTO ulogovan = osobaServ.findUlogovanog(korisnikBezStatusa);
		assertEquals(ulogovan.getKorIme() , "KorIme2");
		assertEquals(ulogovan.getIme() , "Ime2");
		assertEquals(ulogovan.getPrezime() , "Prezime2");
		assertEquals(ulogovan.getEmail() , "korisnik@gmail.com");
		assertEquals(ulogovan.getStatus() , null);
		assertEquals(ulogovan.getUloga() , "KORISNIK");
		
		
		
		
	}
	@Test
	public void findUlogovanogAdmin ()  {
		
		UlogovanDTO ulogovan = osobaServ.findUlogovanog(admin);
		assertEquals(ulogovan.getKorIme() , "Admin");
		assertEquals(ulogovan.getIme() , "AdminIme");
		assertEquals(ulogovan.getPrezime() , "AdminPrezime");
		assertEquals(ulogovan.getEmail() , "admin@gmail.com");
		assertEquals(ulogovan.getUloga() , "ADMINISTRATOR");
		
		
		
	}
	@Test
	public void findUlogovanogVerifikator()  {
		
		UlogovanDTO ulogovan = osobaServ.findUlogovanog(verifikator);
		assertEquals(ulogovan.getKorIme() , "Verifikator");
		assertEquals(ulogovan.getIme() , "VerifikatorIme");
		assertEquals(ulogovan.getPrezime() , "VerifikatorPrezime");
		assertEquals(ulogovan.getEmail() , "verifikator@gmail.com");
		assertEquals(ulogovan.getUloga() , "VERIFIKATOR");
		
		
		
	}
	@Test
	public void findUlogovanogKondukter ()  {
		
		UlogovanDTO ulogovan = osobaServ.findUlogovanog(kondukter);
		assertEquals(ulogovan.getKorIme() , "Kondukter");
		assertEquals(ulogovan.getIme() , "KondukterIme");
		assertEquals(ulogovan.getPrezime() , "KondukterPrezime");
		assertEquals(ulogovan.getEmail() , "kondukter@gmail.com");
		assertEquals(ulogovan.getUloga() , "KONDUKTER");
		
		
		
	}
	
}
