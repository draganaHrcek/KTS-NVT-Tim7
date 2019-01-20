package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Korisnik extends Osoba {

	
	//	lokacija slike prilozenog dokumenta o statusu korisnika
	@Column(name = "lokacija_dokumenta")
	String lokacijaDokumenta;
	
	@Column(name="status_korisnika")
	StatusKorisnika status;
	
	
	
	@OneToMany(mappedBy = "korisnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <Karta> karte;

	
	public Korisnik() {
		super();
		// TODO Auto-generated constructor stub
	}



	public StatusKorisnika getStatus() {
		return status;
	}





	public void setStatus(StatusKorisnika status) {
		this.status = status;
	}




	public Korisnik(String korIme, String lozinka, String ime, String prezime, String email, String lokacijaDokumenta, StatusKorisnika status, List<Karta> karte) {
		super(korIme, lozinka, ime, prezime, email);
		this.lokacijaDokumenta = lokacijaDokumenta;
		this.status = status;
		
		this.karte = karte;
	}



	public String getLokacijaDokumenta() {
		return lokacijaDokumenta;
	}

	public void setLokacijaDokumenta(String lokacijaDokumenta) {
		this.lokacijaDokumenta = lokacijaDokumenta;
	}

	public List<Karta> getKarte() {
		return karte;
	}

	public void setKarte(List<Karta> karte) {
		this.karte = karte;
	}
	
	
}
