package tim7.TIM7.dto;

import java.util.List;

public class UpdatedZonaDTO {
	
	private Long id;
	private String name;
	private List<LinijaDTO> lines;
	
	public UpdatedZonaDTO() {
		super();
	}

	public UpdatedZonaDTO(Long id, String name, List<LinijaDTO> lines) {
		super();
		this.id = id;
		this.name = name;
		this.lines = lines;
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

	public List<LinijaDTO> getLines() {
		return lines;
	}

	public void setLines(List<LinijaDTO> lines) {
		this.lines = lines;
	}

	
	

}
