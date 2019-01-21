package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


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

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "linije_u_zonama",
    			
               joinColumns = @JoinColumn(name="linija_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="zona_id", referencedColumnName="id"))
	List<Zona> zone;
	
	@ManyToMany(mappedBy="linije")
	List<Stanica> stanice;

	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <RasporedVoznje> rasporedVoznje;
	
	@Column(name="obrisan")
	boolean obrisan;

	public Linija() {
		super();
		this.obrisan= false;
		// TODO Auto-generated constructor stub
	}
	
	public Linija(String naziv, boolean obrisan) {
		this.obrisan= obrisan;
		this.naziv = naziv;
		// TODO Auto-generated constructor stub
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

	public List<Zona> getZone() {
		return zone;
	}

	public void setZone(List<Zona> zone) {
		this.zone = zone;
	}

	public List<Stanica> getStanice() {
		return stanice;
	}

	public void setStanice(List<Stanica> stanice) {
		this.stanice = stanice;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	
		
}
