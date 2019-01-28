
package tim7.TIM7.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.TIM7.dto.CenovnikDTO;
import tim7.TIM7.dto.StavkaCenovnikaDto;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CenovnikServiceTest {

	@Autowired
	private StavkaService kartaServ;
	
	@Autowired
	private CenovnikService cenovnikService;
	
	@MockBean
	private CenovnikRepository cenovnikRep;
	
	@MockBean
	private StavkaCenovnikaService stavkaService;
	
	@MockBean
	private StavkaService staService;
	
	@MockBean
	private StavkaCenovnika osobaService;
	
	@MockBean
	private ZonaService zonaService;
	
	@MockBean
	private LinijaService linijaService;
	
	private Cenovnik prosli, buduci, sadasnji;	
	private List<Cenovnik> cenovnici ;	
	private StavkaCenovnikaDto stavkaDto;	
	private StavkaCenovnika stavka;
	private Stavka s;

	@Before
	public void setUp() {
		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();

		before.set(2018, 9, 1);
		after.set(2019, 0, 1);
		 prosli = new Cenovnik(before.getTime(), after.getTime(), new ArrayList<>(),false, 10,20, 10 ,20);
		
		Cenovnik prosliObrisan = new Cenovnik(before.getTime(), after.getTime(), new ArrayList<>(),true, 10,20, 10 ,20);

		before.setTime(after.getTime());
		after.set(2019, 9, 1);
		 sadasnji = new Cenovnik(before.getTime(), after.getTime(),new ArrayList<>(), false, 20,10,30,10);

		before.setTime(after.getTime());
		after.set(2022, 9, 1);
		 buduci = new Cenovnik(before.getTime(), after.getTime(),new ArrayList<>(), false, 20,30,20,30);

		cenovnici = new ArrayList<Cenovnik>();
		cenovnici.add(buduci);
		cenovnici.add(prosli);
		cenovnici.add(sadasnji);
		
		List<Cenovnik> svi = new ArrayList<Cenovnik>();
		svi.add(buduci);
		svi.add(prosli);
		svi.add(sadasnji);
		svi.add(prosliObrisan);
		
		CenovnikDTO budDTO = new CenovnikDTO(buduci);
		
		buduci.setId(1l);
		sadasnji.setId(2l);
		prosli.setId(3l);
		prosliObrisan.setId(4l);
		
		when(cenovnikRep.getById(2000l)).thenReturn(prosli);
		when(cenovnikRep.save(prosli)).thenReturn(prosli);
		when(cenovnikRep.save(buduci)).thenReturn(buduci);
		when(cenovnikRep.save(sadasnji)).thenReturn(sadasnji);
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(cenovnici);
		when(cenovnikRep.findAll()).thenReturn(svi);
		
		
		Linija l= new Linija("linija1", false);
		Zona z = null;
		
		when(linijaService.findByName("linija1")).thenReturn(l);
		//when(zonaService.findByName()).thenReturn(z);
		
		stavka= new StavkaCenovnika(200.0, l, z, TipKarteCenovnik.GODISNJA, TipVozila.AUTOBUS);
		stavkaDto = new StavkaCenovnikaDto(stavka);
		
		 s = new Stavka(TipKarteCenovnik.GODISNJA, TipVozila.AUTOBUS, z, l,false);
		when(staService.findByDto(stavkaDto)).thenReturn(s);

	}

	@Test
	public void findAll() {
		ArrayList<Cenovnik> cenovnikList = (ArrayList<Cenovnik>) cenovnikService.findAll();
		assertThat(cenovnikList).isNotNull();

		assertEquals(cenovnikList.size(), 4);
		verify(cenovnikRep, times(1)).findAll();
		
	}
	
	@Test
	public void addCenovnikFailExsists() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		
		Calendar before = Calendar.getInstance();
		before.set(2018, 9, 1);
		CenovnikDTO dto = new CenovnikDTO(prosli);

		dto.getStavkeCenovnika().add(stavkaDto);
		String response = cenovnikService.addCenovnik(dto);		
		assertEquals(response,"Greska! Postoji vec cenovnik sa tim datumom objavljivanja");
		
	
	}
	
	@Test
	public void addCenovnikFailDate() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		when(cenovnikRep.save(prosli)).thenReturn(prosli);
		
		Calendar before = Calendar.getInstance();
		CenovnikDTO dto = new CenovnikDTO(prosli);

		dto.getStavkeCenovnika().add(stavkaDto);
		before.set(2008,9, 1);
		dto.setDatumObjavljivanja(before.getTime());
		String response = cenovnikService.addCenovnik(dto);		
		assertEquals(response,"Greska! Datum objavljivanja mora biti bar dva dana od sad");
		
	}
	
	@Test
	public void addCenovnikFailPopustNull() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		
		Calendar before = Calendar.getInstance();
		CenovnikDTO dto = new CenovnikDTO(prosli);
		dto.getStavkeCenovnika().add(stavkaDto);

		before.set(2088,9, 1);
		dto.setPopustDjak(null);
		dto.setDatumObjavljivanja(before.getTime());
		String response = cenovnikService.addCenovnik(dto);
		assertEquals(response,"Greska! Popusti moraju biti izmedju 0 i 100");			
	}
	
	@Test
	public void addCenovnikFailPopust() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		
		Calendar before = Calendar.getInstance();
		CenovnikDTO dto = new CenovnikDTO(prosli);
		dto.getStavkeCenovnika().add(stavkaDto);

		before.set(2088,9, 1);
		dto.setDatumObjavljivanja(before.getTime());		
		dto.setPopustDjak(2222);
		String response = cenovnikService.addCenovnik(dto);
		assertEquals(response,"Greska! Popusti moraju biti izmedju 0 i 100");	
	}

	
	@Test
	public void addCenovnikSuccess() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		when(cenovnikRep.save(prosli)).thenReturn(prosli);

		Calendar before = Calendar.getInstance();
		CenovnikDTO dto = new CenovnikDTO(prosli);
		dto.getStavkeCenovnika().add(stavkaDto);

		before.set(2088,9, 1);
		dto.setDatumObjavljivanja(before.getTime());		
		dto.setPopustDjak(2);
		String response = cenovnikService.addCenovnik(dto);
		assertEquals(response,"Cenovnik je uspesno kreiran");
	
	}
	
	@Test
	public void addCenovnikFailStavka() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		when(cenovnikRep.save(prosli)).thenReturn(prosli);
		when(staService.findByDto(stavkaDto)).thenReturn(null);

		Calendar before = Calendar.getInstance();
		CenovnikDTO dto = new CenovnikDTO(prosli);
		dto.getStavkeCenovnika().add(stavkaDto);

		before.set(2088,9, 1);
		dto.setDatumObjavljivanja(before.getTime());		
		
		stavkaDto.setCena(null);
		String response = cenovnikService.addCenovnik(dto);
		assertEquals(response, "Greska! Nije moguce dodati ove stavke");
		
	}
	
	@Test
	public void addCenovnikFailStavka2() {
		List<Cenovnik> buduciCen = new ArrayList<Cenovnik>();
		buduciCen.add(buduci);
		buduciCen.add(prosli);
		buduciCen.add(sadasnji);
		
		when(cenovnikRep.findAllByObrisanFalse()).thenReturn(buduciCen);
		when(cenovnikRep.save(prosli)).thenReturn(prosli);

		Calendar before = Calendar.getInstance();
		CenovnikDTO dto = new CenovnikDTO(prosli);
		dto.getStavkeCenovnika().add(stavkaDto);

		before.set(2088,9, 1);
		dto.setDatumObjavljivanja(before.getTime());		
		
		stavkaDto.setCena(null);
		
		when(staService.findByDto(stavkaDto)).thenReturn(s);

		String response = cenovnikService.addCenovnik(dto);
		assertEquals(response, "Greska! Nije moguce dodati ove stavke");
		
	}

}