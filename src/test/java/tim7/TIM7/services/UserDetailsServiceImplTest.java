package tim7.TIM7.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Kondukter;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Verifikator;
import tim7.TIM7.repositories.OsobaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserDetailsServiceImplTest {

	@Autowired
	private UserDetailsService userDetailsService;

	@MockBean
	private OsobaRepository userRepository;

	@Before
	public void setUp() {
		Korisnik korisnik = new Korisnik();
		korisnik.setKorIme("korisnik");
		korisnik.setLozinka("test");
		when(userRepository.findByKorIme("korisnik")).thenReturn(korisnik);

		Administrator administrator = new Administrator();
		administrator.setKorIme("administrator");
		administrator.setLozinka("test");
		when(userRepository.findByKorIme("administrator")).thenReturn(administrator);

		Verifikator verifikator = new Verifikator();
		verifikator.setKorIme("verifikator");
		verifikator.setLozinka("test");
		when(userRepository.findByKorIme("verifikator")).thenReturn(verifikator);

		Kondukter kondukter = new Kondukter();
		kondukter.setKorIme("kondukter");
		kondukter.setLozinka("test");
		when(userRepository.findByKorIme("kondukter")).thenReturn(kondukter);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void nepostojeciKorisnikTest() {
		userDetailsService.loadUserByUsername("nepostojeci");

	}
	
	@Test
	public void korisnikTest() {
		UserDetails user =  userDetailsService.loadUserByUsername("korisnik");
		assertEquals(user.getUsername(),"korisnik");
		assertEquals(user.getPassword(),"test");
		assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("KORISNIK")));
	}
	
	@Test
	public void administratorTest() {
		UserDetails user =  userDetailsService.loadUserByUsername("administrator");
		assertEquals(user.getUsername(),"administrator");
		assertEquals(user.getPassword(),"test");
		assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
	}
	
	@Test
	public void verifikatorTest() {
		UserDetails user =  userDetailsService.loadUserByUsername("verifikator");
		assertEquals(user.getUsername(),"verifikator");
		assertEquals(user.getPassword(),"test");
		assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("VERIFIKATOR")));
	}
	
	@Test
	public void kondukterTest() {
		UserDetails user =  userDetailsService.loadUserByUsername("kondukter");
		assertEquals(user.getUsername(),"kondukter");
		assertEquals(user.getPassword(),"test");
		assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("KONDUKTER")));
	}

}
