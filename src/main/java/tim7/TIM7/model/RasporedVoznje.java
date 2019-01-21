package tim7.TIM7.model;




import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
@Entity
public class RasporedVoznje {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="dan_u_nedelji")
	DanUNedelji danUNedelji;
	
	@Column (name="vremena")
	@ElementCollection(targetClass=LocalTime.class)
	List<LocalTime> vremena;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Linija linija;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	RedVoznje redVoznje;
	
	
	@Column(name="obrisan")
	boolean obrisan;


	public RasporedVoznje() {
		super();
		this.obrisan= false;
		// TODO Auto-generated constructor stub
	}


	


	public RasporedVoznje(DanUNedelji danUNedelji, List<LocalTime> vremena, Linija linija, RedVoznje redVoznje,
			boolean obrisan) {
		super();
		this.danUNedelji = danUNedelji;
		this.vremena = vremena;
		this.linija = linija;
		this.redVoznje = redVoznje;
		this.obrisan = obrisan;
	}





	public DanUNedelji getDanUNedelji() {
		return danUNedelji;
	}





	public void setDanUNedelji(DanUNedelji danUNedelji) {
		this.danUNedelji = danUNedelji;
	}





	public List<LocalTime> getVremena() {
		return vremena;
	}


	public void setVremena(List<LocalTime> vremena) {
		this.vremena = vremena;
	}


	public Linija getLinija() {
		return linija;
	}


	public void setLinija(Linija linija) {
		this.linija = linija;
	}


	public RedVoznje getRedVoznje() {
		return redVoznje;
	}


	public void setRedVoznje(RedVoznje redVoznje) {
		this.redVoznje = redVoznje;
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





	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	

}
