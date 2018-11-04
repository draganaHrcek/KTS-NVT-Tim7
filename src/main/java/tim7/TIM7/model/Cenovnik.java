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

	

	public Cenovnik() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}



	public Date getDatumObjavljivanja() {
		return datumObjavljivanja;
	}

	public void setDatumObjavljivanja(Date datumObjavljivanja) {
		this.datumObjavljivanja = datumObjavljivanja;
	}

	public Date getDatumIsteka() {
		return datumIsteka;
	}

	public void setDatumIsteka(Date datumIsteka) {
		this.datumIsteka = datumIsteka;
	}

	public List<StavkaCenovnika> getStavke() {
		return stavke;
	}

	public void setStavke(List<StavkaCenovnika> stavke) {
		this.stavke = stavke;
	}
	
	
}
