package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.CertPathTrustManagerParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.OdobrenjeKarteDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.model.Zona;
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
	
	
	//za proveru postojanja karte i cekiranje ukoliko je dnevna
	public String checkKarta(TipVozila tipVozila, String nazivLinije, String kod){
		Karta karta=kartaRepository.findByKod(kod);
		if (karta==null){
			return "NE POSTOJI";
		}else{
			if (karta instanceof VisednevnaKarta){
				Zona zonaKarte=((VisednevnaKarta)karta).getZona();
				boolean prosao=false;
				for (Linija linija:zonaKarte.getLinije()){
					if (linija.getNaziv().equals(nazivLinije)){
						prosao=true;
					}
				}
				if (prosao && karta.getTipPrevoza().equals(tipVozila) && (new Date()).before(karta.getDatumIsteka()) && ((VisednevnaKarta)karta).isOdobrena()){
					return "POSTOJI";
				}else{
					return "NE POSTOJI";
				}
			}else{
				if (karta.getTipPrevoza().equals(tipVozila) && ((DnevnaKarta)karta).getLinija().getNaziv().equals(nazivLinije)){
					if (((DnevnaKarta)karta).isUpotrebljena()){
						return "UPOTREBLJENA";
					}else{
						((DnevnaKarta)karta).setUpotrebljena(true);
						save((DnevnaKarta)karta);
						return "CEKIRANA";
					}				
				}else{
					return "NE POSTOJI";
				}
			}
		}
	}
	
	
	//za odobrenje karata
	public boolean verifyKarta(Long idKarte, String statusOdobrenja){
		VisednevnaKarta karta=(VisednevnaKarta)findOne(idKarte);
		if (karta==null){
			return false;
		}else{
			if (karta.isOdobrena()!=null){
				return false;
			}else if ((new Date()).before(karta.getDatumIsteka())){
				if (statusOdobrenja.equals("ODOBRENA")){
					karta.setOdobrena(true);
				}else{
					karta.setOdobrena(false);
				}
				save(karta);
				return true;
			}else{
				return false; //ne moze da je verifikuje ukoliko je karta istekla
			}
			
		}
	}
	
	public List<OdobrenjeKarteDTO> getNeodobreneKarte(){
		List<Karta> karte = findAll();
		List<OdobrenjeKarteDTO> karteZaOdobrenje = new ArrayList<OdobrenjeKarteDTO>();
		for (Karta karta : karte){
			if (karta instanceof VisednevnaKarta){
				//ne dobavljaju se karte koje su vec verifikovane, niti one kojima je datum isteka prosao
				if (((VisednevnaKarta) karta).isOdobrena()!=null || (new Date()).after(karta.getDatumIsteka())){
					continue;
				}
				OdobrenjeKarteDTO odobrenje = new OdobrenjeKarteDTO();
				odobrenje.setId(karta.getId());
				odobrenje.setKorisnikUsername(karta.getKorisnik().getKorIme());
				odobrenje.setStatusKorisnika(karta.getKorisnik().getStatus());
				odobrenje.setLokacijaDokumenta(karta.getKorisnik().getLokacijaDokumenta());
				odobrenje.setTipKarte(((VisednevnaKarta)karta).getTip());
				odobrenje.setNazivZone(((VisednevnaKarta)karta).getZona().getNaziv());				
				odobrenje.setTipVozila(karta.getTipPrevoza());
				karteZaOdobrenje.add(odobrenje);
				
			}
		}
		if (karteZaOdobrenje.size()==0){
			return null;
		}else{
			return karteZaOdobrenje;
		}
	}

}
