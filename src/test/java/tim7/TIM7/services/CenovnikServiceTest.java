
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
	
	

//	@Test
//	public void findByKod() {
//
//		DnevnaKarta karta = (DnevnaKarta) kartaServ.findByKod("1se45nx4");
//		assertThat(karta).isNotNull();
//
//		assertEquals(karta.getCena(), new Double(60.0));
//		assertEquals(karta.getKod(), "1se45nx4");
//		assertEquals(karta.getTipPrevoza(), TipVozila.valueOf("AUTOBUS"));
//		assertEquals(karta.getLinija().getNaziv(), "linija1");
//
//		verify(kartaRep, times(1)).findByKod("1se45nx4");
//	}
//	
//	
//	@Test
//	public void findByKodNotExist() {
//
//		DnevnaKarta karta = (DnevnaKarta) kartaServ.findByKod("11111111");
//		assertThat(karta).isNull();
//
//
//		verify(kartaRep, times(1)).findByKod("11111111");
//	}
//	@Test
//	public void fiqndAll() {
//
//		ArrayList<Karta> karte = new ArrayList<Karta>();
//		karte.add(karta1);
//		karte.add(karta2);
//		when(kartaRep.findAll()).thenReturn(karte);
//
//		ArrayList<Karta> k = (ArrayList<Karta>) kartaServ.findAll();
//		assertThat(k).isNotNull();
//
//		assertEquals(k.size(), 2);
//		verify(kartaRep, times(1)).findAll();
//
//	}
//
//	@Test
//	public void findOneThatNotExist() {
//
//		Karta karta = kartaServ.findOne(2000l);
//
//		assertEquals(karta, null);
//
//		verify(kartaRep, times(1)).findById(2000l);
//
//	}
//
//	@Test
//	public void findAllEmptyList() {
//		when(kartaRep.findAll()).thenReturn(new ArrayList());
//
//		ArrayList<Karta> korisnici = (ArrayList<Karta>) kartaRep.findAll();
//		assertThat(korisnici).isEmpty();
//
//		verify(kartaRep, times(1)).findAll();
//
//	}
//
//	@Test
//	public void save() {
//
//		DnevnaKarta karta = new DnevnaKarta();
//		karta.setKod("2cn6dzzl");
//		karta.setCena(50.0);
//		karta1.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));
//
//		kartaServ.save(karta);
//
//		verify(kartaRep, times(1)).save(karta);
//
//	}
//
//	@Test
//	public void delete() {
//
//		kartaServ.delete(1l);
//
//		Karta k = kartaServ.findOne(1l);
//		assertEquals(k.isObrisan(), true);
//
//	}
//
//	@Test
//	public void findAllUserTicketsEmptyList() {
//
//		Korisnik kor = new Korisnik();
//		kor.setKarte(new ArrayList<Karta>());
//
//		ArrayList<KartaDTO> karte = kartaServ.findAllUserTickets(kor);
//
//		assertThat(karte).isEmpty();
//
//	}
//
//	@Test
//	public void findAllUserTickets() {
//
//		ArrayList<Karta> karte = new ArrayList<Karta>();
//		karte.add(karta1);
//		karte.add(karta2);
//
//		Korisnik kor = new Korisnik();
//		kor.setKarte(karte);
//
//		ArrayList<KartaDTO> karteDTO = kartaServ.findAllUserTickets(kor);
//
//		assertThat(karteDTO).isNotEmpty();
//		assertEquals(karteDTO.size(), 2);
//
//		// provera da li se DTO objekti poklapaju sa kartama korisnika
//
//		boolean dnevna = false;
//		boolean godisnja = false;
//
//		for (KartaDTO i : karteDTO) {
//			if (i.getTipKarte().equals("DNEVNA")) {
//				if (i.getKod().equals("1se45nx4") && i.getCena() == 60.0 && i.getTipPrevoza().equals("AUTOBUS")
//						&& i.getLinijaZona().equals("linija1")) {
//					dnevna = true;
//
//				}
//			} else if (i.getTipKarte().equals("GODISNJA")) {
//				if (i.getKod().equals("6b33za7g") && i.getCena() == 2000.0 && i.getTipPrevoza().equals("METRO")
//						&& i.getLinijaZona().equals("zona1") && i.getStatusKorisnika().equals("STUDENT")) {
//					godisnja = true;
//
//				}
//			}
//		}
//
//		assertEquals(dnevna, true);
//		assertEquals(godisnja, true);
//
//	}
//
//	@Test
//	public void cenaKarteDnevna() {
//
//		Korisnik korisnik = new Korisnik();
//
//		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());
//
//		KartaDTO kartaDTO = new KartaDTO();
//		kartaDTO.setTipPrevoza("AUTOBUS");
//		kartaDTO.setTipKarte("DNEVNA");
//		kartaDTO.setLinijaZona("linija1");
//
//		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);
//
//		assertEquals(cena, new Double(50.0));
//
//	}
//
//	@Test
//	public void cenaKarteStudentskaVisednevna() {
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setStatus(StatusKorisnika.STUDENT);
//
//		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());
//
//		KartaDTO kartaDTO = new KartaDTO();
//		kartaDTO.setTipPrevoza("TRAMVAJ");
//		kartaDTO.setTipKarte("GODISNJA");
//		kartaDTO.setLinijaZona("prigradska");
//
//		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);
//
//		assertEquals(cena, new Double(1000 * (100 - 20) / 100));
//
//	}
//
//	@Test
//	public void cenaKartePenzionerskaVisednevna() {
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setStatus(StatusKorisnika.PENZIONER);
//
//		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());
//
//		KartaDTO kartaDTO = new KartaDTO();
//		kartaDTO.setTipPrevoza("TRAMVAJ");
//		kartaDTO.setTipKarte("GODISNJA");
//		kartaDTO.setLinijaZona("prigradska");
//
//		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);
//
//		assertEquals(cena, new Double(1000 * (100 - 30) / 100));
//
//	}
//
//	@Test
//	public void cenaKarteDjakVisednevna() {
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setStatus(StatusKorisnika.DJAK);
//
//		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());
//
//		KartaDTO kartaDTO = new KartaDTO();
//		kartaDTO.setTipPrevoza("TRAMVAJ");
//		kartaDTO.setTipKarte("GODISNJA");
//		kartaDTO.setLinijaZona("prigradska");
//
//		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);
//
//		assertEquals(cena, new Double(1000 * (100 - 10) / 100));
//
//	}
//
//	@Test
//	public void cenaKarteNezaposlenVisednevna() {
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setStatus(StatusKorisnika.NEZAPOSLEN);
//
//		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());
//
//		KartaDTO kartaDTO = new KartaDTO();
//		kartaDTO.setTipPrevoza("TRAMVAJ");
//		kartaDTO.setTipKarte("GODISNJA");
//		kartaDTO.setLinijaZona("prigradska");
//
//		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);
//
//		assertEquals(cena, new Double(1000 * (100 - 25) / 100));
//
//	}
//
//	@Test
//	public void cenaKarteVisednevnaZaposleni() {
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setStatus(StatusKorisnika.RADNIK);
//
//		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());
//
//		KartaDTO kartaDTO = new KartaDTO();
//		kartaDTO.setTipPrevoza("TRAMVAJ");
//		kartaDTO.setTipKarte("GODISNJA");
//		kartaDTO.setLinijaZona("prigradska");
//
//		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);
//
//		assertEquals(cena, new Double(1000));
//
//	}
//	@Test
//	public void createNewTicketDnevna() {
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setKarte(new ArrayList<Karta>());
//		korisnik.setIme("Dragana");
//		korisnik.setPrezime("Hrcek");
//		
//		KartaDTO kartaDTO= new KartaDTO();
//		kartaDTO.setTipKarte("DNEVNA");
//		kartaDTO.setTipPrevoza("AUTOBUS");
//		kartaDTO.setLinijaZona("linija1");
//		
//		Linija linija = new Linija();
//		linija.setNaziv("linija1");
//		when(linijaServ.findByName(Matchers.any())).thenReturn(linija);
//
//		kartaServ.createNewTicket(kartaDTO, korisnik, 50.0);
//
//		ArgumentCaptor<DnevnaKarta> argument1 = ArgumentCaptor.forClass(DnevnaKarta.class);
//		verify(kartaRep, atLeast(1)).save(argument1.capture());
//		
//		
//		assertEquals(argument1.getValue().getCena(),new Double(50.0));
//		assertEquals(argument1.getValue().getDatumIsteka(),Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toInstant(ZoneOffset.UTC)));
//		assertEquals(argument1.getValue().getKorisnik(),korisnik);
//		assertEquals(argument1.getValue().getTipPrevoza(),TipVozila.AUTOBUS);
//		assertThat(argument1.getValue().getKod()).isNotNull();
//		assertEquals(argument1.getValue().getLinija().getNaziv(),"linija1");
//		
//		
//		verify(osobaService, times(1)).save(korisnik);
//		
//
//	
//
//
//	}
//	@Test
//	public void createNewTicketMesecna() {
//		LocalDate cd = LocalDate.now();
//		
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setKarte(new ArrayList<Karta>());
//		korisnik.setIme("Dragana");
//		korisnik.setPrezime("Hrcek");
//	
//		
//		KartaDTO kartaDTO= new KartaDTO();
//		kartaDTO.setTipKarte("MESECNA");
//		kartaDTO.setTipPrevoza("AUTOBUS");
//		kartaDTO.setLinijaZona("zona1");
//		kartaDTO.setStatusKorisnika("STUDENT");
//		
//		Zona zona = new Zona();
//		zona.setNaziv("zona1");
//		when(zonaServ.findByName(Matchers.any())).thenReturn(zona);
//
//		kartaServ.createNewTicket(kartaDTO, korisnik, 1000.0);
//
//		ArgumentCaptor<VisednevnaKarta> argument1 = ArgumentCaptor.forClass(VisednevnaKarta.class);
//		verify(kartaRep, atLeast(1)).save(argument1.capture());
//		
//		
//		assertEquals(argument1.getValue().getCena(),new Double(1000.0));
//		assertEquals(argument1.getValue().getDatumIsteka(),Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//		assertEquals(argument1.getValue().getKorisnik(),korisnik);
//		assertEquals(argument1.getValue().getTipPrevoza(),TipVozila.AUTOBUS);
//		assertThat(argument1.getValue().getKod()).isNotNull();
//		assertEquals(argument1.getValue().getZona().getNaziv(),"zona1");
//		assertEquals(argument1.getValue().getTipKorisnika(),StatusKorisnika.STUDENT);
//		
//		
//		verify(osobaService, times(1)).save(korisnik);
//		
//
//	
//
//
//	}
//	
//	@Test
//	public void createNewTicketGodisnja() {
//		LocalDate cd = LocalDate.now();
//		
//
//		Korisnik korisnik = new Korisnik();
//		korisnik.setKarte(new ArrayList<Karta>());
//		korisnik.setIme("Dragana");
//		korisnik.setPrezime("Hrcek");
//	
//		
//		KartaDTO kartaDTO= new KartaDTO();
//		kartaDTO.setTipKarte("GODISNJA");
//		kartaDTO.setTipPrevoza("AUTOBUS");
//		kartaDTO.setLinijaZona("zona1");
//		kartaDTO.setStatusKorisnika("PENZIONER");
//		
//		Zona zona = new Zona();
//		zona.setNaziv("zona1");
//		when(zonaServ.findByName(Matchers.any())).thenReturn(zona);
//
//		kartaServ.createNewTicket(kartaDTO, korisnik, 4000.0);
//
//		ArgumentCaptor<VisednevnaKarta> argument1 = ArgumentCaptor.forClass(VisednevnaKarta.class);
//		verify(kartaRep, atLeast(1)).save(argument1.capture());
//		
//		
//		assertEquals(argument1.getValue().getCena(),new Double(4000.0));
//		assertEquals(argument1.getValue().getDatumIsteka(),Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//		assertEquals(argument1.getValue().getKorisnik(),korisnik);
//		assertEquals(argument1.getValue().getTipPrevoza(),TipVozila.AUTOBUS);
//		assertThat(argument1.getValue().getKod()).isNotNull();
//		assertEquals(argument1.getValue().getZona().getNaziv(),"zona1");
//		assertEquals(argument1.getValue().getTipKorisnika(),StatusKorisnika.PENZIONER);
//		
//		
//		verify(osobaService, times(1)).save(korisnik);
//		
//
//	
//
//
//	}

}