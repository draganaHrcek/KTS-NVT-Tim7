package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Stavka {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	//nova enumeracija za cenovnik zbog dnevne karte koja nije u enumeraciji tip karte
	@Column(name="tip_karte")
	TipKarteCenovnik tipKarte;
	
	@Column(name="vrsta_prevoza")
	TipVozila vrstaPrevoza;
	
	//ako je karta dnevna zona je null, za sve ostale tipove cena zavisi i od zone
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Zona zona;
	
	//za dnevnu kartu u ostalim slucajevima null
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Linija linija;
	
	@OneToMany(mappedBy = "stavka", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<StavkaCenovnika> stavkeCenovnika;

	
	@Column(name="obrisan")
	boolean obrisan;
	public Stavka() {
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


	public TipKarteCenovnik getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(TipKarteCenovnik tipKarte) {
		this.tipKarte = tipKarte;
	}

	public Linija getLinija() {
		return linija;
	}

	public void setLinija(Linija linija) {
		this.linija = linija;
	}

	public TipVozila getVrstaPrevoza() {
		return vrstaPrevoza;
	}

	public void setVrstaPrevoza(TipVozila vrstaPrevoza) {
		this.vrstaPrevoza = vrstaPrevoza;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public List<StavkaCenovnika> getStavkeCenovnika() {
		return stavkeCenovnika;
	}

	public void setStavkeCenovnika(List<StavkaCenovnika> stavkeCenovnika) {
		this.stavkeCenovnika = stavkeCenovnika;
	}
	
	
}
