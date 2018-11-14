package tim7.TIM7.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Stanica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name="oznka")
	String oznaka;
	
	@OneToOne(fetch=FetchType.LAZY )
	LokacijaStanice lokacija; 
	
	@ManyToMany
    @JoinTable(name = "stanice_u_liniji",
               joinColumns = @JoinColumn(name="stanica_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="linija_id", referencedColumnName="id"))
	List<Linija> linije;
	
	@Column(name="obrisan")
	boolean obrisan;
	public Stanica() {
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

	

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public LokacijaStanice getLokacija() {
		return lokacija;
	}

	public void setLokacija(LokacijaStanice lokacija) {
		this.lokacija = lokacija;
	}

	public List<Linija> getLinije() {
		return linije;
	}

	public void setLinije(List<Linija> linije) {
		this.linije = linije;
	}
	
	
}
