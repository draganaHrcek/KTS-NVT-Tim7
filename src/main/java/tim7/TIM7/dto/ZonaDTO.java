package tim7.TIM7.dto;

import tim7.TIM7.model.Zona;

public class ZonaDTO {
	private Long id;
	private String name;
	
	public ZonaDTO() {
		super();
	}
	
	public ZonaDTO(Zona zona) {
		super();
		id=zona.getId();
		name=zona.getNaziv();
	}

	public ZonaDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
