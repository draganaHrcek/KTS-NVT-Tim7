package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.List;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.Zona;

public class LinijaDTO {
	private Long id;
	private String name;
	private List<ZonaDTO> zones;
	private List<StanicaDTO> stations;
	
	public LinijaDTO() {
		super();
		zones = new ArrayList<ZonaDTO>();
		stations = new ArrayList<StanicaDTO>();
	}
	
	public LinijaDTO(Linija line) {
		super();
		id = line.getId();
		name = line.getNaziv();
		List<ZonaDTO> temp = new ArrayList<ZonaDTO>();
		for(Zona zone : line.getZone()) {
			ZonaDTO zonaDTO = new ZonaDTO(zone);
			temp.add(zonaDTO);
		}
		zones = temp;
		
		List<StanicaDTO> temp2 = new ArrayList<StanicaDTO>();
		for(Stanica station : line.getStanice()) {
			StanicaDTO stationDTO = new StanicaDTO(station);
			temp2.add(stationDTO);
		}
		stations = temp2;
	}

	public LinijaDTO(Long id, String name, List<ZonaDTO> zones, List<StanicaDTO> stations) {
		super();
		this.id = id;
		this.name = name;
		this.zones = zones;
		this.stations = stations;
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

	public List<ZonaDTO> getZones() {
		return zones;
	}

	public void setZones(List<ZonaDTO> zones) {
		this.zones = zones;
	}

	public List<StanicaDTO> getStations() {
		return stations;
	}

	public void setStations(List<StanicaDTO> stations) {
		this.stations = stations;
	}

}
