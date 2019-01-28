package tim7.TIM7.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tim7.TIM7.dto.VoziloDTO;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.VoziloRepository;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class VoziloServiceTest {
	@Autowired 
	private VoziloService voziloService;
	
	@MockBean
	private VoziloRepository voziloRepository;
	
	@MockBean
	private LinijaRepository linijaRepository;
	
	@Before
	public void setUp() {
		Vozilo vehNoLine = new Vozilo();
		vehNoLine.setId(1L);
		vehNoLine.setObrisan(false);
		vehNoLine.setRegistracija("NS1");
		vehNoLine.setTipVozila(TipVozila.AUTOBUS);
		
		
		
		Linija lineDel = new Linija();
		lineDel.setId(1L);
		lineDel.setNaziv("linija");
		lineDel.setObrisan(true);
		
		Linija line = new Linija();
		line.setId(2L);
		line.setNaziv("linija exists");
		line.setObrisan(false);
		
		
		Vozilo vehWithLine = new Vozilo();
		vehWithLine.setId(5L);
		vehWithLine.setObrisan(false);
		vehWithLine.setRegistracija("NS123");
		vehWithLine.setTipVozila(TipVozila.AUTOBUS);
		vehWithLine.setLinija(line);
		
		List<Vozilo> allVehicles = new ArrayList<Vozilo>();
		allVehicles.add(vehNoLine);
		allVehicles.add(vehWithLine);
		
		
		when(voziloRepository.findById(1L)).thenReturn(Optional.of(vehNoLine));
		when(voziloRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
		when(voziloRepository.findById(5L)).thenReturn(Optional.of(vehWithLine));
		when(voziloRepository.findByRegistracija("error")).thenReturn(null);
		when(voziloRepository.findByRegistracija("NS1")).thenReturn(vehNoLine);
		when(voziloRepository.findAll()).thenReturn(allVehicles);
		
		
		when(linijaRepository.findById(1L)).thenReturn(Optional.of(lineDel));
		when(linijaRepository.findById(2L)).thenReturn(Optional.of(line));
		when(linijaRepository.findById(3L)).thenReturn(null);
	}
	
	//find vehicle that exists
	@Test
	public void findOneTest1() {
		Vozilo retValue = voziloService.findOne(1L);
		
		assertNotEquals(null, retValue);
	}
	
	//find vehicle that doesn't exist
	@Test
	public void findOneTest2() {
		Vozilo retValue = voziloService.findOne(3L);
		
		assertEquals(null, retValue);
	}
	
	//find vehicle that exists
	@Test
	public void findByNameTest1() {
		Vozilo retValue = voziloService.findByName("NS1");
		
		assertNotEquals(null, retValue);
	}
	
	//find vehicle that doesn't exist
	@Test
	public void findByNameTest2() {
		Vozilo retValue = voziloService.findByName("error");
		
		assertEquals(null, retValue);
	}
	
	//add vehicle without line
	@Test
	public void addNewVehicleTest1() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(2L);
		veh.setLineId(null);
		veh.setLineName("");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		
		boolean retValue = voziloService.addNewVehicle(veh);
		
		assertTrue(retValue);
	}
	
	//add vehicle with line
	@Test
	public void addNewVehicleTest2() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(3L);
		veh.setLineId(2L);
		veh.setLineName("linija exists");
		veh.setRegistration("NS12");
		veh.setType("AUTOBUS");
		
		boolean retValue = voziloService.addNewVehicle(veh);
		
		assertTrue(retValue);
	}
	
	//try adding vehicle with an already used registration
	@Test
	public void addNewVehicleTest3() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(1L);
		veh.setLineId(null);
		veh.setLineName("");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		
		boolean retValue = voziloService.addNewVehicle(veh);
		
		assertFalse(retValue);
	}
	
	//add vehicle with a line that's deleted
	@Test
	public void addNewVehicleTest4() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(2L);
		veh.setLineId(1L);
		veh.setLineName("linija");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		
		boolean retValue = voziloService.addNewVehicle(veh);
		
		assertFalse(retValue);
	}
	
	//update vehicle that doesn't exist
	@Test
	public void updateVehicleTest1() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(2L);
		veh.setLineId(null);
		veh.setLineName("");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		boolean retValue = voziloService.updateVehicle(veh);
		
		assertFalse(retValue);
	}
	
	//update vehicle that exists
	@Test
	public void updateVehicleTest2() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(1L);
		veh.setLineId(null);
		veh.setLineName("");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		boolean retValue = voziloService.updateVehicle(veh);
		
		assertTrue(retValue);
	}
	
	//update vehicle with deleted line
	@Test
	public void updateVehicleTest3() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(1L);
		veh.setLineId(1L);
		veh.setLineName("linija");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		boolean retValue = voziloService.updateVehicle(veh);
		
		assertFalse(retValue);
	}
	
	//update vehicle with existing line
	@Test
	public void updateVehicleTest4() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(1L);
		veh.setLineId(2L);
		veh.setLineName("linija exists");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		boolean retValue = voziloService.updateVehicle(veh);
		
		assertTrue(retValue);
	}
	
	//update vehicle with non existent line
	@Test
	public void updateVehicleTest5() {
		VoziloDTO veh = new VoziloDTO();
		veh.setId(1L);
		veh.setLineId(3L);
		veh.setLineName("linija");
		veh.setRegistration("NS11");
		veh.setType("AUTOBUS");
		boolean retValue = voziloService.updateVehicle(veh);
		
		assertFalse(retValue);
	}
	
	//delete vehicle that exists and isn't used currently
	@Test
	public void deleteVehicleTest1() {
		
		boolean retValue = voziloService.deleteVehicle(1L);
		
		assertTrue(retValue);
	}
	
	//delete vehicle that doesn't exist
	@Test
	public void deleteVehicleTest2() {
		
		boolean retValue = voziloService.deleteVehicle(2L);
		
		assertFalse(retValue);
	}
	
	//delete vehicle that's currently used
	@Test
	public void deleteVehicleTest3() {
		
		boolean retValue = voziloService.deleteVehicle(5L);
		
		assertFalse(retValue);
	}
	
	//get all vehicles
	@Test 
	public void getAllVehiclesTest1() {
		List<VoziloDTO> retValue = voziloService.getAllVehicles();
		
		assertEquals(2, retValue.size());
	}
		

}
