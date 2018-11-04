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
}
