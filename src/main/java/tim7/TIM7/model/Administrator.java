package tim7.TIM7.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Administrator extends Osoba{
	
	@OneToMany(mappedBy = "autor", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List<Izvestaj> izvestaji;
	
	public Administrator() {
		super();
	}



	public List<Izvestaj> getIzvestaji() {
		return izvestaji;
	}

	public void setIzvestaji(List<Izvestaj> izvestaji) {
		this.izvestaji = izvestaji;
	}
	
	
}
