package tim7.TIM7.dto;

import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Vozilo;

public class VoziloDTO {
	private Long id;
	private String registration;
	private Long lineId;
	private TipVozila type;

	public VoziloDTO() {
		super();
	}
	
	public VoziloDTO(Vozilo vozilo) {
		id = vozilo.getId();
		registration = vozilo.getRegistracija();
		lineId = vozilo.getLinija().getId();
		type = vozilo.getTipVozila();
	}

	public VoziloDTO(Long id, String registration, Long lineId, TipVozila type) {
		super();
		this.id = id;
		this.registration = registration;
		this.lineId = lineId;
		this.type = type;
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

	public TipVozila getType() {
		return type;
	}

	public void setType(TipVozila type) {
		this.type = type;
	}
	
	
	
	
	

}
