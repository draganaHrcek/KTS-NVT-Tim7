package tim7.TIM7.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
public class RedVoznje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@OneToMany(mappedBy = "redVoznje", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	List <RasporedVoznje> rasporediVoznje;
	
	
	@Column(name="obrisan")
	boolean obrisan;
	
	@Column(name="datum_objavljivanja")
	Date datumObjavljivanja;
	


	public RedVoznje() {
		super();
		this.obrisan= false;
		// TODO Auto-generated constructor stub
	}


	


	public RedVoznje(boolean obrisan, Date datumObjavljivanja) {
		super();
		this.obrisan = obrisan;
		this.datumObjavljivanja = datumObjavljivanja;
	}





	public List<RasporedVoznje> getRasporediVoznje() {
		return rasporediVoznje;
	}





	public void setRasporediVoznje(List<RasporedVoznje> rasporediVoznje) {
		this.rasporediVoznje = rasporediVoznje;
	}





	public boolean isObrisan() {
		return obrisan;
	}


	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}


	public Date getDatumObjavljivanja() {
		return datumObjavljivanja;
	}


	public void setDatumObjavljivanja(Date datumObjavljivanja) {
		this.datumObjavljivanja = datumObjavljivanja;
	}




	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	

}
