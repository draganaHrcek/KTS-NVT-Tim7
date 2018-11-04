package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Zona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="oznaka")
	String oznaka;
	
	@ManyToMany(mappedBy="zone")
	List<Linija> linije;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<Stavka> stavkeCenovnika;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<VisednevnaKarta> karte;
}
