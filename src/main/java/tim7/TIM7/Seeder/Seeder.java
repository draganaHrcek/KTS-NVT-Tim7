package tim7.TIM7.Seeder;

import java.time.LocalTime;
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
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.OsobaRepository;
import tim7.TIM7.repositories.RasporedVoznjeRepository;
import tim7.TIM7.repositories.RedVoznjeRepository;
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
	RedVoznjeRepository redVoznjeRepository;
	
	@Autowired
	RasporedVoznjeRepository rasporedVoznjeRepository;

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
//		seedStanica();
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

	public void seedVozilo() {

	}
	
	public void seedStanica() {
		
	}

	public void seedOsoba() {
		//pass je '12345678'
		Administrator admin = new Administrator("a", "$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "Admin", "Adminovic","a@gmail.com");
		Korisnik korisnik = new Korisnik("e","$2a$10$Vc0ucRlZKZwApbjZNZUmduCL2dZ.T1152UQuEpglLAkpYmLt6vxK6", "Elena", "Roncevic","e@gmail.com", "", null, new ArrayList<>());
		
		osobaRepository.save(korisnik);
		osobaRepository.save(admin);
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

}
