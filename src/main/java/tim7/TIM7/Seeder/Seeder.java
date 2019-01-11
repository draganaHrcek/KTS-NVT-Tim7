package tim7.TIM7.Seeder;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.OsobaRepository;
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

	public Seeder() {
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		//seedCenovnik();
		//seedZona();
		//seedLinija();
		//connectZonaLinija();
		//seedStavka();
		//seedStavkaCenovnika();
	}

	public void seedCenovnik() {
		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();

		before.set(2018, 9, 1);
		after.set(2019, 6, 30);
		Cenovnik trenutni = new Cenovnik(before.getTime(), after.getTime(), false);

		after.set(2019, 0, 1);
		Cenovnik prosli = new Cenovnik(before.getTime(), after.getTime(), false);

		before.setTime(after.getTime());
		after.set(2019, 9, 1);
		Cenovnik buduci = new Cenovnik(before.getTime(), after.getTime(), false);

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

			linija = linijaRepository.findByNaziv("linija 2");
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, prigradska, linija, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, gradska, linija, false));
			stavkaRepository.save(new Stavka(tip, TipVozila.AUTOBUS, veternik, linija, false));
		}
	}

	public void seedVozilo() {
	}

	public void seedOsoba() {
	}

}
