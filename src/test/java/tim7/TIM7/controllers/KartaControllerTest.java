package tim7.TIM7.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tim7.TIM7.TestUtil;
import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.model.Zona;
import tim7.TIM7.services.CenovnikService;
import tim7.TIM7.services.KartaService;
import tim7.TIM7.services.LinijaService;
import tim7.TIM7.services.OsobaService;
import tim7.TIM7.services.ZonaService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KartaControllerTest {

	private static final String URL_PREFIX = "/karte";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	CenovnikService cenovnikServ;

	@Autowired
	KartaService kartaServ;

	@Autowired
	LinijaService linijaServ;

	@Autowired
	ZonaService zonaServ;

	@Autowired
	KartaController kartaCon;

	@Autowired
	OsobaService osobaServ;

	private DnevnaKarta karta1;
	private VisednevnaKarta karta2;

	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		if (karta1 == null) {
			karta1 = new DnevnaKarta();
			karta1.setKod("1se45nx4");
			karta1.setCena(60.0);
			karta1.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));
			Linija linija = new Linija();
			linija.setNaziv("linija1");
			karta1.setLinija(linija);
			linijaServ.save(linija);

		}

		if (karta2 == null) {

			karta2 = new VisednevnaKarta();
			karta2.setKod("6b33za7g");
			karta2.setCena(2000.0);
			karta2.setTip(TipKarte.valueOf("GODISNJA"));
			karta2.setTipPrevoza(TipVozila.valueOf("METRO"));
			karta2.setDatumIsteka(new Date(2019, 1, 31, 0, 0, 0));
			karta2.setTipKorisnika(StatusKorisnika.valueOf("STUDENT"));
			Zona zona = new Zona();
			zona.setNaziv("zona1");
			karta2.setZona(zona);
			zonaServ.save(zona);

		}

	}

	@Test
	public void izlistavanjeKartaKorisnikas() throws Exception {

		Korisnik korisnik = new Korisnik();
		korisnik.setKorIme("KorIme");

		ArrayList<Karta> karte = new ArrayList<Karta>();
		karte.add(karta1);
		karta1.setKorisnik(korisnik);
		korisnik.setKarte(karte);

		osobaServ.save(korisnik);
		kartaServ.save(karta1);

		String token = TestUtil.generateToken(korisnik.getKorIme());

		mockMvc.perform(get(URL_PREFIX + "/izlistajKarte").header("X-Auth-Token", token)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$.[*].kod").value(hasItem("1se45nx4")))
				.andExpect(jsonPath("$.[*].cena").value(hasItem(60.0)))
				.andExpect(jsonPath("$.[*].tipPrevoza").value(hasItem("AUTOBUS")))
				.andExpect(jsonPath("$.[*].tipKarte").value(hasItem("DNEVNA")));

	}

	@Test
	public void kupovinaVecPostojeceKarte() throws Exception {

		Korisnik korisnik = new Korisnik();
		korisnik.setKorIme("KorIme1");

		ArrayList<Karta> karte = new ArrayList<Karta>();
		karte.add(karta2);

		karta2.setKorisnik(korisnik);
		korisnik.setKarte(karte);

		osobaServ.save(korisnik);
		kartaServ.save(karta2);

		String token = TestUtil.generateToken(korisnik.getKorIme());

		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setTipPrevoza("METRO");
		kartaDTO.setLinijaZona("zona1");

		mockMvc.perform(post(URL_PREFIX + "/kupovinaKarte").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(TestUtil.json(kartaDTO)).header("X-Auth-Token", token)).andExpect(status().isNotModified());

	}
	
	@Test
	@Transactional
	public void upesnaKupovinaKarte() throws Exception {
		
		Korisnik korisnik = new Korisnik();
		korisnik.setKorIme("KorIme3");
		korisnik.setStatus(StatusKorisnika.STUDENT);
		korisnik.setKarte(new ArrayList());
		
		osobaServ.save(korisnik);
		
		String token = TestUtil.generateToken(korisnik.getKorIme());
		
		cenovnikServ.save(TestUtil.kreiranjeCenovnika());
		
		KartaDTO kartaDTO = new KartaDTO();
		kartaDTO.setTipKarte("GODISNJA");
		kartaDTO.setTipPrevoza("TRAMVAJ");
		kartaDTO.setLinijaZona("prigradska");
		kartaDTO.setStatusKorisnika("STUDENT");
		
		mockMvc.perform(post(URL_PREFIX + "/kupovinaKarte").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(TestUtil.json(kartaDTO)).header("X-Auth-Token", token)).andExpect(status().isCreated());
		assertEquals(((Korisnik)osobaServ.findByUsername("KorIme3")).getKarte().size(), 1);
		
	}

}
