package tim7.TIM7.services;

import java.util.List;

import javax.net.ssl.CertPathTrustManagerParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.repositories.KartaRepository;

@Service
public class KartaService {

	@Autowired
	KartaRepository kartaRepository;
	
	
	@Autowired
	CenovnikService cenovnikService;
	
	public Karta findOne(Long id) {
		return kartaRepository.findById(id).get();
	}

	public List<Karta> findAll() {
		return kartaRepository.findAll();
	}


	public Karta save(Karta karta) {
		return kartaRepository.save(karta);
	}

	public void delete(Long id) {
		Karta karta=findOne(id);
		karta.setObrisan(true);
		save(karta);
	}
	public double cenaKarte(KartaDTO karta, Korisnik kor) {
		
		Cenovnik cenovnik=  cenovnikService.findAll().stream().findFirst().get();
		double cena= 0;
		for (StavkaCenovnika i : cenovnik.getStavke()) {
			if(i.getStavka().getVrstaPrevoza().toString().equals(karta.getTipPrevoza()) && i.getStavka().getTipKarte().toString().equals(karta.getTipKarte()) ) {
				if ( i.getStavka().getLinija().getNaziv().equals(karta.getLinijaZona()) || i.getStavka().getZona().getNaziv().equals(karta.getLinijaZona())) {
					cena=i.getCena();
					break;
					
				}
				
			}
		}
		
		if (kor.getStatus().equals(StatusKorisnika.STUDENT)) {
			cena=cena*(100-cenovnik.getPopustStudent())/100;
			
		}else if(kor.getStatus().equals(StatusKorisnika.PENZIONER) ){
			cena=cena*(100-cenovnik.getPopustPenzioner())/100;	
			
		}else if(kor.getStatus().equals(StatusKorisnika.DJAK) ){
			cena=cena*(100-cenovnik.getPopustDjak())/100;
			
			
		}else if(kor.getStatus().equals(StatusKorisnika.NEZAPOSLEN) ){
			
			cena=cena*(100-cenovnik.getPopustNezaposlen())/100;
			
			
		}
		return cena;
	}

}
