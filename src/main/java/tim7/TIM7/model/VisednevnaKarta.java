package tim7.TIM7.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class VisednevnaKarta extends Karta {

	
	@Column(name = "tip")
	TipKarte tip;
	
	@Column(name = "tip_Korisnika")
	StatusKorisnika tipKorisnika;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Zona zona;

	//da li je kupovina karte odobrena od strane admina
	@Column(name = "odobrena")
	Boolean odobrena;

	
	
	
	public StatusKorisnika getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(StatusKorisnika tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	public Boolean isOdobrena() {
		return odobrena;
	}

	public void setOdobrena(boolean odobrena) {
		this.odobrena = odobrena;
	}

	public VisednevnaKarta() {
		super();
		this.odobrena= false;
		// TODO Auto-generated constructor stub
	}

	public TipKarte getTip() {
		return tip;
	}

	public void setTip(TipKarte tip) {
		this.tip = tip;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}



	public VisednevnaKarta(TipKarte tip, Zona zona, Boolean odobrena, TipVozila tipVozila, String kod, Date datumIsteka, Double cena, Korisnik korisnik) {
		super(tipVozila, kod, datumIsteka, cena, korisnik);
		this.tip = tip;
		this.zona = zona;
		this.odobrena = odobrena;
	}
	
	

	
	
	
}
