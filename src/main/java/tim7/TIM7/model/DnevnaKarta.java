package tim7.TIM7.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class DnevnaKarta extends Karta {
	
	@Column(name = "upotrebljena")
	boolean upotrebljena;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Linija linija;



	public DnevnaKarta() {
		super();
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
	
	
	
}
