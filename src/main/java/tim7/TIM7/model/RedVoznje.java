package tim7.TIM7.model;

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
	List <RasporedVoznje> rasporedVoznje;
	
	
	@Column(name="obrisan")
	boolean obrisan;


	public RedVoznje() {
		super();
		this.obrisan= false;
		// TODO Auto-generated constructor stub
	}


	public List<RasporedVoznje> getRasporedVoznje() {
		return rasporedVoznje;
	}


	public void setRasporedVoznje(List<RasporedVoznje> rasporedVoznje) {
		this.rasporedVoznje = rasporedVoznje;
	}


	public boolean isObrisan() {
		return obrisan;
	}


	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
	
	
	
	

}
