package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.StavkaCenovnika;

public class CenovnikDTO {
	private Date datumObjavljivanja;
	private Date datumIsteka;
	private List<StavkaCenovnikaDto> stavkeCenovnika;
	private Long id;
	
	public CenovnikDTO(Cenovnik cenovnik) {
		this.stavkeCenovnika = new ArrayList<StavkaCenovnikaDto>();
		this.datumIsteka = cenovnik.getDatumIsteka();
		this.datumObjavljivanja = cenovnik.getDatumObjavljivanja();
		this.id = cenovnik.getId();
		for(StavkaCenovnika stavka : cenovnik.getStavke()){
			StavkaCenovnikaDto stavkaDto = new StavkaCenovnikaDto(stavka);
			this.stavkeCenovnika.add(stavkaDto);
		}
	}

	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Date getDatumObjavljivanja() {
		return datumObjavljivanja;
	}

	public void setDatumObjavljivanja(Date datumObjavljivanja) {
		this.datumObjavljivanja = datumObjavljivanja;
	}

	public Date getDatumIsteka() {
		return datumIsteka;
	}

	public void setDatumIsteka(Date datumIsteka) {
		this.datumIsteka = datumIsteka;
	}

	public List<StavkaCenovnikaDto> getStavkeCenovnika() {
		return stavkeCenovnika;
	}

	public void setStavkeCenovnika(List<StavkaCenovnikaDto> stavkeCenovnika) {
		this.stavkeCenovnika = stavkeCenovnika;
	}
	
	

}
