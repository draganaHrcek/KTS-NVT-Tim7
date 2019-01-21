package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.List;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.Zona;

public class LinijaDTO {
	private Long id;
	private String name;
	private List<Long> zonesIds;
	private List<Long> stationsIds;
	
	public LinijaDTO() {
		super();
		zonesIds = new ArrayList<Long>();
		stationsIds = new ArrayList<Long>();
	}
	
	public LinijaDTO(Linija line) {
		super();
		id = line.getId();
		name = line.getNaziv();
		List<Long> temp = new ArrayList<Long>();
		for(Zona zone : line.getZone()) {
			temp.add(zone.getId());
		}
		zonesIds = temp;
		
		List<Long> temp2 = new ArrayList<Long>();
		for(Stanica station : line.getStanice()) {
			temp2.add(station.getId());
		}
		stationsIds = temp2;
	}

	public LinijaDTO(Long id, String name, List<Long> zonesIds, List<Long> stationsIds) {
		super();
		this.id = id;
		this.name = name;
		this.zonesIds = zonesIds;
		this.stationsIds = stationsIds;
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

	public List<Long> getZonesIds() {
		return zonesIds;
	}

	public void setZonesIds(List<Long> zonesIds) {
		this.zonesIds = zonesIds;
	}

	public List<Long> getStationsIds() {
		return stationsIds;
	}

	public void setStationsIds(List<Long> stationsIds) {
		this.stationsIds = stationsIds;
	}

}
