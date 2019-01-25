package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.KorisnikDTO;
import tim7.TIM7.dto.KorisnikTokenDTO;
import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Kondukter;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.repositories.OsobaRepository;

@Service
public class OsobaService {

	@Autowired
	OsobaRepository osobaRepository;
	
	
	public Osoba findOne(Long id) {
		return osobaRepository.findById(id).orElse(null);
	}

	public List<Osoba> findAll() {
		return osobaRepository.findAll();
	}


	public Osoba save(Osoba osoba) {
		return osobaRepository.save(osoba);
	}

	public Osoba findByUsername(String username) {
		return osobaRepository.findByKorIme(username);
	}
	public void delete(Long id) {
		Osoba osoba=findOne(id);
		osoba.setObrisan(true);
		save(osoba);
	}
	
	public KorisnikTokenDTO findUlogovanog(Osoba o) {

		KorisnikTokenDTO kor= new KorisnikTokenDTO();
		kor.setKorIme(o.getKorIme());
		kor.setEmail(o.getEmail());
		kor.setIme(o.getIme());
		kor.setPrezime(o.getPrezime());
		
		if (o instanceof Korisnik) {
			kor.setUloga("KORISNIK");
			if(((Korisnik) o).getStatus()!=null) {
				kor.setStatus(((Korisnik) o).getStatus().toString());
			}
		}else if(o instanceof Administrator) {
			
			kor.setUloga("ADMINISTRATOR");
			
		}else if (o instanceof Kondukter) {
			kor.setUloga("KONDUKTER");
			
			
		}else {
			
			
			kor.setUloga("VERIFIKATOR");
		}
		return kor;
		
	}
	public void createNewUser(KorisnikDTO registracijaForma) {
		
		Korisnik noviKorisnik= new Korisnik();
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		noviKorisnik.setEmail(registracijaForma.getEmail());
		noviKorisnik.setIme(registracijaForma.getIme());
		noviKorisnik.setPrezime(registracijaForma.getPrezime());
		noviKorisnik.setLozinka(encoder.encode(registracijaForma.getLozinka1()));
		noviKorisnik.setKorIme(registracijaForma.getKorIme());
		noviKorisnik.setKarte(new ArrayList<Karta> ());
		noviKorisnik.setLokacijaDokumenta(null);
		noviKorisnik.setStatus(null);
		save((Korisnik)noviKorisnik);
	}
	

}
