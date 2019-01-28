package tim7.TIM7.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StanicaULiniji {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Linija linija;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	Stanica stanica; 
	
	@Column
	boolean obrisan;
	
	public StanicaULiniji() {
		super();
		obrisan=false;
	}

	public StanicaULiniji(Long id, Linija linija, Stanica stanica, boolean obrisan) {
		super();
		this.id = id;
		this.linija = linija;
		this.stanica = stanica;
		this.obrisan=obrisan;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Linija getLinija() {
		return linija;
	}

	public void setLinija(Linija linija) {
		this.linija = linija;
	}

	public Stanica getStanica() {
		return stanica;
	}

	public void setStanica(Stanica stanica) {
		this.stanica = stanica;
	}

	public boolean isObrisan() {
		return obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
	
	

	
}
