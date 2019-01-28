package tim7.TIM7.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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

import tim7.TIM7.dto.StanicaDTO;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.repositories.StanicaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class StanicaServiceTest {
	
	@Autowired
	StanicaService stanicaService;
	
	@MockBean
	StanicaRepository stanicaRepository;
	
	@Before
	public void setUp() {
		
		Stanica station = new Stanica();
		station.setId(1L);
		station.setObrisan(false);
		station.setOznaka("stanica1");
		
		when(stanicaRepository.findById(1L)).thenReturn(Optional.of(station));
		when(stanicaRepository.findByOznaka("stanica1")).thenReturn(station);
		
	}
	
	//find station that exists
	@Test
	public void findOneTest1() {
		Stanica retValue = stanicaService.findOne(1L);
		
		assertNotEquals(null, retValue);
	}
	
	//find station that doesn't exist
	@Test
	public void findOneTest2() {
		Stanica retValue = stanicaService.findOne(2L);
		
		assertEquals(null, retValue);
	}
	
	//find station that exists
	@Test
	public void findByNameTest1() {
		Stanica retValue = stanicaService.findByName("stanica1");
		
		assertNotEquals(null, retValue);
	}
	
	//find station that doesn't exist
	@Test
	public void findByNameTest2() {
		Stanica retValue = stanicaService.findByName("error");
		
		assertEquals(null, retValue);
	}
	
	//add station with already used name
	@Test
	public void addNewStationTest1() {
		StanicaDTO st = new StanicaDTO();
		st.setId(3L);
		st.setName("stanica1");
		
		boolean retValue = stanicaService.addNewStation(st);
		
		assertFalse(retValue);
	}
	
	// add station successfully
	@Test
	public void addNewStationTest2() {
		StanicaDTO st = new StanicaDTO();
		st.setId(3L);
		st.setName("stanicanova");
		
		boolean retValue = stanicaService.addNewStation(st);
		
		assertTrue(retValue);
	}
	

}
