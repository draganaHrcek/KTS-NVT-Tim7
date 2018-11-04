package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Stavka {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="tip_karte")
	TipKarte tipKarte;
	
	@Column(name="vrsta_prevoza")
	TipVozila vrstaPrevoza;
	
	//ako je karta dnevna zona je null, za sve ostale tipove cena zavisi i od zone
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Zona zona;
	
	@OneToMany(mappedBy = "stavka", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<StavkaCenovnika> stavkeCenovnika;
}
