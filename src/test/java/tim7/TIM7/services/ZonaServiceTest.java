package tim7.TIM7.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.UpdatedZonaDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.LinijaUZoniRepository;
import tim7.TIM7.repositories.ZonaRepository;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ZonaServiceTest {
	
	@Autowired 
	private ZonaService zonaService;
	
	@MockBean
	private ZonaRepository zonaRepository;
	
	@MockBean
	private LinijaRepository linijaRepository;
	
	@MockBean
	private LinijaUZoniRepository luzRepository;
	
	@MockBean
	private CenovnikRepository cjenovnikRepository;
	
	@Before
	public void setUp() {
		Zona zone = new Zona();
		zone.setId(1L);
		zone.setNaziv("zona");
		zone.setObrisan(false);
		
		Zona zoneDel = new Zona();
		zoneDel.setId(3L);
		zoneDel.setNaziv("zonaDel");
		zoneDel.setObrisan(true);
		
		Linija line = new Linija();
		line.setId(1L);
		line.setNaziv("linija");
		line.setObrisan(false);
		
		/*
		Cenovnik cen = new Cenovnik();
		Calendar now = Calendar.getInstance();
		Date dat = new Date();
		cen.setDatumObjavljivanja(dat);
		Calendar cal = Calendar.getInstance();
		cal.set(2020, 10, 28);
		cen.setDatumIsteka(cal.getTime());
		cen.setObrisan(false);
		StavkaCenovnika sc = new StavkaCenovnika();
		sc.setCenovnik(cen);
		sc.setCena(200);
		Stavka st = new Stavka();
		st.setZona(zone);
		sc.setStavka(st);
		
		ArrayList<Cenovnik> cens = new ArrayList<Cenovnik>();
		cens.add(cen);
		
		when(cjenovnikRepository.findAllByObrisanFalse()).thenReturn(cens);
		when(cjenovnikRepository.findById(1L)).thenReturn(Optional.of(cen));
		*/
		
		when(zonaRepository.findById(1L)).thenReturn(Optional.of(zone));
		when(zonaRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
		when(zonaRepository.findByNaziv("zona")).thenReturn(zone);
		when(zonaRepository.findByNaziv("error")).thenReturn(null);
		when(zonaRepository.findById(3L)).thenReturn(Optional.of(zoneDel));
		
		when(linijaRepository.findById(1L)).thenReturn(Optional.of(line));
		when(linijaRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
	}
	
	//find zone that exists
	@Test
	public void findOneTest1() {
		Zona zone = zonaService.findOne(1L);
		
		assertNotEquals(null, zone);
	}
	
	//find zone that doesn't exist
	@Test
	public void findOneTest2() {
		Zona zone = zonaService.findOne(2L);
		
		assertEquals(null, zone);
	}
	
	//find zone that exists
	@Test
	public void findByNameTest1() {
		Zona retValue = zonaService.findByName("zona");
		
		assertNotEquals(null, retValue);
	}
	
	//find zone that doesn't exist
	@Test
	public void findByNameTest2() {
		Zona retValue = zonaService.findByName("error");
		
		assertEquals(null, retValue);
	}
	
	//add zone with already used name
	@Test
	public void addNewZoneTest1() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(2L);
		uz.setName("zona");
		
		boolean retValue = zonaService.addNewZone(uz);
		assertFalse(retValue);
	}
	
	//add zone successfully
	@Test
	public void addNewZoneTest2() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(2L);
		uz.setName("validZona");
		
		LinijaDTO line = new LinijaDTO();
		line.setId(1L);
		List<LinijaDTO> list = new ArrayList<LinijaDTO>();
		list.add(line);
		uz.setLines(list);
		
		boolean retValue = zonaService.addNewZone(uz);
		
		assertTrue(retValue);
	}
	
	//add zone with line that doesn't exist
	@Test public void addNewZoneTest3() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(2L);
		uz.setName("validZona");
		
		LinijaDTO line = new LinijaDTO();
		line.setId(2L);
		List<LinijaDTO> list = new ArrayList<LinijaDTO>();
		list.add(line);
		uz.setLines(list);
		
		boolean retValue = zonaService.addNewZone(uz);
		
		assertFalse(retValue);	
	}
	
	//add zone successfully without lines
	@Test
	public void addNewZoneTest4() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(2L);
		uz.setName("validZona");

		uz.setLines(new ArrayList<LinijaDTO>());
		
		boolean retValue = zonaService.addNewZone(uz);
		
		assertTrue(retValue);
	}
	
	//update zone that is deleted
	@Test 
	public void updateZoneTest1() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(3L);
		uz.setName("testzone");
		uz.setLines(new ArrayList<LinijaDTO>());
		
		boolean retValue = zonaService.updateZone(uz);
		
		assertFalse(retValue);
	}
	
	//update zone that doesn't exist
	@Test 
	public void updateZoneTest2() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(2L);
		uz.setName("testzone2");
		uz.setLines(new ArrayList<LinijaDTO>());
		
		boolean retValue = zonaService.updateZone(uz);
		
		assertFalse(retValue);
	}
	
	//update zone successfully
	@Test 
	public void updateZoneTest3() {
		UpdatedZonaDTO uz = new UpdatedZonaDTO();
		uz.setId(1L);
		uz.setName("naziv");
		uz.setLines(new ArrayList<LinijaDTO>());
		
		boolean retValue = zonaService.updateZone(uz);
		
		assertTrue(retValue);
	}
	
	//delete zone that doesn't exist
	@Test
	public void deleteZoneTest1() {
		
		boolean retValue = zonaService.deleteZone(2L);
		
		assertFalse(retValue);
	}
	
	//delete zone that is in current Cenovnik
	@Ignore
	@Test 
	public void deleteZoneTest2() {
		boolean retValue = zonaService.deleteZone(1L);
		
		assertFalse(retValue);
	}
}
