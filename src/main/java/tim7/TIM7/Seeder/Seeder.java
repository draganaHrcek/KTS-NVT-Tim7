package tim7.TIM7.Seeder;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Kondukter;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.LinijaUZoni;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.StanicaULiniji;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Verifikator;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.KartaRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.LinijaUZoniRepository;
import tim7.TIM7.repositories.OsobaRepository;
import tim7.TIM7.repositories.RasporedVoznjeRepository;
import tim7.TIM7.repositories.RedVoznjeRepository;
import tim7.TIM7.repositories.StanicaRepository;
import tim7.TIM7.repositories.StanicaULinijiRepository;
import tim7.TIM7.repositories.StavkaCenovnikaRepository;
import tim7.TIM7.repositories.StavkaRepository;
import tim7.TIM7.repositories.VoziloRepository;
import tim7.TIM7.repositories.ZonaRepository;

@Component
public class Seeder {

	@Autowired
	CenovnikRepository cenovnikRepository;

	@Autowired
	ZonaRepository zonaRepository;

	@Autowired
	LinijaRepository linijaRepository;

	@Autowired
	StavkaCenovnikaRepository stavkaCenovnikaRepository;

	@Autowired
	StavkaRepository stavkaRepository;

	@Autowired
	VoziloRepository voziloRepository;

	@Autowired
	OsobaRepository osobaRepository;
	
	@Autowired
	StanicaRepository stanicaRepository;
	
	@Autowired
	RedVoznjeRepository redVoznjeRepository;
	
	@Autowired
	RasporedVoznjeRepository rasporedVoznjeRepository;
	
	@Autowired
	KartaRepository kartaRepository;
	
	@Autowired
	LinijaUZoniRepository luzRepository;
	
	@Autowired
	StanicaULinijiRepository sulRepository;

	public Seeder() {
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
//		seedCenovnik();
//		seedZona();
//		seedLinija();
//		connectZonaLinija();
//		seedStavka();
//		seedStavkaCenovnika();
//		seedOsoba();
//		seedIkija();
//		seedZoneLinijeStaniceRedoviRasporediVoznje();
//		seedKarte();
//		seedOdobreneNeodobreneKarte();
		
	}

	public void seedCenovnik() {
		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();

		before.set(2018, 9, 1);
		after.set(2019, 6, 30);
		

		Cenovnik trenutni = new Cenovnik(before.getTime(), after.getTime(),new ArrayList<>(), false, 20,30,20,30);

		after.set(2019, 0, 1);
		Cenovnik prosli = new Cenovnik(before.getTime(), after.getTime(), new ArrayList<>(),false, 10,20, 10 ,20);

		before.setTime(after.getTime());
		after.set(2019, 9, 1);
		Cenovnik buduci = new Cenovnik(before.getTime(), after.getTime(),new ArrayList<>(), false, 20,10,30,10);

		cenovnikRepository.save(trenutni);
		cenovnikRepository.save(prosli);
		cenovnikRepository.save(buduci);

	}

	public void seedLinija() {
		for (int i = 1; i < 5; i++) {
			Linija line = new Linija("linija " + i, false);
			linijaRepository.save(line);
		}
	}

	public void seedZona() {
		zonaRepository.save(new Zona("gradksa", false));
		zonaRepository.save(new Zona("prigradska", false));
		zonaRepository.save(new Zona("veternik", false));

	}

	public void connectZonaLinija() {
		Zona gradska = zonaRepository.findByNaziv("gradksa");
		Zona prigradska = zonaRepository.findByNaziv("prigradska");
		Zona veternik = zonaRepository.findByNaziv("veternik");
		


		Linija linija1 = linijaRepository.findByNaziv("linija 1");
		LinijaUZoni luz1 = new LinijaUZoni();
		luz1.setLinija(linija1);
		luz1.setZona(gradska);
		luzRepository.save(luz1);

		Linija linija2 = linijaRepository.findByNaziv("linija 2");		
		
		LinijaUZoni luz2 = new LinijaUZoni();
		luz2.setLinija(linija2);
		luz2.setZona(gradska);
		luzRepository.save(luz2);
		
		LinijaUZoni luz3 = new LinijaUZoni();
		luz3.setLinija(linija2);
		luz3.setZona(prigradska);
		luzRepository.save(luz3);

		Linija linija3 = linijaRepository.findByNaziv("linija 3");
		LinijaUZoni luz4 = new LinijaUZoni();
		luz4.setLinija(linija3);
		luz4.setZona(prigradska);
		luzRepository.save(luz4);

		Linija linija4 = linijaRepository.findByNaziv("linija 4");
		LinijaUZoni luz5 = new LinijaUZoni();
		luz5.setLinija(linija4);
		luz5.setZona(gradska);
		luzRepository.save(luz5);
		
		LinijaUZoni luz6 = new LinijaUZoni();
		luz6.setLinija(linija4);
		luz6.setZona(prigradska);
		luzRepository.save(luz6);
		
		LinijaUZoni luz7 = new LinijaUZoni();
		luz7.setLinija(linija4);
		luz7.setZona(veternik);
		luzRepository.save(luz7);
		
		luzRepository.save(luz1);
		luzRepository.save(luz2);
		luzRepository.save(luz3);
		luzRepository.save(luz4);
		luzRepository.save(luz5);
		luzRepository.save(luz6);
		luzRepository.save(luz7);
	}

	public void seedStavkaCenovnika() {
		int i = 0;
		for (Stavka stavka : stavkaRepository.findAll()) {
			Cenovnik cenovnik = cenovnikRepository.findAll().get(i%3);
			stavkaCenovnikaRepository.save(new StavkaCenovnika(100 + i * 10, stavka, cenovnik, false));
			i++;
		}

	}

	public void seedStavka() {
		Zona gradska = zonaRepository.findByNaziv("gradksa");
		Zona prigradska = zonaRepository.findByNaziv("prigradska");
		Zona veternik = zonaRepository.findByNaziv("veternik");
		Linija linija;

		for (TipKarteCenovnik tip : TipKarteCenovnik.values()) {
			if(tip.equals(TipKarteCenovnik.DNEVNA)){
				linija = linijaRepository.findByNaziv("linija 1");
				stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.METRO, linija, false));
	
				linija = linijaRepository.findByNaziv("linija 2");
				stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.METRO, linija, false));

	
				linija = linijaRepository.findByNaziv("linija 3");
				stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.METRO, linija, false));

	
				linija = linijaRepository.findByNaziv("linija 4");
				stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, linija, false));
				stavkaRepository.save(new Stavka(tip, TipVozila.METRO, linija, false));

			}
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, gradska, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, gradska, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.METRO, gradska, false));
			
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, prigradska, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, prigradska, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.METRO, prigradska, false));
			
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, veternik, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.TRAMVAJ, veternik, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.METRO, veternik, false));
		}
	}
	
	public void seedIkija() {
		Vozilo vehicle1 = new Vozilo();
		vehicle1.setObrisan(false);
		vehicle1.setRegistracija("NS123");
		vehicle1.setTipVozila(TipVozila.AUTOBUS);
		vehicle1.setLinija(null);
		voziloRepository.save(vehicle1);
		
		Vozilo vehicle2 = new Vozilo();
		vehicle2.setObrisan(false);
		vehicle2.setRegistracija("NS456");
		vehicle2.setTipVozila(TipVozila.AUTOBUS);
		Linija line = linijaRepository.findByNaziv("linija 3");
		vehicle2.setLinija(line);
		voziloRepository.save(vehicle2);
		
		Vozilo vehicle3 = new Vozilo();
		vehicle3.setObrisan(true);
		vehicle3.setRegistracija("NS789");
		vehicle3.setTipVozila(TipVozila.AUTOBUS);
		vehicle3.setLinija(null);
		voziloRepository.save(vehicle3);
		
	}

	public void seedOsoba() {
		//pass je '12345678'
		Administrator admin = new Administrator("a", "$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "Admin", "Adminovic","a@gmail.com");
		Korisnik korisnik = new Korisnik("e","$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "Elena", "Roncevic","e@gmail.com", "", null, new ArrayList<>());
		
		osobaRepository.save(korisnik);
		osobaRepository.save(admin);
		//KORISNICI ZA TESTOVE 
		
		
		Administrator adminTest = new Administrator("AdminTest", "$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "AdminTest", "AdminTest","a@gmail.com");
		Korisnik korisnikTest = new Korisnik("KorisnikTest","$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "KorisnikTest", "KorisnikTest","e@gmail.com", "",StatusKorisnika.valueOf("STUDENT") , new ArrayList<>());
		Verifikator verifikatorTest = new Verifikator("VerifikatorTest", "$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "VerifikatorTest", "VerifikatorTest","a@gmail.com");
		Kondukter kondukterTest = new Kondukter("KondukterTest", "$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "KondukterTest", "KondukterTest","a@gmail.com");
		Korisnik korisnikBezStatusaTest = new Korisnik("KorisnikBezStatusaTest","$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "ImeTest", "PrezimeTest","test@gmail.com", "", null , new ArrayList<>());
		
		Korisnik korisnikLoginTest = new Korisnik("KorisnikLoginTest","$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "ImeTest", "PrezimeTest","test@gmail.com", "", null , new ArrayList<>());
		
		
    
    LocalDate cd = LocalDate.now();
		
		DnevnaKarta karta1= new DnevnaKarta();
		karta1.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));
		karta1.setKod("2gm8O0z");
		karta1.setDatumIsteka(Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toInstant(ZoneOffset.UTC)));
		karta1.setLinija(linijaRepository.findByNaziv("linija 1"));
		karta1.setKorisnik(korisnikTest);
		
		
		//mesecne
		
		
		VisednevnaKarta karta2= new VisednevnaKarta();
		karta2.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));
		karta2.setKod("ap0B45l");
		karta2.setTip(TipKarte.valueOf("MESECNA"));
		karta2.setDatumIsteka(Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		karta2.setZona(zonaRepository.findByNaziv("prigradska"));
		karta2.setTipKorisnika(korisnikTest.getStatus());
		karta2.setKorisnik(korisnikTest);
		karta2.setOdobrena(true);
		
		

		VisednevnaKarta karta4= new VisednevnaKarta();
		karta4.setTipPrevoza(TipVozila.valueOf("TRAMVAJ"));
		karta4.setKod("b9lli23z");
		karta4.setTip(TipKarte.valueOf("MESECNA"));
		karta4.setDatumIsteka(Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		karta4.setZona(zonaRepository.findByNaziv("gradksa"));
		karta4.setTipKorisnika(korisnikTest.getStatus());
		karta4.setKorisnik(korisnikTest);
		karta4.setOdobrena(false);
		
		
		VisednevnaKarta karta5= new VisednevnaKarta();
		karta5.setTipPrevoza(TipVozila.valueOf("METRO"));
		karta5.setKod("wi6b1xzo");
		karta5.setTip(TipKarte.valueOf("MESECNA"));
		karta5.setDatumIsteka(Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		karta5.setZona(zonaRepository.findByNaziv("veternik"));
		karta5.setTipKorisnika(korisnikTest.getStatus());
		karta5.setKorisnik(korisnikTest);
	
		
		//godisnje
		
		
		VisednevnaKarta karta3= new VisednevnaKarta();
		karta3.setTipPrevoza(TipVozila.valueOf("AUTOBUS"));
		karta3.setKod("ecx4m92f");
		karta3.setTip(TipKarte.valueOf("GODISNJA"));
		karta3.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		karta3.setOdobrena(true);
		karta3.setTipKorisnika(korisnikTest.getStatus());
		karta3.setZona(zonaRepository.findByNaziv("prigradska"));
		karta3.setKorisnik(korisnikTest);
		
		
		VisednevnaKarta karta6= new VisednevnaKarta();
		karta6.setTipPrevoza(TipVozila.valueOf("TRAMVAJ"));
		karta6.setKod("4df91m3a");
		karta6.setTip(TipKarte.valueOf("GODISNJA"));
		karta6.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		karta6.setOdobrena(false);
		karta6.setTipKorisnika(korisnikTest.getStatus());
		karta6.setZona(zonaRepository.findByNaziv("gradksa"));
		karta6.setKorisnik(korisnikTest);
		
		VisednevnaKarta karta7= new VisednevnaKarta();
		karta7.setTipPrevoza(TipVozila.valueOf("METRO"));
		karta7.setKod("1xs87n42");
		karta7.setTip(TipKarte.valueOf("GODISNJA"));
		karta7.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		karta7.setTipKorisnika(korisnikTest.getStatus());
		karta7.setZona(zonaRepository.findByNaziv("veternik"));
		karta7.setKorisnik(korisnikTest);
		
		
		//dodavanje karat i snimanje u bazu
		
		
		korisnikTest.getKarte().add(karta1);
		korisnikTest.getKarte().add(karta2);
		korisnikTest.getKarte().add(karta3);
		korisnikTest.getKarte().add(karta4);
		korisnikTest.getKarte().add(karta5);
		
		osobaRepository.save(korisnikLoginTest);
		osobaRepository.save(korisnikTest);
		osobaRepository.save(adminTest);
		osobaRepository.save(verifikatorTest);
		osobaRepository.save(kondukterTest);
		osobaRepository.save(korisnikBezStatusaTest);
		kartaRepository.save(karta1);
		kartaRepository.save(karta2);
		kartaRepository.save(karta3);
		kartaRepository.save(karta4);
		kartaRepository.save(karta5);
		kartaRepository.save(karta6);
		kartaRepository.save(karta7);
		
	}
	
	
	public void seedRasporedVoznjeVremena(){
		List<RasporedVoznje> rasporediVoznje = rasporedVoznjeRepository.findAll();
		for (int i=0;i<rasporediVoznje.size();i++){
			LocalTime begin = LocalTime.of(4, 30);
			LocalTime end = LocalTime.of(0, 0);
			List<LocalTime> vremena = new ArrayList<LocalTime>();
			if (rasporediVoznje.get(i).getDanUNedelji().equals(DanUNedelji.RADNI)){
				//seeduj za radni dan 15 minuta razlike				
				while (begin.isAfter(end)){
					vremena.add(begin);
					begin=begin.plusMinutes(15);
				}
				vremena.add(end);
			}else if (rasporediVoznje.get(i).getDanUNedelji().equals(DanUNedelji.SUBOTA)){
				//seeduj za subotu 30 minuta razlike
				while (begin.isAfter(end)){
					vremena.add(begin);
					begin=begin.plusMinutes(30);
				}
				vremena.add(end);
			}else{
				//seeduj za nedelju 45 minuta razlike
				while (begin.isAfter(end)){
					vremena.add(begin);
					begin=begin.plusMinutes(45);
				}
				vremena.add(end);
			}
			rasporediVoznje.get(i).setVremena(vremena);
			rasporedVoznjeRepository.save(rasporediVoznje.get(i));
		}
		
	}
	
	public void seedZoneLinijeStaniceRedoviRasporediVoznje(){
		zonaRepository.save(new Zona("NazivZone1", false));
		zonaRepository.save(new Zona("NazivZone2", false));
		zonaRepository.save(new Zona("NazivZone3", false));
		zonaRepository.save(new Zona("NazivZone4", true));
		zonaRepository.save(new Zona("NazivZone5", true));
		
		linijaRepository.save(new Linija("NazivLinije1",false));
		linijaRepository.save(new Linija("NazivLinije2",false));
		linijaRepository.save(new Linija("NazivLinije3",false));
		linijaRepository.save(new Linija("NazivLinije4",false));
		linijaRepository.save(new Linija("NazivLinije5",false));
		linijaRepository.save(new Linija("NazivLinije6",true));
		
		Zona zona1=zonaRepository.findByNaziv("NazivZone1");
		Zona zona2=zonaRepository.findByNaziv("NazivZone2");
		Zona zona3=zonaRepository.findByNaziv("NazivZone3");
		Zona zona4=zonaRepository.findByNaziv("NazivZone4");
		Zona zona5=zonaRepository.findByNaziv("NazivZone5");
		
		Linija linija1=linijaRepository.findByNaziv("NazivLinije1");
		Linija linija2=linijaRepository.findByNaziv("NazivLinije2");
		Linija linija3=linijaRepository.findByNaziv("NazivLinije3");
		Linija linija4=linijaRepository.findByNaziv("NazivLinije4");
		Linija linija5=linijaRepository.findByNaziv("NazivLinije5");
		Linija linija6=linijaRepository.findByNaziv("NazivLinije6");
		
		LinijaUZoni luz1 = new LinijaUZoni();
		LinijaUZoni luz2 = new LinijaUZoni();
		LinijaUZoni luz3 = new LinijaUZoni();
		LinijaUZoni luz4 = new LinijaUZoni();
		LinijaUZoni luz5 = new LinijaUZoni();
		LinijaUZoni luz6 = new LinijaUZoni();
		LinijaUZoni luz7 = new LinijaUZoni();
		LinijaUZoni luz8 = new LinijaUZoni();
		
		luz1.setZona(zona1);
		luz1.setLinija(linija1);
		
		luz2.setZona(zona1);
		luz2.setLinija(linija2);
		
		luz3.setZona(zona2);
		luz3.setLinija(linija1);
		
		luz4.setZona(zona2);
		luz4.setLinija(linija2);
		
		luz5.setZona(zona2);
		luz5.setLinija(linija4);
		
		luz6.setZona(zona3);
		luz6.setLinija(linija1);
		
		luz7.setZona(zona3);
		luz7.setLinija(linija3);
		
		luz8.setZona(zona3);
		luz8.setLinija(linija5);
		
		luzRepository.save(luz1);
		luzRepository.save(luz2);
		luzRepository.save(luz3);
		luzRepository.save(luz4);
		luzRepository.save(luz5);
		luzRepository.save(luz6);
		luzRepository.save(luz7);
		luzRepository.save(luz8);
		
		stanicaRepository.save(new Stanica(false, "OznakaStanice1", 45.254827, 19.831585));
		stanicaRepository.save(new Stanica(false, "OznakaStanice2",45.254223, 19.825620));
		stanicaRepository.save(new Stanica(false, "OznakaStanice3",45.256941, 19.824118));
		stanicaRepository.save(new Stanica(false, "OznakaStanice4",45.259600, 19.827937));
		stanicaRepository.save(new Stanica(false, "OznakaStanice5",45.258301, 19.833560));
		stanicaRepository.save(new Stanica(false, "OznakaStanice6",45.251292, 19.824333));
		stanicaRepository.save(new Stanica(false, "OznakaStanice7",45.249238, 19.830213));
		stanicaRepository.save(new Stanica(false, "OznakaStanice8",45.251262, 19.837336));
		stanicaRepository.save(new Stanica(false, "OznakaStanice9",45.247879, 19.837679));
		stanicaRepository.save(new Stanica(false, "OznakaStanice10",45.246459, 19.830170));
		stanicaRepository.save(new Stanica(true, "OznakaStanice11",45.253588, 19.845533));
		stanicaRepository.save(new Stanica(true, "OznakaStanice12",45.256065, 19.841585));
		
		Stanica stanica1 = stanicaRepository.findByOznaka("OznakaStanice1");
		Stanica stanica2 = stanicaRepository.findByOznaka("OznakaStanice2");
		Stanica stanica3 = stanicaRepository.findByOznaka("OznakaStanice3");
		Stanica stanica4 = stanicaRepository.findByOznaka("OznakaStanice4");
		Stanica stanica5 = stanicaRepository.findByOznaka("OznakaStanice5");
		Stanica stanica6 = stanicaRepository.findByOznaka("OznakaStanice6");
		Stanica stanica7 = stanicaRepository.findByOznaka("OznakaStanice7");
		Stanica stanica8 = stanicaRepository.findByOznaka("OznakaStanice8");
		Stanica stanica9 = stanicaRepository.findByOznaka("OznakaStanice9");
		Stanica stanica10 = stanicaRepository.findByOznaka("OznakaStanice10");
		Stanica stanica11 = stanicaRepository.findByOznaka("OznakaStanice11");
		Stanica stanica12 = stanicaRepository.findByOznaka("OznakaStanice12");
		
		StanicaULiniji sul1 = new StanicaULiniji();
		StanicaULiniji sul2 = new StanicaULiniji();
		StanicaULiniji sul3 = new StanicaULiniji();
		StanicaULiniji sul4 = new StanicaULiniji();
		StanicaULiniji sul5 = new StanicaULiniji();
		StanicaULiniji sul6 = new StanicaULiniji();
		StanicaULiniji sul7 = new StanicaULiniji();
		StanicaULiniji sul8 = new StanicaULiniji();
		StanicaULiniji sul9 = new StanicaULiniji();
		StanicaULiniji sul10 = new StanicaULiniji();
		StanicaULiniji sul11 = new StanicaULiniji();
		StanicaULiniji sul12 = new StanicaULiniji();
		StanicaULiniji sul13 = new StanicaULiniji();
		StanicaULiniji sul14 = new StanicaULiniji();
		StanicaULiniji sul15 = new StanicaULiniji();
		StanicaULiniji sul16 = new StanicaULiniji();
		StanicaULiniji sul17 = new StanicaULiniji();
		StanicaULiniji sul18 = new StanicaULiniji();
		StanicaULiniji sul19 = new StanicaULiniji();

		sul1.setLinija(linija1);
		sul1.setStanica(stanica1);
		
		sul2.setLinija(linija1);
		sul2.setStanica(stanica3);
		
		sul3.setLinija(linija1);
		sul3.setStanica(stanica3);
		
		sul4.setLinija(linija1);
		sul4.setStanica(stanica4);
		
		sul5.setLinija(linija1);
		sul5.setStanica(stanica5);
		
		sul6.setLinija(linija2);
		sul6.setStanica(stanica1);
		
		sul7.setLinija(linija2);
		sul7.setStanica(stanica2);
		
		sul8.setLinija(linija2);
		sul8.setStanica(stanica6);
		
		sul9.setLinija(linija2);
		sul9.setStanica(stanica7);
		
		sul10.setLinija(linija3);
		sul10.setStanica(stanica7);
		
		sul11.setLinija(linija3);
		sul11.setStanica(stanica8);
		
		sul12.setLinija(linija3);
		sul12.setStanica(stanica9);
		
		sul13.setLinija(linija4);
		sul13.setStanica(stanica7);
		
		sul14.setLinija(linija4);
		sul14.setStanica(stanica9);
		
		sul15.setLinija(linija4);
		sul15.setStanica(stanica10);
		
		sul16.setLinija(linija5);
		sul16.setStanica(stanica7);
		
		sul17.setLinija(linija5);
		sul17.setStanica(stanica8);
		
		sul18.setLinija(linija5);
		sul18.setStanica(stanica9);
		
		sul19.setLinija(linija5);
		sul19.setStanica(stanica10);
	
		
		sulRepository.save(sul1);
		sulRepository.save(sul2);
		sulRepository.save(sul3);
		sulRepository.save(sul4);
		sulRepository.save(sul5);
		sulRepository.save(sul6);	
		sulRepository.save(sul7);
		sulRepository.save(sul8);
		sulRepository.save(sul9);
		sulRepository.save(sul10);
		sulRepository.save(sul11);
		sulRepository.save(sul12);
		sulRepository.save(sul13);
		sulRepository.save(sul14);
		sulRepository.save(sul15);
		sulRepository.save(sul16);
		sulRepository.save(sul17);
		sulRepository.save(sul18);
		sulRepository.save(sul19);
		
		
		Calendar vreme=Calendar.getInstance();
		vreme.set(2019, 0, 2, 0, 0);
		RedVoznje redVoznje1=new RedVoznje(false, vreme.getTime());
		redVoznjeRepository.save(redVoznje1);
		
		vreme.set(2018, 11, 1, 0, 0);
		redVoznjeRepository.save(new RedVoznje(true, vreme.getTime()));
		
		vreme.set(2019, 10, 2, 0, 0);
		RedVoznje redVoznje3=new RedVoznje(false,vreme.getTime());
		redVoznjeRepository.save(redVoznje3);
		
		
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija1, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija1, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija1, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija2, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija2, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija2, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija3, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija3, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija3, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija4, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija4, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija4, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija5, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija5, redVoznje1, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija5, redVoznje1, false));
	
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija1, redVoznje3, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija1, redVoznje3, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija1, redVoznje3, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.RADNI, linija2, redVoznje3, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.SUBOTA, linija2, redVoznje3, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija2, redVoznje3, false));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija2, redVoznje3, true));
		rasporedVoznjeRepository.save(new RasporedVoznje(DanUNedelji.NEDELJA, linija2, redVoznje3, true));
		
		seedRasporedVoznjeVremena();
	}

	public void seedKarte(){
		Korisnik korisnik = new Korisnik("MarinaTest","$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "Marina", "Jerkovic","e@gmail.com", "",StatusKorisnika.valueOf("STUDENT") , new ArrayList<>());
		 korisnik= osobaRepository.save(korisnik);
		
		
		LocalDate cd = LocalDate.now();
		Linija linija1=linijaRepository.findByNaziv("NazivLinije1");
		
		Zona zona1 = zonaRepository.findByNaziv("NazivZone1");
		
		//proci ce za linije 1,2
		VisednevnaKarta mesecna = new VisednevnaKarta(TipKarte.MESECNA, zona1, true, TipVozila.AUTOBUS, "kod1", Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()),900.0,korisnik);
		kartaRepository.save(mesecna);
		
		//proci ce za linije 1,2
		VisednevnaKarta godisnja1 = new VisednevnaKarta(TipKarte.GODISNJA, zona1, true, TipVozila.METRO, "kod2", Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000.0,korisnik);
		kartaRepository.save(godisnja1);
		
		VisednevnaKarta godisnja2 = new VisednevnaKarta(TipKarte.GODISNJA, zona1, false, TipVozila.TRAMVAJ, "kod3", Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000.0,korisnik);
		kartaRepository.save(godisnja2);
		
		
		DnevnaKarta dnevna1 = new DnevnaKarta(false,linija1,TipVozila.METRO,"kod4",new Date(),60.0,korisnik);
		kartaRepository.save(dnevna1);
		
		DnevnaKarta dnevna2 = new DnevnaKarta(true,linija1,TipVozila.AUTOBUS,"kod5",new Date(),120.0,korisnik);
		kartaRepository.save(dnevna2);
	}
	
	public void seedOdobreneNeodobreneKarte(){
		Korisnik korisnik = (Korisnik)osobaRepository.findByKorIme("MarinaTest");
		
		Linija linija2=linijaRepository.findByNaziv("NazivLinije2");
		
		Calendar calendar = Calendar.getInstance();
		
		Zona zona2 = zonaRepository.findByNaziv("NazivZone2");
		
		//odobrena i datum prosao
		calendar.set(2018, 11, 31);
		VisednevnaKarta mesecna1 = new VisednevnaKarta(TipKarte.MESECNA, zona2, true, TipVozila.AUTOBUS, "kod6", calendar.getTime(),600.0,korisnik);
		kartaRepository.save(mesecna1);
		
		//neodobrena i datum nije prosao
		calendar.set(2019, 3, 13);
		VisednevnaKarta mesecna2 = new VisednevnaKarta(TipKarte.MESECNA, zona2, null, TipVozila.METRO, "kod7", calendar.getTime(),700.0,korisnik);
		kartaRepository.save(mesecna2);
		
		//neodobrena i datum prosao
		calendar.set(2018, 11, 31);
		VisednevnaKarta mesecna3 = new VisednevnaKarta(TipKarte.MESECNA, zona2, null, TipVozila.TRAMVAJ, "kod8", calendar.getTime(),800.0,korisnik);
		kartaRepository.save(mesecna3);
		
		
		//ponistena i datum nije prosao
		calendar.set(2019, 11, 31);
		VisednevnaKarta mesecna4 = new VisednevnaKarta(TipKarte.MESECNA, zona2, false, TipVozila.METRO, "kod9", calendar.getTime(),900.0,korisnik);
		kartaRepository.save(mesecna4);
		
		
		//neodobrena i datum nije prosao
		calendar.set(2019, 4, 13);
		VisednevnaKarta mesecna5 = new VisednevnaKarta(TipKarte.MESECNA, zona2, null, TipVozila.TRAMVAJ, "kod10", calendar.getTime(),1000.0,korisnik);
		kartaRepository.save(mesecna5);
		
		//odobrena i datum nije prosao
		calendar.set(2019, 3, 13);
		VisednevnaKarta mesecna6 = new VisednevnaKarta(TipKarte.MESECNA, zona2, true, TipVozila.AUTOBUS, "kod11", calendar.getTime(),700.0,korisnik);
		kartaRepository.save(mesecna6);
		
		//odobrena i datum nije prosao
		calendar.set(2019, 3, 13);
		VisednevnaKarta mesecna7 = new VisednevnaKarta(TipKarte.MESECNA, zona2, true, TipVozila.METRO, "kod12", calendar.getTime(),700.0,korisnik);
		kartaRepository.save(mesecna7);
		
		
		//odobrena i datum nije prosao
		calendar.set(2019, 3, 13);
		VisednevnaKarta mesecna8 = new VisednevnaKarta(TipKarte.MESECNA, zona2, true, TipVozila.TRAMVAJ, "kod13", calendar.getTime(),700.0,korisnik);
		kartaRepository.save(mesecna8);
		
		
	}
}


