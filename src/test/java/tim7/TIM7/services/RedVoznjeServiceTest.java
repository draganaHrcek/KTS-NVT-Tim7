package tim7.TIM7.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.TIM7.dto.RedVoznjeDTO;
import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.RasporedVoznjeRepository;
import tim7.TIM7.repositories.RedVoznjeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RedVoznjeServiceTest {
	@Autowired
	RedVoznjeService redVoznjeService;
	
	@MockBean
	RasporedVoznjeRepository rasporedVoznjeRepository;
	
	@MockBean
	LinijaRepository linijaRepository;
	
	@MockBean
	RedVoznjeRepository redVoznjeRepository;
	
	
	private RedVoznje trenutniRedVoznje;
	private RedVoznje buduciRedVoznje;
	
	@Before
	public void setUp() {
		trenutniRedVoznje=new RedVoznje();
		Calendar calendar=Calendar.getInstance();
		calendar.set(2019, 0, 7);
		trenutniRedVoznje.setDatumObjavljivanja(calendar.getTime());
		trenutniRedVoznje.setObrisan(false);
		RasporedVoznje trenutniRaspored1 = new RasporedVoznje();
		RasporedVoznje trenutniRaspored2 = new RasporedVoznje();
		
		
		Linija linija1=new Linija("NazivLinije1", false);
		Linija linija2=new Linija("NazivLinije2", false);
		
		trenutniRaspored1.setDanUNedelji(DanUNedelji.RADNI);
		trenutniRaspored1.setLinija(linija1);
		trenutniRaspored1.setObrisan(false);
		List<LocalTime> vremena=new ArrayList<LocalTime>();
		vremena.add(LocalTime.of(12, 30));
		vremena.add(LocalTime.of(12, 50));		
		trenutniRaspored1.setVremena(vremena);		
		
		trenutniRaspored2.setDanUNedelji(DanUNedelji.RADNI);
		trenutniRaspored2.setLinija(linija2);
		trenutniRaspored2.setObrisan(false);
		List<LocalTime> vremena2=new ArrayList<LocalTime>();
		vremena2.add(LocalTime.of(12, 30));
		vremena2.add(LocalTime.of(12, 50));		
		trenutniRaspored1.setVremena(vremena2);	
		
		ArrayList<RasporedVoznje> rasporediVoznje = new ArrayList<RasporedVoznje>();
		rasporediVoznje.add(trenutniRaspored1);
		rasporediVoznje.add(trenutniRaspored2);
		
		trenutniRedVoznje.setRasporediVoznje(rasporediVoznje);
		
		ArrayList<RedVoznje> redoviVoznje=new ArrayList<RedVoznje>();
		redoviVoznje.add(trenutniRedVoznje);

		when(redVoznjeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(trenutniRedVoznje));


		when(redVoznjeRepository.findById((long) 1000)).thenReturn(Optional.ofNullable(null));
		
		

	}
	
	
	@Test
	public void findById() { //

		RedVoznje redVoznje = (RedVoznje) redVoznjeService.findById(1L);
		assertThat(redVoznje).isNotNull();

		
		assertEquals(redVoznje.getRasporediVoznje().size(),2);

		verify(redVoznjeRepository, times(1)).findById(1l);
	}
	
	
	@Test
	public void findAll() {
		ArrayList<RedVoznje> redoviVoznje=new ArrayList<RedVoznje>();
		redoviVoznje.add(trenutniRedVoznje);
		redoviVoznje.add(buduciRedVoznje);
		when(redVoznjeRepository.findAll()).thenReturn(redoviVoznje);
		
		ArrayList<RedVoznje> redovi = (ArrayList<RedVoznje>) redVoznjeRepository.findAll();
		assertThat(redovi).isNotNull();

		assertEquals(redovi.size(), 2);
		verify(redVoznjeRepository, times(1)).findAll();

	}
	
	@Test
	public void save() {
		RedVoznje redVoznje=new RedVoznje();
		Calendar calendar = Calendar.getInstance();
		redVoznje.setDatumObjavljivanja(calendar.getTime());
		redVoznje.setObrisan(true);
		
		redVoznjeRepository.save(redVoznje);

		verify(redVoznjeRepository, times(1)).save(redVoznje);

	}
	
	
	@Test
	public void delete() {
		RedVoznje neobrisan=new RedVoznje(false,Calendar.getInstance().getTime());
		when(redVoznjeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(neobrisan));
		redVoznjeService.delete(1L);

		RedVoznje red = redVoznjeService.findById(1L);
		assertTrue(red.isObrisan());

	}
	
	
	@Test
	public void getTrenutniRedVoznjeImaTrenutniNemaBuduci() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2019, 0,20);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getTrenutniRedVoznje();
		assertNotNull(redVoznjeDto);
		

	}
	
	
	@Test
	public void getTrenutniRedVoznjeNemaTrenutniNemaBuduci() {
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getTrenutniRedVoznje();
		assertNull(redVoznjeDto);

	}
	
	@Test
	public void getTrenutniRedVoznjeImaTrenutniImaBuduciNeMenjaSe() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2019, 0,20);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());
		calendar.set(2019, 1,21);
		RedVoznje redVoznje2 = new RedVoznje(false,calendar.getTime());
		
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		neobrisani.add(redVoznje2);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getTrenutniRedVoznje();
		assertNotNull(redVoznjeDto);
		assertEquals(redVoznje.getDatumObjavljivanja(),redVoznjeDto.getDatumObjavljivanja());

	}
	
	
	@Test
	public void getTrenutniRedVoznjeNemaTrenutniImaBuduciNeMenjaSe() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2019, 3,20);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());	
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getTrenutniRedVoznje();
		assertNull(redVoznjeDto);

	}
	
	@Test
	public void getTrenutniRedVoznjeImaTrenutniImaBuduciMenjaSe() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2018, 11,4);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());
		
		calendar.set(2019, 0,4);
		RedVoznje redVoznje2 = new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		neobrisani.add(redVoznje2);
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getTrenutniRedVoznje();
		assertNotNull(redVoznjeDto);
		assertEquals(redVoznje2.getDatumObjavljivanja(),redVoznjeDto.getDatumObjavljivanja());

	}
	
	
	@Test
	public void getBuduciRedVoznjeNemaTrenutniNemaBuduci() {
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getBuduciRedVoznje();
		assertNull(redVoznjeDto);

	}
	
	
	@Test
	public void getBuduciRedVoznjeImaTrenutniNemaBuduci() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2018, 11,4);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getBuduciRedVoznje();
		assertNull(redVoznjeDto);

	}
	
	@Test
	public void getBuduciRedVoznjeNemaTrenutniImaBuduci() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2019, 11,4);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getBuduciRedVoznje();
		assertNotNull(redVoznjeDto);

	}
	
	
	@Test
	public void getBuduciRedVoznjeImaTrenutniImaBuduci() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2018, 11,4);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());		
		
		calendar.set(2019, 11,4);
		RedVoznje redVoznje2 = new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		neobrisani.add(redVoznje2);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO redVoznjeDto=redVoznjeService.getBuduciRedVoznje();
		assertNotNull(redVoznjeDto);
		assertEquals(redVoznje2.getDatumObjavljivanja(),redVoznjeDto.getDatumObjavljivanja());
	}
	
	
	@Test
	public void createRedVoznjeNemaBuduciDobarDatum() {
		Calendar calendar=Calendar.getInstance();
		calendar.set(2019, 11,4);
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		RedVoznjeDTO red = redVoznjeService.createRedVoznje(calendar.getTime());
		assertNotNull(red);
		assertEquals(calendar.getTime(),red.getDatumObjavljivanja());
		
		
	}
	
	@Test
	public void createRedVoznjeImaBuduciDobarDatum() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 11,4);
		RedVoznje redVoznje = new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		
		calendar.set(2019, 11,1);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO red = redVoznjeService.createRedVoznje(calendar.getTime());
		assertNull(red);
		
		
	}
	
	
	@Test
	public void createRedVoznjeNemaBuduciLosDatum() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 0,2);
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		RedVoznjeDTO red = redVoznjeService.createRedVoznje(calendar.getTime());
		assertNull(red);
		
		
	}
	
	
	@Test
	public void createRedVoznjeImaBuduciLosDatum() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 5,2);
		RedVoznje redVoznje=new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		calendar.set(2018, 9, 12);
		RedVoznjeDTO red = redVoznjeService.createRedVoznje(calendar.getTime());
		assertNull(red);
		
		
	}
	
	
	@Test
	public void deleteBuduciRedVoznjePostoji() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 5,2);
		RedVoznje redVoznje=new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		calendar.set(2018, 9, 12);
		String status = redVoznjeService.deleteBuduciRedVoznje();
		assertEquals("OBRISAN", status);
		
		
	}
	
	@Test
	public void deleteBuduciRedVoznjeNePostoji() {		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		String status = redVoznjeService.deleteBuduciRedVoznje();
		assertEquals("NE POSTOJI", status);
		
		
	}
	
	@Test
	public void changeBuduciRedVoznjeNePostojiDobarDatum() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 5,2);
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		String status = redVoznjeService.changeBuduciRedVoznje(calendar.getTime());
		assertEquals("NE POSTOJI", status);
		
		
	}
	
	@Test
	public void changeBuduciRedVoznjePostojiDobarDatum() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 5,2);
		RedVoznje redVoznje=new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		calendar.set(2019,5,10);
		String status = redVoznjeService.changeBuduciRedVoznje(calendar.getTime());
		assertEquals("IZMENJEN", status);	
	}
	
	@Test
	public void changeBuduciRedVoznjeNePostojiLosDatum() {
		
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
		
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2018,5,10);
		String status = redVoznjeService.changeBuduciRedVoznje(calendar.getTime());
		assertEquals("NE POSTOJI", status);	
	}
	
	
	@Test
	public void changeBuduciRedVoznjePostojiLosDatum() {
		Calendar calendar=Calendar.getInstance();		
		calendar.set(2019, 5,2);
		RedVoznje redVoznje=new RedVoznje(false,calendar.getTime());
		
		ArrayList<RedVoznje> neobrisani = new ArrayList<RedVoznje>();
		neobrisani.add(redVoznje);
		
		when(redVoznjeRepository.findByObrisanFalse()).thenReturn(neobrisani);
				
		calendar.set(2018,5,10);
		String status = redVoznjeService.changeBuduciRedVoznje(calendar.getTime());
		assertEquals("LOS DATUM", status);	
	}
	
	
}
