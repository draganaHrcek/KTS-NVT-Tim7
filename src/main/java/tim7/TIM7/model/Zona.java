package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Zona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="naziv")
	String naziv;
	
	@ManyToMany(mappedBy="zone")
	List<Linija> linije;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<Stavka> stavkeCenovnika;
	
	@OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<VisednevnaKarta> karte;

	
	@Column(name="obrisan")
	boolean obrisan;
	
	public Zona() {
		super();
		this.obrisan= false;
	}
	
	public Zona(String naziv, boolean obrisan) {
		super();
		this.obrisan= obrisan;
		this.naziv = naziv;
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

	public List<Linija> getLinije() {
		return linije;
	}

	public void setLinije(List<Linija> linije) {
		this.linije = linije;
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
	
	
	
	
}
