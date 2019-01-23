package tim7.TIM7.model;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Kondukter extends Osoba {
	public Kondukter() {
		super();
	}
	
	public Kondukter(String korIme, String lozinka, String ime, String prezime, String email) {
		super(korIme, lozinka, ime, prezime, email);
	}


}
