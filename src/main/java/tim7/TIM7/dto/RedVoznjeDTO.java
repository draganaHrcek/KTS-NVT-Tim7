package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;

public class RedVoznjeDTO {
	private Long id;
	private List<RasporedVoznjeDTO> rasporediVoznje;
	private Date datumObjavljivanja;

	
	
	
	
	public RedVoznjeDTO() {
		super();
	}


	public RedVoznjeDTO(RedVoznje redVoznje) {
		super();
		this.id = redVoznje.getId();
		this.rasporediVoznje = new ArrayList<RasporedVoznjeDTO>();
		this.datumObjavljivanja = redVoznje.getDatumObjavljivanja();
		
		for (RasporedVoznje rv : redVoznje.getRasporediVoznje()){
			this.rasporediVoznje.add(new RasporedVoznjeDTO(rv));
		}
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public List<RasporedVoznjeDTO> getRasporediVoznje() {
		return rasporediVoznje;
	}


	public void setRasporediVoznje(List<RasporedVoznjeDTO> rasporediVoznje) {
		this.rasporediVoznje = rasporediVoznje;
	}


	public Date getDatumObjavljivanja() {
		return datumObjavljivanja;
	}


	public void setDatumObjavljivanja(Date datumObjavljivanja) {
		this.datumObjavljivanja = datumObjavljivanja;
	}


	
	
	
	
}
