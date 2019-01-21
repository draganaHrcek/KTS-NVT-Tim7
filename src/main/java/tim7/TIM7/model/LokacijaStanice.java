package tim7.TIM7.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

	@Column(name="obrisan")
	boolean obrisan;
	
	public LokacijaStanice() {
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



	public String getNazivMesta() {
		return nazivMesta;
	}


	public void setNazivMesta(String nazivMesta) {
		this.nazivMesta = nazivMesta;
	}


	public double getLongituda() {
		return longituda;
	}


	public void setLongituda(double longituda) {
		this.longituda = longituda;
	}


	public double getLatituda() {
		return latituda;
	}


	public void setLatituda(double latituda) {
		this.latituda = latituda;
	}


	public Stanica getStanica() {
		return stanica;
	}


	public void setStanica(Stanica stanica) {
		this.stanica = stanica;
	}
	
	
	
}
