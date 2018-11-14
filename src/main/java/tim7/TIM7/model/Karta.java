package tim7.TIM7.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public abstract class Karta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="tip_prevoza")
	TipVozila tipPrevoza;
	
	public TipVozila getTipPrevoza() {
		return tipPrevoza;
	}



	public void setTipPrevoza(TipVozila tipPrevoza) {
		this.tipPrevoza = tipPrevoza;
	}

	@Column(name = "datum_isteka")
	Date datumIsteka;
	
	@Column(name = "cena")
	Double cena;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Korisnik korisnik;

	@Column(name="obrisan")
	boolean obrisan;
	
	public Karta() {
		super();
		this.obrisan=false;	
		// TODO Auto-generated constructor stub
	}



	public boolean isObrisan() {
		return obrisan;
	}



	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}



	public Long getId() {
		return id;
	}

	

	public Date getDatumIsteka() {
		return datumIsteka;
	}

	public void setDatumIsteka(Date datumIsteka) {
		this.datumIsteka = datumIsteka;
	}

	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
		this.cena = cena;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	
	

}
