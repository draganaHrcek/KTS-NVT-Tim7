package tim7.TIM7.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Izvestaj {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	Administrator autor;
	
	@Column(name="datum_izrade")
	Date datumIzrade;
	
	@Column (name="sadrzaj")
	String sadrzaj;

	

	public Izvestaj() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}



	public Administrator getAutor() {
		return autor;
	}

	public void setAutor(Administrator autor) {
		this.autor = autor;
	}

	public Date getDatumIzrade() {
		return datumIzrade;
	}

	public void setDatumIzrade(Date datumIzrade) {
		this.datumIzrade = datumIzrade;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}
	
	
}
