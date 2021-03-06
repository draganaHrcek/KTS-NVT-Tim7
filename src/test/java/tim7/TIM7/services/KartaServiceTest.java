
package tim7.TIM7.services;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import tim7.TIM7.TestUtil;
import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.OdgovorDTO;
import tim7.TIM7.dto.OdobrenjeKarteDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.LinijaUZoni;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.KartaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KartaServiceTest {

	@Autowired
	private KartaService kartaServ;
	
	@MockBean
	private OsobaService osobaService;

	@MockBean
	private KartaRepository kartaRep;
	
	@MockBean
	private ZonaService zonaServ;
	
	@MockBean
	private LinijaService linijaServ;
	
	@MockBean
	private CenovnikService cenovnikServ;

	private DnevnaKarta karta1;
	private VisednevnaKarta karta2;

	@Before
	public void setUp() {
		karta1 = new DnevnaKarta();
		karta1.setKod("1se45nx4");
		karta1.setCena(60.0);
		karta1.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));
		Linija linija = new Linija();
		linija.setNaziv("linija1");
		karta1.setLinija(linija);

		when(kartaRep.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(karta1));

		karta2 = new VisednevnaKarta();
		karta2.setKod("6b33za7g");
		karta2.setCena(2000.0);
		karta2.setTip(TipKarte.valueOf("GODISNJA"));
		karta2.setTipPrevoza(TipVozila.valueOf("METRO"));
		karta2.setTipKorisnika(StatusKorisnika.valueOf("STUDENT"));
		Zona zona = new Zona();
		zona.setNaziv("zona1");
		karta2.setZona(zona);

		when(kartaRep.findById(2000l)).thenReturn(Optional.ofNullable(null));
		when(kartaRep.findByKod("1se45nx4")).thenReturn(Optional.of(karta1));
		when(kartaRep.findByKod("11111111")).thenReturn(Optional.ofNullable(null));

	}

	@Test
	public void findOne() {

		DnevnaKarta karta = (DnevnaKarta) kartaServ.findOne(1L);
		assertThat(karta).isNotNull();

		assertEquals(karta.getCena(), new Double(60.0));
		assertEquals(karta.getKod(), "1se45nx4");
		assertEquals(karta.getTipPrevoza(), TipVozila.valueOf("AUTOBUS"));
		assertEquals(karta.getLinija().getNaziv(), "linija1");

		verify(kartaRep, times(1)).findById(1l);
	}

	@Test
	public void findByKod() {

		DnevnaKarta karta = (DnevnaKarta) kartaServ.findByKod("1se45nx4");
		assertThat(karta).isNotNull();

		assertEquals(karta.getCena(), new Double(60.0));
		assertEquals(karta.getKod(), "1se45nx4");
		assertEquals(karta.getTipPrevoza(), TipVozila.valueOf("AUTOBUS"));
		assertEquals(karta.getLinija().getNaziv(), "linija1");

		verify(kartaRep, times(1)).findByKod("1se45nx4");
	}
	
	
	@Test
	public void findByKodNotExist() {

		DnevnaKarta karta = (DnevnaKarta) kartaServ.findByKod("11111111");
		assertThat(karta).isNull();


		verify(kartaRep, times(1)).findByKod("11111111");
	}
	@Test
	public void findAll() {

		ArrayList<Karta> karte = new ArrayList<Karta>();
		karte.add(karta1);
		karte.add(karta2);
		when(kartaRep.findAll()).thenReturn(karte);

		ArrayList<Karta> k = (ArrayList<Karta>) kartaServ.findAll();
		assertThat(k).isNotNull();

		assertEquals(k.size(), 2);
		verify(kartaRep, times(1)).findAll();

	}

	@Test
	public void findOneThatNotExist() {

		Karta karta = kartaServ.findOne(2000l);

		assertEquals(karta, null);

		verify(kartaRep, times(1)).findById(2000l);

	}

	@Test
	public void findAllEmptyList() {
		when(kartaRep.findAll()).thenReturn(new ArrayList());

		ArrayList<Karta> korisnici = (ArrayList<Karta>) kartaRep.findAll();
		assertThat(korisnici).isEmpty();

		verify(kartaRep, times(1)).findAll();

	}

	@Test
	public void save() {

		DnevnaKarta karta = new DnevnaKarta();
		karta.setKod("2cn6dzzl");
		karta.setCena(50.0);
		karta1.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));

		kartaServ.save(karta);

		verify(kartaRep, times(1)).save(karta);

	}

	@Test
	public void delete() {

		kartaServ.delete(1l);

		Karta k = kartaServ.findOne(1l);
		assertEquals(k.isObrisan(), true);

	}

	@Test
	public void findAllUserTicketsEmptyList() {

		Korisnik kor = new Korisnik();
		kor.setKarte(new ArrayList<Karta>());

		ArrayList<KartaDTO> karte = kartaServ.findAllUserTickets(kor);

		assertThat(karte).isEmpty();

	}

	@Test
	public void findAllUserTickets() {

		ArrayList<Karta> karte = new ArrayList<Karta>();
		karte.add(karta1);
		karte.add(karta2);

		Korisnik kor = new Korisnik();
		kor.setKarte(karte);

		ArrayList<KartaDTO> karteDTO = kartaServ.findAllUserTickets(kor);

		assertThat(karteDTO).isNotEmpty();
		assertEquals(karteDTO.size(), 2);

		// provera da li se DTO objekti poklapaju sa kartama korisnika

		boolean dnevna = false;
		boolean godisnja = false;

		for (KartaDTO i : karteDTO) {
			if (i.getTipKarte().equals("DNEVNA")) {
				if (i.getKod().equals("1se45nx4") && i.getCena() == 60.0 && i.getTipPrevoza().equals("AUTOBUS")
						&& i.getLinijaZona().equals("linija1")) {
					dnevna = true;

				}
			} else if (i.getTipKarte().equals("GODISNJA")) {
				if (i.getKod().equals("6b33za7g") && i.getCena() == 2000.0 && i.getTipPrevoza().equals("METRO")
						&& i.getLinijaZona().equals("zona1") && i.getStatusKorisnika().equals("STUDENT")) {
					godisnja = true;

				}
			}
		}

		assertEquals(dnevna, true);
		assertEquals(godisnja, true);

	}

	@Test
	public void cenaKarteDnevna() {

		Korisnik korisnik = new Korisnik();

		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipPrevoza("AUTOBUS");
		kartaDTO.setTipKarte("DNEVNA");
		kartaDTO.setLinijaZona("linija1");

		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);

		assertEquals(cena, new Double(50.0));

	}

	@Test
	public void cenaKarteStudentskaVisednevna() {

		Korisnik korisnik = new Korisnik();
		korisnik.setStatus(StatusKorisnika.STUDENT);

		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipPrevoza("TRAMVAJ");
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setLinijaZona("prigradska");

		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);

		assertEquals(cena, new Double(1000 * (100 - 20) / 100));

	}

	@Test
	public void cenaKartePenzionerskaVisednevna() {

		Korisnik korisnik = new Korisnik();
		korisnik.setStatus(StatusKorisnika.PENZIONER);

		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipPrevoza("TRAMVAJ");
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setLinijaZona("prigradska");

		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);

		assertEquals(cena, new Double(1000 * (100 - 30) / 100));

	}

	@Test
	public void cenaKarteDjakVisednevna() {

		Korisnik korisnik = new Korisnik();
		korisnik.setStatus(StatusKorisnika.DJAK);

		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipPrevoza("TRAMVAJ");
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setLinijaZona("prigradska");

		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);

		assertEquals(cena, new Double(1000 * (100 - 10) / 100));

	}

	@Test
	public void cenaKarteNezaposlenVisednevna() {

		Korisnik korisnik = new Korisnik();
		korisnik.setStatus(StatusKorisnika.NEZAPOSLEN);

		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipPrevoza("TRAMVAJ");
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setLinijaZona("prigradska");

		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);

		assertEquals(cena, new Double(1000 * (100 - 25) / 100));

	}

	@Test
	public void cenaKarteVisednevnaZaposleni() {

		Korisnik korisnik = new Korisnik();
		korisnik.setStatus(StatusKorisnika.RADNIK);

		when(cenovnikServ.getTrenutni()).thenReturn(TestUtil.kreiranjeCenovnika());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipPrevoza("TRAMVAJ");
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setLinijaZona("prigradska");

		Double cena = kartaServ.cenaKarte(kartaDTO, korisnik);

		assertEquals(cena, new Double(1000));

	}
	@Test
	public void createNewTicketDnevna() {

		Korisnik korisnik = new Korisnik();
		korisnik.setKarte(new ArrayList<Karta>());
		korisnik.setIme("Dragana");
		korisnik.setPrezime("Hrcek");
		
		KartaDTO kartaDTO= new KartaDTO();
		kartaDTO.setTipKarte("DNEVNA");
		kartaDTO.setTipPrevoza("AUTOBUS");
		kartaDTO.setLinijaZona("linija1");
		
		Linija linija = new Linija();
		linija.setNaziv("linija1");
		when(linijaServ.findByName(Matchers.any())).thenReturn(linija);

		kartaServ.createNewTicket(kartaDTO, korisnik, 50.0);

		ArgumentCaptor<DnevnaKarta> argument1 = ArgumentCaptor.forClass(DnevnaKarta.class);
		verify(kartaRep, atLeast(1)).save(argument1.capture());
		
		
		assertEquals(argument1.getValue().getCena(),new Double(50.0));
		assertEquals(argument1.getValue().getDatumIsteka(),Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toInstant(ZoneOffset.UTC)));
		assertEquals(argument1.getValue().getKorisnik(),korisnik);
		assertEquals(argument1.getValue().getTipPrevoza(),TipVozila.AUTOBUS);
		assertThat(argument1.getValue().getKod()).isNotNull();
		assertEquals(argument1.getValue().getLinija().getNaziv(),"linija1");
		
		
		verify(osobaService, times(1)).save(korisnik);
		

	


	}
	@Test
	public void createNewTicketMesecna() {
		LocalDate cd = LocalDate.now();
		

		Korisnik korisnik = new Korisnik();
		korisnik.setKarte(new ArrayList<Karta>());
		korisnik.setIme("Dragana");
		korisnik.setPrezime("Hrcek");
	
		
		KartaDTO kartaDTO= new KartaDTO();
		kartaDTO.setTipKarte("MESECNA");
		kartaDTO.setTipPrevoza("AUTOBUS");
		kartaDTO.setLinijaZona("zona1");
		kartaDTO.setStatusKorisnika("STUDENT");
		
		Zona zona = new Zona();
		zona.setNaziv("zona1");
		when(zonaServ.findByName(Matchers.any())).thenReturn(zona);

		kartaServ.createNewTicket(kartaDTO, korisnik, 1000.0);

		ArgumentCaptor<VisednevnaKarta> argument1 = ArgumentCaptor.forClass(VisednevnaKarta.class);
		verify(kartaRep, atLeast(1)).save(argument1.capture());
		
		
		assertEquals(argument1.getValue().getCena(),new Double(1000.0));
		assertEquals(argument1.getValue().getDatumIsteka(),Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		assertEquals(argument1.getValue().getKorisnik(),korisnik);
		assertEquals(argument1.getValue().getTipPrevoza(),TipVozila.AUTOBUS);
		assertThat(argument1.getValue().getKod()).isNotNull();
		assertEquals(argument1.getValue().getZona().getNaziv(),"zona1");
		assertEquals(argument1.getValue().getTipKorisnika(),StatusKorisnika.STUDENT);
		
		
		verify(osobaService, times(1)).save(korisnik);
		

	


	}
	
	@Test
	public void createNewTicketGodisnja() {
		LocalDate cd = LocalDate.now();
		

		Korisnik korisnik = new Korisnik();
		korisnik.setKarte(new ArrayList<Karta>());
		korisnik.setIme("Dragana");
		korisnik.setPrezime("Hrcek");
	
		
		KartaDTO kartaDTO= new KartaDTO();
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setTipPrevoza("AUTOBUS");
		kartaDTO.setLinijaZona("zona1");
		kartaDTO.setStatusKorisnika("PENZIONER");
		
		Zona zona = new Zona();
		zona.setNaziv("zona1");
		when(zonaServ.findByName(Matchers.any())).thenReturn(zona);

		kartaServ.createNewTicket(kartaDTO, korisnik, 4000.0);

		ArgumentCaptor<VisednevnaKarta> argument1 = ArgumentCaptor.forClass(VisednevnaKarta.class);
		verify(kartaRep, atLeast(1)).save(argument1.capture());
		
		
		assertEquals(argument1.getValue().getCena(),new Double(4000.0));
		assertEquals(argument1.getValue().getDatumIsteka(),Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		assertEquals(argument1.getValue().getKorisnik(),korisnik);
		assertEquals(argument1.getValue().getTipPrevoza(),TipVozila.AUTOBUS);
		assertThat(argument1.getValue().getKod()).isNotNull();
		assertEquals(argument1.getValue().getZona().getNaziv(),"zona1");
		assertEquals(argument1.getValue().getTipKorisnika(),StatusKorisnika.PENZIONER);
		
		
		verify(osobaService, times(1)).save(korisnik);
		

	


	}
	
	
	@Test
	public void getNeodobreneKarte () {
		
		LocalDate cd = LocalDate.now();
		
		VisednevnaKarta  karta1 = new VisednevnaKarta ();
		karta1.setOdobrena(false);
		karta1.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		VisednevnaKarta  karta2 = new VisednevnaKarta ();
		karta2.setOdobrena(true);
		karta2.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		
		//samo ova treba da bude vracena jer ona nije odbijena niti je odobrena kupovina
		VisednevnaKarta  karta3 = new VisednevnaKarta ();
		karta3.setOdobrena(null);
		karta3.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		Korisnik kor= new Korisnik();
		kor.setIme("korIme");	
		kor.setStatus(StatusKorisnika.STUDENT);
		kor.setLokacijaDokumenta("");
		
		Zona zona= new Zona();
		zona.setNaziv("ZONA1");
		
		
		karta3.setKorisnik(kor);
		karta3.setZona(zona);
		karta3.setTipPrevoza(TipVozila.AUTOBUS);
		karta3.setTip(TipKarte.GODISNJA);
		
		ArrayList<Karta> karte = new ArrayList<Karta>();
		karte.add(karta1);
		karte.add(karta2);
		karte.add(karta3);
		
		
		when(kartaRep.findAll()).thenReturn(karte);
		
		ArrayList <OdobrenjeKarteDTO> k= (ArrayList<OdobrenjeKarteDTO>) kartaServ.getNeodobreneKarte();
	
		
		assertEquals(k.size(), 1);
		
		
	
		
		
	}
	@Test
	public void getNeodobreneKartePraznaLista () {
		
		ArrayList<Karta> karte = new ArrayList<Karta>();
		when(kartaRep.findAll()).thenReturn(karte);
		
		ArrayList <OdobrenjeKarteDTO> k= (ArrayList<OdobrenjeKarteDTO>) kartaServ.getNeodobreneKarte();
		
		
		assertEquals(k, null);
		
		
		
		
		
	}
	
	@Test
	public void kartaExist () {
		
		LocalDate cd = LocalDate.now();
		VisednevnaKarta karta = new VisednevnaKarta();
		
		Zona zona= new Zona();
		zona.setNaziv("ZONA1");
		karta.setZona(zona);
		karta.setTip(TipKarte.GODISNJA);
		karta.setTipPrevoza(TipVozila.AUTOBUS);
		karta.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		ArrayList<Karta> karte = new ArrayList<Karta>();
		karte.add(karta);
		
		Korisnik kor = new Korisnik ();
		
		kor.setKarte(karte);
		
		
		KartaDTO kartaDTO= new KartaDTO();
		kartaDTO.setLinijaZona(zona.getNaziv());
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setTipPrevoza("AUTOBUS");
		
		boolean exist= kartaServ.kartaExist(kartaDTO, kor);
		
		assertEquals(exist, true);
		
		
		
		
		
	}
	@Test
	public void kartaNotExist () {
		
		
		Korisnik kor = new Korisnik ();
		ArrayList<Karta> karte = new ArrayList<Karta>();
		kor.setKarte(karte);
		
		
		
		KartaDTO kartaDTO= new KartaDTO();
		
		Zona zona= new Zona();
		zona.setNaziv("ZONA1");
		
		kartaDTO.setLinijaZona(zona.getNaziv());
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setTipPrevoza("AUTOBUS");
		
		boolean exist= kartaServ.kartaExist(kartaDTO, kor);
		
		assertEquals(exist, false);
		
		
		
		
		
	}
	@Test
	public void checkKartaDnevna () {
		
		
		DnevnaKarta karta= new DnevnaKarta();
		karta.setTipPrevoza(TipVozila.AUTOBUS);
		karta.setKod("12345678");
		
		Linija linija= new Linija();
		linija.setNaziv("linija1");
		karta.setLinija(linija);
		
		when(kartaRep.findByKod("12345678")).thenReturn(Optional.of(karta));
		OdgovorDTO odg= kartaServ.checkKarta(TipVozila.AUTOBUS, "linija1", "12345678");
		
		assertEquals(odg.getOdgovor(), "Karta uspesno cekirana");
		
		
		
		
		
	}
	
	@Test
	public void checkKartaUpotrebljenaDnevna () {
		
		
		DnevnaKarta karta= new DnevnaKarta();
		karta.setTipPrevoza(TipVozila.AUTOBUS);
		karta.setKod("12345678");
		karta.setUpotrebljena(true);
		
		Linija linija= new Linija();
		linija.setNaziv("linija1");
		karta.setLinija(linija);
		
		when(kartaRep.findByKod("12345678")).thenReturn(Optional.of(karta));
		OdgovorDTO odg= kartaServ.checkKarta(TipVozila.AUTOBUS, "linija1", "12345678");
		
		assertEquals(odg, null);
		
		
		
		
		
	}
	
	@Test
	public void checkKartaNepostojeca () {
		
		
	
		
		when(kartaRep.findByKod("12345678")).thenReturn(Optional.ofNullable(null));
		
		OdgovorDTO odg= kartaServ.checkKarta(TipVozila.AUTOBUS, "linija1", "12345678");
		
		assertEquals(odg, null);
		
		
		
		
		
	}
	@Test
	public void checkKartaVazecaVisednevna () {
		
		
		LocalDate cd = LocalDate.now();
		VisednevnaKarta karta= new VisednevnaKarta();
		karta.setTipPrevoza(TipVozila.AUTOBUS);
		karta.setKod("12345678");
		karta.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		
		Linija linija= new Linija();
		linija.setNaziv("linija1");
		
		Zona zona= new Zona();
		zona.setNaziv("ZONA1");
		ArrayList<LinijaUZoni> linije = new ArrayList<LinijaUZoni>();
		LinijaUZoni linijaUZoni = new LinijaUZoni(1l, linija, zona, false);
		linije.add(linijaUZoni);
		
		zona.setLinije(linije);
		
		karta.setZona(zona);
		karta.setOdobrena(true);
	
		
	
		
		when(kartaRep.findByKod("12345678")).thenReturn(Optional.ofNullable(karta));
		
		OdgovorDTO odg= kartaServ.checkKarta(TipVozila.AUTOBUS, "linija1", "12345678");
		
		assertEquals(odg.getOdgovor(), "Karta je vazeca");
		
		
		
		
		
	}
	
	@Test
	public void checkKartaVisednevnaIstekla() {
		
		
		
		VisednevnaKarta karta= new VisednevnaKarta();
		karta.setTipPrevoza(TipVozila.AUTOBUS);
		karta.setKod("12345678");
		karta.setDatumIsteka(Date.from(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(23, 59, 59)).toInstant(ZoneOffset.UTC)));
		
		
		Linija linija= new Linija();
		linija.setNaziv("linija1");
		
		Zona zona= new Zona();
		zona.setNaziv("ZONA1");
		ArrayList<LinijaUZoni> linije = new ArrayList<LinijaUZoni>();
		LinijaUZoni linijaUZoni = new LinijaUZoni(1l, linija, zona, false);
		linije.add(linijaUZoni);
		
		zona.setLinije(linije);
		
		karta.setZona(zona);
		karta.setOdobrena(true);
	
		
	
		
		when(kartaRep.findByKod("12345678")).thenReturn(Optional.ofNullable(karta));
		
		OdgovorDTO odg= kartaServ.checkKarta(TipVozila.AUTOBUS, "linija1", "12345678");
		
		assertEquals(odg, null);
		
		
		
		
		
	}
	@Test
	public void verifyKartaNepostojeca() {
		
		when(kartaRep.findById(Matchers.anyLong())).thenReturn(Optional.ofNullable(null));
		
		OdgovorDTO odg= kartaServ.verifyKarta(1L, "");
		
		assertEquals(odg, null);
		
		
		
		
		
	}
	@Test
	public void verifyKartaOdobrena() {
		
		LocalDate cd = LocalDate.now();
		
		VisednevnaKarta karta= new VisednevnaKarta();
		karta.setOdobrena(null);
		karta.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		when(kartaRep.findById(Matchers.anyLong())).thenReturn(Optional.of(karta));
		
		OdgovorDTO odg= kartaServ.verifyKarta(1L, "ODOBRENA");
		
		assertEquals(odg.getOdgovor(), "Karta je uspesno odobrena");
		
		
		
		
		
	}
	@Test
	public void verifyKartaOdbijena() {
		
		LocalDate cd = LocalDate.now();
		
		VisednevnaKarta karta= new VisednevnaKarta();
		karta.setOdobrena(null);
		karta.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		when(kartaRep.findById(Matchers.anyLong())).thenReturn(Optional.of(karta));
		
		OdgovorDTO odg= kartaServ.verifyKarta(1L, "PONISTENA");
		
		assertEquals(odg.getOdgovor(), "Karta je uspesno ponistena");
		
		
		
		
		
	}
	
	
	

}