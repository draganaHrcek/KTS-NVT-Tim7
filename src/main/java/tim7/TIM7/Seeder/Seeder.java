package tim7.TIM7.Seeder;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Kondukter;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.model.Verifikator;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.KartaRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.OsobaRepository;
import tim7.TIM7.repositories.RasporedVoznjeRepository;
import tim7.TIM7.repositories.RedVoznjeRepository;
import tim7.TIM7.repositories.StanicaRepository;
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
//		seedStanica();
//		seedZoneLinijeStaniceRedoviRasporediVoznje();
//		seedKarte();
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
			linijaRepository.save(new Linija("linija " + i, false));
		}
	}

	public void seedZona() {
		zonaRepository.save(new Zona("gradksa", false));
		zonaRepository.save(new Zona("prigradska", false));
		zonaRepository.save(new Zona("veternik", false));

	}

	public void connectZonaLinija() {
		Zona gradska = zonaRepository.findByNaziv("graksa");
		Zona prigradska = zonaRepository.findByNaziv("prigradska");
		Zona veternik = zonaRepository.findByNaziv("veternik");

		Linija linija = linijaRepository.findByNaziv("linija 1");
		linija.getZone().add(gradska);
		linijaRepository.save(linija);

		linija = linijaRepository.findByNaziv("linija 2");
		linija.getZone().add(gradska);
		linija.getZone().add(prigradska);
		linijaRepository.save(linija);

		linija = linijaRepository.findByNaziv("linija 3");
		linija.getZone().add(veternik);
		linijaRepository.save(linija);

		linija = linijaRepository.findByNaziv("linija 4");
		linija.getZone().add(gradska);
		linija.getZone().add(prigradska);
		linija.getZone().add(veternik);
		linijaRepository.save(linija);
	}

	public void seedStavkaCenovnika() {
		Cenovnik cenovnik = cenovnikRepository.findAll().get(0);
		int i = 0;
		for (Stavka stavka : stavkaRepository.findAll()) {
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
			linija = linijaRepository.findByNaziv("linija 1");
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, gradska, linija, false));

			linija = linijaRepository.findByNaziv("linija 2");
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, prigradska, linija, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, gradska, linija, false));

			linija = linijaRepository.findByNaziv("linija 3");
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, veternik, linija, false));

			linija = linijaRepository.findByNaziv("linija 4");
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, prigradska, linija, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, gradska, linija, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, veternik, linija, false));
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
		
		osobaRepository.save(korisnikTest);
		osobaRepository.save(adminTest);
		osobaRepository.save(verifikatorTest);
		osobaRepository.save(kondukterTest);
		
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
		
		linija1.getZone().add(zona1);
		linija1.getZone().add(zona2);
		linija1.getZone().add(zona3);
		linijaRepository.save(linija1);
		
		linija2.getZone().add(zona1);
		linija2.getZone().add(zona2);
		linijaRepository.save(linija2);
		
		linija3.getZone().add(zona3);
		linijaRepository.save(linija3);
		
		linija4.getZone().add(zona2);
		linija4.getZone().add(zona4);
		linijaRepository.save(linija4);
		
		linija5.getZone().add(zona3);
		linija5.getZone().add(zona5);
		linijaRepository.save(linija5);
		
		linija6.getZone().add(zona2);
		linijaRepository.save(linija6);
		
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
		
		stanica1.getLinije().add(linija1);
		stanica1.getLinije().add(linija2);
		stanicaRepository.save(stanica1);
		
		stanica2.getLinije().add(linija1);
		stanica2.getLinije().add(linija2);
		stanicaRepository.save(stanica2);
		
		stanica3.getLinije().add(linija1);
		stanicaRepository.save(stanica3);
		
		stanica4.getLinije().add(linija1);
		stanicaRepository.save(stanica4);
		
		stanica5.getLinije().add(linija1);
		stanicaRepository.save(stanica5);
		
		
		stanica6.getLinije().add(linija2);
		stanicaRepository.save(stanica6);
		
		
		stanica7.getLinije().add(linija2);
		stanica7.getLinije().add(linija3);
		stanica7.getLinije().add(linija4);
		stanica7.getLinije().add(linija5);
		stanicaRepository.save(stanica7);
		
		
		stanica8.getLinije().add(linija3);
		stanica8.getLinije().add(linija5);
		stanica8.getLinije().add(linija6);
		stanicaRepository.save(stanica8);

		
		stanica9.getLinije().add(linija3);
		stanica9.getLinije().add(linija4);
		stanica9.getLinije().add(linija5);
		stanica9.getLinije().add(linija6);
		stanicaRepository.save(stanica9);
		
		
		stanica10.getLinije().add(linija4);
		stanica10.getLinije().add(linija5);
		stanicaRepository.save(stanica10);
		
		
		stanica11.getLinije().add(linija6);
		stanicaRepository.save(stanica11);
		
		stanica12.getLinije().add(linija6);
		stanicaRepository.save(stanica12);
		
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
		Korisnik korisnik = (Korisnik)osobaRepository.findByKorIme("KorisnikTest");
		
		LocalDate cd = LocalDate.now();
		Linija linija1=linijaRepository.findByNaziv("NazivLinije1");
		
		Zona zona1 = zonaRepository.findByNaziv("NazivZone1");
		
		//proci ce za linije 1,2,3
		VisednevnaKarta mesecna = new VisednevnaKarta(TipKarte.MESECNA, zona1, true, TipVozila.AUTOBUS, "kod1", Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()),900.0,korisnik);
		kartaRepository.save(mesecna);
		
		//proci ce za linije 1,2,3
		VisednevnaKarta godisnja1 = new VisednevnaKarta(TipKarte.GODISNJA, zona1, true, TipVozila.METRO, "kod2", Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000.0,korisnik);
		kartaRepository.save(godisnja1);
		
		VisednevnaKarta godisnja2 = new VisednevnaKarta(TipKarte.GODISNJA, zona1, false, TipVozila.TRAMVAJ, "kod3", Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000.0,korisnik);
		kartaRepository.save(godisnja2);
		
		
		DnevnaKarta dnevna1 = new DnevnaKarta(false,linija1,TipVozila.METRO,"kod4",new Date(),60.0,korisnik);
		kartaRepository.save(dnevna1);
		
		DnevnaKarta dnevna2 = new DnevnaKarta(true,linija1,TipVozila.AUTOBUS,"kod5",new Date(),120.0,korisnik);
		kartaRepository.save(dnevna2);
	}
}
