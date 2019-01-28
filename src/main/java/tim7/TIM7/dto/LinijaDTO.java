package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.List;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.LinijaUZoni;
import tim7.TIM7.model.StanicaULiniji;
import tim7.TIM7.model.Vozilo;

public class LinijaDTO {
	private Long id;
	private String name;
	private List<ZonaDTO> zones;
	private List<StanicaDTO> stations;
	private List<VoziloDTO> vehicles;
	
	public LinijaDTO() {
		super();
		zones = new ArrayList<ZonaDTO>();
		stations = new ArrayList<StanicaDTO>();
		vehicles = new ArrayList<VoziloDTO>();
	}
	
	public LinijaDTO(Linija line) {
		super();
		id = line.getId();
		name = line.getNaziv();
		
		List<StanicaDTO> temp = new ArrayList<StanicaDTO>();
		for(StanicaULiniji sul : line.getStanice()) {
			StanicaDTO stationDTO = new StanicaDTO(sul.getStanica());
			temp.add(stationDTO);
		}
		stations = temp;
		
		List<ZonaDTO> temp2 = new ArrayList<ZonaDTO>();
		for(LinijaUZoni luz : line.getZone()) {
			ZonaDTO zonaDTO = new ZonaDTO(luz.getZona());
			temp2.add(zonaDTO);
		}
		zones = temp2;
		
		List<VoziloDTO> temp3 = new ArrayList<VoziloDTO>();
		for(Vozilo vehicle : line.getVozila()) {
			VoziloDTO vehicleDTO = new VoziloDTO(vehicle);
			temp3.add(vehicleDTO);
		}
		vehicles = temp3;
	}

	public LinijaDTO(Long id, String name, List<ZonaDTO> zones, List<StanicaDTO> stations, List<VoziloDTO> vehicles) {
		super();
		this.id = id;
		this.name = name;
		this.zones = zones;
		this.stations = stations;
		this.vehicles = vehicles;
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

	public List<VoziloDTO> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<VoziloDTO> vehicles) {
		this.vehicles = vehicles;
	}

	
}
