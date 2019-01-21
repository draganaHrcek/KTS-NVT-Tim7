package tim7.TIM7.model;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Verifikator extends Osoba {
	
	public Verifikator() {
		super();
	}

}
