package tim7.TIM7.model;

import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

	//popust izrazen u procentima npr 20 %
	 
	@Column(name="popust_student")
	Integer popustStudent;
	@Column(name="popust_penzioner")
	Integer popustPenzioner;
	@Column(name="popust_djak")
	Integer popustDjak;
	@Column(name="popust_nezaposlen")
	Integer popustNezaposlen;
	
	
	
	public void setPopustStudent(Integer popustStudent) {
		this.popustStudent = popustStudent;
	}

	public void setPopustPenzioner(Integer popustPenzioner) {
		this.popustPenzioner = popustPenzioner;
	}

	public void setPopustDjak(Integer popustDjak) {
		this.popustDjak = popustDjak;
	}

	public void setPopustNezaposlen(Integer popustNezaposlen) {
		this.popustNezaposlen = popustNezaposlen;
	}

	public int getPopustStudent() {
		return popustStudent;
	}

	public int getPopustPenzioner() {
		return popustPenzioner;
	}

	public int getPopustDjak() {
		return popustDjak;
	}

	public int getPopustNezaposlen() {
		return popustNezaposlen;
	}



	public Cenovnik(Date datumObjavljivanja, Date datumIsteka, List<StavkaCenovnika> stavke, boolean obrisan,
			int popustStudent, int popustPenzioner, int popustDjak, int popustNezaposlen) {
		super();
		
		this.datumObjavljivanja = datumObjavljivanja;
		this.datumIsteka = datumIsteka;
		this.stavke = stavke;
		this.obrisan = obrisan;
		this.popustStudent = popustStudent;
		this.popustPenzioner = popustPenzioner;
		this.popustDjak = popustDjak;
		this.popustNezaposlen = popustNezaposlen;
	}

	public Cenovnik() {
		super();
		this.obrisan= false;
		this.stavke = new ArrayList<StavkaCenovnika>();
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
