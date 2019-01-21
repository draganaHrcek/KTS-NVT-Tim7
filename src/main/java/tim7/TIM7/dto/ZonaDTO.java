package tim7.TIM7.dto;

import tim7.TIM7.model.Zona;

public class ZonaDTO {
	private Long id;
	private String naziv;
	
	public ZonaDTO(Long id, String naziv) {
		super();
		this.id = id;
		this.naziv = naziv;
	}
	
	public ZonaDTO(){
		super();
	}

	public ZonaDTO(Zona zona) {
		this.id = zona.getId();
		this.naziv = zona.getNaziv();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	
}
