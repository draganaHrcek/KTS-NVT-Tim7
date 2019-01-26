package tim7.TIM7.services;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.CertPathTrustManagerParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.OdgovorDTO;
import tim7.TIM7.dto.OdobrenjeKarteDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarte;
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
	
	@Autowired
	KartaService kartaService;
	
	@Autowired
	OsobaService osobaService;
	
	@Autowired
	ZonaService zonaService;
	
	@Autowired
	LinijaService linijaService;
	
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
	
	public ArrayList<KartaDTO> findAllUserTickets(Korisnik kor){
		List<KartaDTO> karteDTO = new ArrayList<>();
		for (Karta k : kor.getKarte()) {
			KartaDTO kartaDTO = new KartaDTO();
			kartaDTO.setCena(k.getCena());
			kartaDTO.setDatumIsteka(k.getDatumIsteka());
			kartaDTO.setTipPrevoza(k.getTipPrevoza().toString());
			kartaDTO.setKod(k.getKod());
			if(k instanceof DnevnaKarta) {
				kartaDTO.setTipKarte("DNEVNA");
				kartaDTO.setLinijaZona(((DnevnaKarta)k).getLinija().getNaziv());
				kartaDTO.setCekiranaDnevnaKarta(((DnevnaKarta) k).isUpotrebljena());
			} else {
				kartaDTO.setLinijaZona(((VisednevnaKarta)k).getZona().getNaziv());
				kartaDTO.setTipKarte(((VisednevnaKarta)k).getTip().toString());
				kartaDTO.setStatusKorisnika(((VisednevnaKarta)k).getTipKorisnika().toString());
				kartaDTO.setOdobrenaKupovina(((VisednevnaKarta)k).isOdobrena());
			}
			karteDTO.add(kartaDTO);
		}
		
		return (ArrayList<KartaDTO>) karteDTO;
	}
	public double cenaKarte(KartaDTO karta, Korisnik kor) {
		
		Cenovnik cenovnik=  cenovnikService.getTrenutni();
		double cena= 0;
		for (StavkaCenovnika i : cenovnik.getStavke()) {
			if(i.getStavka().getVrstaPrevoza().toString().equals(karta.getTipPrevoza()) && i.getStavka().getTipKarte().toString().equals(karta.getTipKarte()) ) {
				if ( i.getStavka().getLinija().getNaziv().equals(karta.getLinijaZona()) || i.getStavka().getZona().getNaziv().equals(karta.getLinijaZona())) {
					cena=i.getCena();
					break;
					
				}
				
			}
		}
		
		if(karta.getTipKarte().equals("DNEVNA")) {
			cena=cena;
			
		}else {
		
			if (StatusKorisnika.STUDENT.equals(kor.getStatus())) {
				cena=cena*(100-cenovnik.getPopustStudent())/100;
				
			}else if(StatusKorisnika.PENZIONER.equals(kor.getStatus())){
				cena=cena*(100-cenovnik.getPopustPenzioner())/100;	
				
			}else if(StatusKorisnika.DJAK.equals(kor.getStatus())){
				cena=cena*(100-cenovnik.getPopustDjak())/100;
				
				
			}else if(StatusKorisnika.NEZAPOSLEN.equals(kor.getStatus())){
				
				cena=cena*(100-cenovnik.getPopustNezaposlen())/100;
				
				
			}
		}
		return cena;
	}
	public void createNewTicket(KartaDTO karta, Korisnik kor, double cena ) {
		
if (karta.getTipKarte().equals("DNEVNA")) {
			
			DnevnaKarta k= new DnevnaKarta ();
			k.setDatumIsteka(Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).toInstant(ZoneOffset.UTC)));
			k.setTipPrevoza(TipVozila.valueOf(karta.getTipPrevoza()));
			k.setLinija(linijaService.findByName(karta.getLinijaZona()));
			k.setCena(cena);
			k.setKod((UUID.randomUUID().toString()).substring(0, 7));
			k.setKorisnik(kor);
			kor.getKarte().add(k);
			kartaService.save(k);
			osobaService.save(kor);			
		}else {
			
			VisednevnaKarta k= new VisednevnaKarta ();
			LocalDate cd = LocalDate.now();
			if (karta.getTipKarte().equals("MESECNA")) {
				k.setDatumIsteka(Date.from(cd.withDayOfMonth(cd.getMonth().length(cd.isLeapYear())).atStartOfDay(ZoneId.systemDefault()).toInstant()));
				k.setTip(TipKarte.MESECNA);
			
			}else {
				k.setDatumIsteka(Date.from(cd.with(lastDayOfYear()).atStartOfDay(ZoneId.systemDefault()).toInstant()));
				k.setTip(TipKarte.GODISNJA);
				
			}
			
			k.setTipKorisnika(StatusKorisnika.valueOf(karta.getStatusKorisnika()));
			k.setTipPrevoza(TipVozila.valueOf(karta.getTipPrevoza()));
			k.setZona(zonaService.findByName(karta.getLinijaZona()));
			k.setCena(cena);
			k.setKod((UUID.randomUUID().toString()).substring(0, 7));
			
			
			
			k.setKorisnik(kor);
			kor.getKarte().add(k);
			kartaService.save(k);
			osobaService.save(kor);
		}
		
	}
	
	public boolean kartaExist(KartaDTO karta, Korisnik kor) {
		
		List<Karta> karte= kor.getKarte();
		boolean kupljena= false;
		
		for (Karta k : karte) {
			
			
			if ((k instanceof VisednevnaKarta)) {
				if (karta.getTipKarte().equals(((VisednevnaKarta) k).getTip().toString())   &&  karta.getTipPrevoza().equals(((VisednevnaKarta) k).getTipPrevoza().toString())  && ((VisednevnaKarta)k).getZona().getNaziv().equals(karta.getLinijaZona())&& (((VisednevnaKarta)k).isOdobrena() == null || ((VisednevnaKarta)k).isOdobrena() != false)) {
		
					if (LocalDateTime.now().isBefore(LocalDateTime.ofInstant(k.getDatumIsteka().toInstant(), ZoneId.systemDefault()))) {
						
						System.out.println(LocalDateTime.now().isBefore(LocalDateTime.ofInstant(k.getDatumIsteka().toInstant(), ZoneId.systemDefault())));
						kupljena=true;
						break;
						
					}
					
				}	
			}	
		}
		return kupljena;
		
	}
	
	//za proveru postojanja karte i cekiranje ukoliko je dnevna
	public OdgovorDTO checkKarta(TipVozila tipVozila, String nazivLinije, String kod){
		Karta karta=kartaRepository.findByKod(kod);
		OdgovorDTO odgovor=new OdgovorDTO();
		if (karta==null){
			return null;
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
					odgovor.setOdgovor("Karta je vazeca");;
				}else{
					return null;
				}
			}else{
				if (karta.getTipPrevoza().equals(tipVozila) && ((DnevnaKarta)karta).getLinija().getNaziv().equals(nazivLinije)){
					if (((DnevnaKarta)karta).isUpotrebljena()){
						return null;
					}else{
						((DnevnaKarta)karta).setUpotrebljena(true);
						save((DnevnaKarta)karta);
						odgovor.setOdgovor("Karta uspesno cekirana");;
					}				
				}else{
					return null;
				}
			}
			return odgovor;
		}
	}
	
	
	//za odobrenje karata
	public OdgovorDTO verifyKarta(Long idKarte, String statusOdobrenja){
		VisednevnaKarta karta=(VisednevnaKarta)findOne(idKarte);
		if (karta==null){
			return null;
		}else{
			if (karta.isOdobrena()!=null){
				return null;
			}else if ((new Date()).before(karta.getDatumIsteka())){
				String status=null;
				if (statusOdobrenja.equals("ODOBRENA")){
					karta.setOdobrena(true);
					status="odobrena";
				}else{
					karta.setOdobrena(false);
					status="ponistena";
				}
				save(karta);
				return new OdgovorDTO("Karta je uspesno "+status);
			}else{
				return null; //ne moze da je verifikuje ukoliko je karta istekla
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
