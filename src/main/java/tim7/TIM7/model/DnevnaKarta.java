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
public class DnevnaKarta extends Karta {
	
	@Column(name = "upotrebljena")
	boolean upotrebljena;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Linija linija;

	

	public DnevnaKarta() {
		super();
		this.upotrebljena= false;
		
		// TODO Auto-generated constructor stub
	}



	public boolean isUpotrebljena() {
		return upotrebljena;
	}

	public void setUpotrebljena(boolean upotrebljena) {
		this.upotrebljena = upotrebljena;
	}

	public Linija getLinija() {
		return linija;
	}

	public void setLinija(Linija linija) {
		this.linija = linija;
	}



	public DnevnaKarta(boolean upotrebljena, Linija linija, TipVozila tipVozila, String kod, Date datumIsteka, Double cena, Korisnik korisnik) {
		super(tipVozila, kod, datumIsteka, cena, korisnik);
		this.upotrebljena = upotrebljena;
		this.linija = linija;
	}
	
	
	
}
