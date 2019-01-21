package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Administrator extends Osoba{
	
	
	
	public Administrator() {
		super();
	}

	public Administrator(String korIme, String lozinka, String ime, String prezime, String email) {
		super(korIme, lozinka, ime, prezime, email);
	}

	
	
}
