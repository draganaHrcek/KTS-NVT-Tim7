package tim7.TIM7.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class LokacijaStanice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Column(name="naziv_mesta")
	String nazivMesta;
	
	@Column(name="longituda")
	double longituda;
	
	@Column(name="latituda")
	double latituda;
	
	@OneToOne(fetch=FetchType.LAZY )
	Stanica stanica;
	
}
