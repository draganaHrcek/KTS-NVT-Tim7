package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.StavkaCenovnika;

public class CenovnikDTO {
	private Date datumObjavljivanja;
	private Date datumIsteka;
	private List<StavkaCenovnikaDto> stavkeCenovnika;
	private Long id;
	private Integer popustStudent;
	private Integer popustPenzioner;
	private Integer popustDjak;
	private Integer popustNezaposlen;
	
	public CenovnikDTO(Cenovnik cenovnik) {
		this.stavkeCenovnika = new ArrayList<StavkaCenovnikaDto>();
		this.datumObjavljivanja = cenovnik.getDatumObjavljivanja();
		this.id = cenovnik.getId();
		this.popustDjak = cenovnik.getPopustDjak();
		this.popustNezaposlen = cenovnik.getPopustNezaposlen();
		this.popustPenzioner = cenovnik.getPopustPenzioner();
		this.popustStudent = cenovnik.getPopustStudent();
		for(StavkaCenovnika stavka : cenovnik.getStavke()){
			StavkaCenovnikaDto stavkaDto = new StavkaCenovnikaDto(stavka);
			this.stavkeCenovnika.add(stavkaDto);
		}
	}
	
	public CenovnikDTO(){
		super();
	}

	
	
	public Integer getPopustStudent() {
		return popustStudent;
	}

	public void setPopustStudent(Integer popustStudent) {
		this.popustStudent = popustStudent;
	}

	public Integer getPopustPenzioner() {
		return popustPenzioner;
	}

	public void setPopustPenzioner(Integer popustPenzioner) {
		this.popustPenzioner = popustPenzioner;
	}

	public Integer getPopustDjak() {
		return popustDjak;
	}

	public void setPopustDjak(Integer popustDjak) {
		this.popustDjak = popustDjak;
	}

	public Integer getPopustNezaposlen() {
		return popustNezaposlen;
	}

	public void setPopustNezaposlen(Integer popustNezaposlen) {
		this.popustNezaposlen = popustNezaposlen;
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
