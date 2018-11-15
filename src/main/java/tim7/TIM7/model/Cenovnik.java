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

import org.hibernate.annotations.Where;

//uvek bi trebao da bude aktivan 1 cenovnik, ko se bude ovim bavio nek stavi kada se dodaje novi cenovnik neka se logicki obrise stari, ili dodati neku metodu koja ce kad se pokrene proveriti datume isteka svih cenovnika i pobrisati stare 
@Where(clause= "obrisan = 'false' ")
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

	@Column(name="obrisan")
	boolean obrisan;

	public Cenovnik() {
		super();
		this.obrisan= false;
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
