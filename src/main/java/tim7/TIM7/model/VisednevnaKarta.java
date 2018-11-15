package tim7.TIM7.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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
	boolean odobrena;

	
	
	
	public StatusKorisnika getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(StatusKorisnika tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	boolean isOdobrena() {
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
	
	
}
