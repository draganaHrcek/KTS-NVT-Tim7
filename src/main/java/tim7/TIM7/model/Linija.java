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
public class Linija {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="naziv")
	String naziv;
	
	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <Vozilo> vozila;
	 
	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<Stavka> stavkeCenovnika;

	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<LinijaUZoni> zone;
	
	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<StanicaULiniji> stanice;

	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <RasporedVoznje> rasporedVoznje;
	
	@Column(name="obrisan")
	boolean obrisan;

	public Linija() {
		super();
		this.obrisan= false;
		this.stanice = new ArrayList<StanicaULiniji>();
		this.zone = new ArrayList<LinijaUZoni>();
		this.vozila = new ArrayList<Vozilo>();
		// TODO Auto-generated constructor stub
	}
	
	public Linija(String naziv, boolean obrisan) {
		this.obrisan= obrisan;
		this.naziv = naziv;
		this.stanice = new ArrayList<StanicaULiniji>();
		this.zone = new ArrayList<LinijaUZoni>();
		this.vozila = new ArrayList<Vozilo>();
		// TODO Auto-generated constructor stub
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public List<RasporedVoznje> getRasporedVoznje() {
		return rasporedVoznje;
	}

	public void setRasporedVoznje(List<RasporedVoznje> rasporedVoznje) {
		this.rasporedVoznje = rasporedVoznje;
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

	public List<Stavka> getStavkeCenovnika() {
		return stavkeCenovnika;
	}
	
	public void setStavkeCenovnika(List<Stavka> stavkeCenovnika) {
		this.stavkeCenovnika = stavkeCenovnika;
	}

	public List<Vozilo> getVozila() {
		return vozila;
	}

	public void setVozila(List<Vozilo> vozila) {
		this.vozila = vozila;
	}

	

	public List<LinijaUZoni> getZone() {
		return zone;
	}

	public void setZone(List<LinijaUZoni> zoneLinije) {
		this.zone = zoneLinije;
	}

	public List<StanicaULiniji> getStanice() {
		return stanice;
	}

	public void setStanice(List<StanicaULiniji> staniceLinije) {
		this.stanice = staniceLinije;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	
		
}
