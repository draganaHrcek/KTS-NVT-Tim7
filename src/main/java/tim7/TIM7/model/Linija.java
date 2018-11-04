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

@Entity
public class Linija {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@OneToMany(mappedBy = "linija", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <Vozilo> vozila;
	 
	@ManyToMany
    @JoinTable(name = "linije_u_zonama",
               joinColumns = @JoinColumn(name="linija_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="zona_id", referencedColumnName="id"))
	List<Zona> zone;
	
	@ManyToMany(mappedBy="linije")
	List<Stanica> stanice;

	

	public Linija() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
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
	
	
		
}
