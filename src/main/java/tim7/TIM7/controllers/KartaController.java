package tim7.TIM7.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.dto.OdobrenjeKarteDTO;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.security.TokenUtils;
import tim7.TIM7.services.CenovnikService;
import tim7.TIM7.services.KartaService;
import tim7.TIM7.services.LinijaService;
import tim7.TIM7.services.OsobaService;
import tim7.TIM7.services.ZonaService;

@RestController
@RequestMapping("/karte")
public class KartaController {

	@Autowired
	KartaService kartaService;


	@Autowired
	LinijaService linijaService;
	
	@Autowired
	CenovnikService cenovnikService;
	
	@Autowired
	ZonaService zonaService;

	
	@Autowired
	OsobaService osobaService;
	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value="/izlistajKarte", produces = "application/json" ,method = RequestMethod.GET)
	public ResponseEntity<List<KartaDTO>> izlistajKarte(@RequestHeader ("X-Auth-Token") String token ) {
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
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
			}
			karteDTO.add(kartaDTO);
		}
		return new ResponseEntity<>(karteDTO, HttpStatus.OK);
	}
	@RequestMapping(value="/kupovinaKarte", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity<List<KartaDTO>> kupovinaKarte(@RequestHeader ("X-Auth-Token") String token, @RequestBody KartaDTO karta )
	{
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		
		double cena= kartaService.cenaKarte(karta,kor);
		
		int length = 10;
	    boolean useLetters = true;
	    boolean useNumbers = false;
	  
	    
	    
		
		if (karta.getTipKarte().equals("DNEVNA")) {
			
			DnevnaKarta k= new DnevnaKarta ();
			k.setDatumIsteka(new Date());
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
		
		
		return new ResponseEntity<>( HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/proveriKartu/{tipVozila}/{nazivLinije}/{kod}", consumes = "application/json" ,method = RequestMethod.POST)
	public ResponseEntity<String> checkKarta(@RequestHeader ("X-Auth-Token") String token, @PathVariable String tipVozila, @PathVariable String nazivLinije, @PathVariable String kod){
		String statusProvere = kartaService.checkKarta(TipVozila.valueOf(tipVozila), nazivLinije, kod);
		if (statusProvere.equals("NE POSTOJI")){
			return new ResponseEntity<>("Ne postoji vazeca karta", HttpStatus.BAD_REQUEST);
		}else if (statusProvere.equals("POSTOJI")){
			return new ResponseEntity<>("Karta sa zadatim kodom je vazeca", HttpStatus.OK);
		}else if (statusProvere.equals("UPOTREBLJENA")){
			return new ResponseEntity<>("Karta sa zadatim kodom je vec cekirana", HttpStatus.IM_USED);
		}else{
			return new ResponseEntity<>("Karta je uspesno cekirana", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/odobriKartu/{idKarte}/{statusOdobrenja}", method = RequestMethod.POST)
	public ResponseEntity<String> verifyKarta(@RequestHeader ("X-Auth-Token") String token, @PathVariable Long idKarte, @PathVariable String statusOdobrenja){
		boolean izmenjenStatus=kartaService.verifyKarta(idKarte, statusOdobrenja);
		if (izmenjenStatus){
			return new ResponseEntity<>(HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value="/dobaviNeodobreneKarte", method = RequestMethod.GET)
	public ResponseEntity<List<OdobrenjeKarteDTO>> getNeodobreneKarte(@RequestHeader ("X-Auth-Token") String token){
		List<OdobrenjeKarteDTO> neodobreneKarte=kartaService.getNeodobreneKarte();
		if (neodobreneKarte==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return new ResponseEntity<>(neodobreneKarte, HttpStatus.OK);
		}
	}
	
}
