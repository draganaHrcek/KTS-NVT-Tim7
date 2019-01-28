package tim7.TIM7.model;

import java.util.ArrayList;
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
public class Stanica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="oznaka")
	String oznaka;
	
	@Column(name="longituda")
	double longituda;
	
	@Column(name="latituda")
	double latituda;
	
	@OneToMany(mappedBy = "stanica", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	List<StanicaULiniji> linije; 
	
	@Column(name="obrisan")
	boolean obrisan;
	public Stanica() {
		super();
		this.obrisan= false;
		this.linije = new ArrayList<StanicaULiniji>();
		// TODO Auto-generated constructor stub
	}
	
	



	public Stanica(Long id, String oznaka, double longituda, double latituda, List<StanicaULiniji> linijeStanice,
			boolean obrisan) {
		super();
		this.id = id;
		this.oznaka = oznaka;
		this.longituda = longituda;
		this.latituda = latituda;
		this.linije = linijeStanice;
		this.obrisan = obrisan;
	}





	public Stanica(boolean obrisan, String oznaka, double latituda, double longituda) {
		super();
		this.oznaka = oznaka;
		this.longituda = longituda;
		this.latituda = latituda;
		this.obrisan = obrisan;
		this.linije = new ArrayList<StanicaULiniji>();
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

	

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}




	public List<StanicaULiniji> getLinije() {
		return linije;
	}





	public void setLinije(List<StanicaULiniji> linijeStanice) {
		this.linije = linijeStanice;
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

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
}
