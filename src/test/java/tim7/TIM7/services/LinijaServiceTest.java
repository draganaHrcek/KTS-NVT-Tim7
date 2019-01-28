package tim7.TIM7.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.TIM7.model.Linija;
import tim7.TIM7.repositories.LinijaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class LinijaServiceTest {

	@Autowired
	LinijaService linijaService;
	
	@MockBean
	LinijaRepository linijaRepository;
	
	@Before
	public void setUp() {
		Linija line = new Linija();
		line.setId(1L);
		line.setNaziv("linija");
		line.setObrisan(false);
		
		when(linijaRepository.findById(1L)).thenReturn(Optional.of(line));
		when(linijaRepository.findByNaziv("linija")).thenReturn(line);
	}
	
	//find line that exists
	@Test
	public void findOneTest1() {
		Linija line = linijaService.findOne(1L);
		
		assertNotEquals(null, line);
	}
	
	//find line that doesn't exist
	@Test
	public void findOneTest2() {
		Linija line = linijaService.findOne(2L);
		
		assertEquals(null, line);
	}
	
	//find line that exists
	@Test
	public void findByNameTest1() {
		Linija line = linijaService.findByName("linija");
		
		assertNotEquals(null, line);
	}
	
	//find line that doesn't exist
	@Test
	public void findByNameTest2() {
		Linija line = linijaService.findByName("error");
		
		assertEquals(null, line);
	}
}
