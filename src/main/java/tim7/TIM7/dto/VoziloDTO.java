package tim7.TIM7.dto;

import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Vozilo;

public class VoziloDTO {
	private Long id;
	private String registration;
	private Long lineId;
	private String lineName;
	private String type;

	public VoziloDTO() {
		super();
	}
	
	public VoziloDTO(Vozilo vozilo) {
		id = vozilo.getId();
		registration = vozilo.getRegistracija();
		if(vozilo.getLinija()!=null) {
			lineId = vozilo.getLinija().getId();
			lineName = vozilo.getLinija().getNaziv();
		}else {
			lineId= null;
		}
		type = vozilo.getTipVozila().toString();
	}

	public VoziloDTO(Long id, String registration, Long lineId, String type, String lineName) {
		super();
		this.id = id;
		this.registration = registration;
		this.lineId = lineId;
		this.type = type;
		this.lineName = lineName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	
	
	
	

}
