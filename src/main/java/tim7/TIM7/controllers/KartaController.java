package tim7.TIM7.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.dto.KartaDTO;
import tim7.TIM7.model.DnevnaKarta;
import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.VisednevnaKarta;
import tim7.TIM7.security.TokenUtils;
import tim7.TIM7.services.KartaService;
import tim7.TIM7.services.OsobaService;

@RestController
@RequestMapping("/karte")
public class KartaController {

	@Autowired
	KartaService kartaService;

	@Autowired
	OsobaService osobaService;
	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value="/izlistajKarte", consumes = "application/json" ,method = RequestMethod.GET)
	public ResponseEntity<List<KartaDTO>> izlistajKarte(@RequestHeader ("X-Auth-Token") String token ) {
		Korisnik kor = (Korisnik)osobaService.findByUsername(tokenUtils.getUsernameFromToken(token));
		List<KartaDTO> karteDTO = new ArrayList<>();
		for (Karta k : kor.getKarte()) {
			KartaDTO kartaDTO = new KartaDTO();
			kartaDTO.setCena(k.getCena());
			kartaDTO.setDatumIsteka(k.getDatumIsteka());
			kartaDTO.setTipPrevoza(k.getTipPrevoza().toString());
			if(k instanceof DnevnaKarta) {
				kartaDTO.setTipKarte("DNEVNA");
				kartaDTO.setTipPrevoza(((DnevnaKarta) k).getLinija().getNaziv());
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
	
	
}
