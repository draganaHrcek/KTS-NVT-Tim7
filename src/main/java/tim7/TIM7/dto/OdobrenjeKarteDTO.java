package tim7.TIM7.dto;

import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.TipKarte;
import tim7.TIM7.model.TipVozila;

public class OdobrenjeKarteDTO {
	private Long id;
	private String korisnikUsername;
	private TipKarte tipKarte;
	private StatusKorisnika statusKorisnika;
	private String nazivZone;
	private String lokacijaDokumenta;
	private TipVozila tipVozila;
	
	public OdobrenjeKarteDTO() {
		super();
	}

	

	public OdobrenjeKarteDTO(Long id, String korisnikUsername, TipKarte tipKarte, StatusKorisnika statusKorisnika,
			String nazivZone, String lokacijaDokumenta, TipVozila tipVozila) {
		super();
		this.id = id;
		this.korisnikUsername = korisnikUsername;
		this.tipKarte = tipKarte;
		this.statusKorisnika = statusKorisnika;
		this.nazivZone = nazivZone;
		this.lokacijaDokumenta = lokacijaDokumenta;
		this.tipVozila = tipVozila;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKorisnikUsername() {
		return korisnikUsername;
	}

	public void setKorisnikUsername(String korisnikUsername) {
		this.korisnikUsername = korisnikUsername;
	}

	public TipKarte getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(TipKarte tipKarte) {
		this.tipKarte = tipKarte;
	}

	public StatusKorisnika getStatusKorisnika() {
		return statusKorisnika;
	}

	public void setStatusKorisnika(StatusKorisnika statusKorisnika) {
		this.statusKorisnika = statusKorisnika;
	}

	public String getNazivZone() {
		return nazivZone;
	}

	public void setNazivZone(String nazivZone) {
		this.nazivZone = nazivZone;
	}

	public String getLokacijaDokumenta() {
		return lokacijaDokumenta;
	}

	public void setLokacijaDokumenta(String lokacijaDokumenta) {
		this.lokacijaDokumenta = lokacijaDokumenta;
	}



	public TipVozila getTipVozila() {
		return tipVozila;
	}



	public void setTipVozila(TipVozila tipVozila) {
		this.tipVozila = tipVozila;
	}
	
	
	
}
