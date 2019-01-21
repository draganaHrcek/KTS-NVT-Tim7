package tim7.TIM7.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
public class StavkaCenovnika {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="cena")
	double cena;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Stavka stavka;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Cenovnik cenovnik;
	
	@Column(name="obrisan")
	boolean obrisan;
	
	
	
	public StavkaCenovnika(double cena, Stavka stavka, Cenovnik cenovnik, boolean obrisan) {
		super();
		this.cena = cena;
		this.stavka = stavka;
		this.cenovnik = cenovnik;
		this.obrisan = obrisan;
	}

	public StavkaCenovnika() {
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


	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public Stavka getStavka() {
		return stavka;
	}

	public void setStavka(Stavka stavka) {
		this.stavka = stavka;
	}

	public Cenovnik getCenovnik() {
		return cenovnik;
	}

	public void setCenovnik(Cenovnik cenovnik) {
		this.cenovnik = cenovnik;
	}

}
