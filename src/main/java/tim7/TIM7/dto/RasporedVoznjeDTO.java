package tim7.TIM7.dto;

import java.time.LocalTime;
import java.util.List;

import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.RasporedVoznje;

public class RasporedVoznjeDTO {
	private Long id;
	private DanUNedelji danUNedelji;
	private List<LocalTime> vremena;
	private String nazivLinije;
	
	
	
	
	public RasporedVoznjeDTO() {
		super();
	}


	public RasporedVoznjeDTO(RasporedVoznje rv) {
		super();
		this.id = rv.getId();
		this.danUNedelji = rv.getDanUNedelji();
		this.vremena = rv.getVremena();
		this.nazivLinije = rv.getLinija().getNaziv();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public String getNazivLinije() {
		return nazivLinije;
	}


	public void setNazivLinije(String nazivLinije) {
		this.nazivLinije = nazivLinije;
	}
	
	
	
	
	
	
	
}
