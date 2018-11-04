package tim7.TIM7.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Cenovnik {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="datum_objavljivanja")
	Date datumObjavljivanja;
	
	@Column(name="datum_isteka")
	Date datumIsteka;
	
	@OneToMany(mappedBy = "cenovnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<StavkaCenovnika> stavke;
	
	
}
