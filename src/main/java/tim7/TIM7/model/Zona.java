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
public class Zona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="naziv")
	String naziv;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<LinijaUZoni> linije;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<Stavka> stavkeCenovnika;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<VisednevnaKarta> karte;

	
	@Column(name="obrisan")
	boolean obrisan;
	
	public Zona() {
		super();
		this.linije = new ArrayList<LinijaUZoni>();
		this.obrisan= false;
	}
	
	public Zona(String naziv, boolean obrisan) {
		super();
		this.obrisan= obrisan;
		this.naziv = naziv;
		this.linije = new ArrayList<LinijaUZoni>();
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



	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public List<Stavka> getStavkeCenovnika() {
		return stavkeCenovnika;
	}

	public void setStavkeCenovnika(List<Stavka> stavkeCenovnika) {
		this.stavkeCenovnika = stavkeCenovnika;
	}

	public List<VisednevnaKarta> getKarte() {
		return karte;
	}

	public void setKarte(List<VisednevnaKarta> karte) {
		this.karte = karte;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LinijaUZoni> getLinije() {
		return linije;
	}

	public void setLinije(List<LinijaUZoni> linijeZone) {
		this.linije = linijeZone;
	}
	
	
	
	
}
