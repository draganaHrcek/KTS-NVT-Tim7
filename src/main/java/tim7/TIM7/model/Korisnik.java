package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Korisnik extends Osoba {

	@Column(name = "status")
	StatusKorisnika status;
	//	lokacija slike prilozenog dokumenta o statusu korisnika
	@Column(name = "lokacija_dokumenta")
	String lokacijaDokumenta;
	
	@OneToMany(mappedBy = "korisnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <Karta> karte;
	
}
