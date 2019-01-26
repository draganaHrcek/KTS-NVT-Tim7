package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.List;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stanica;
import tim7.TIM7.model.Vozilo;
import tim7.TIM7.model.Zona;

public class UpdatedLinijaDTO {
	private Long id;
	private String name;
	private List<ZonaDTO> zones;
	private List<StanicaDTO> stations;
	private List<VoziloDTO> vehicles;
	private List<Long> removedZones;
	private List<Long> removedStations;
	private List<Long> removedVehicles;
	
	public UpdatedLinijaDTO() {
		super();
		zones = new ArrayList<ZonaDTO>();
		stations = new ArrayList<StanicaDTO>();
		vehicles = new ArrayList<VoziloDTO>();
		removedZones = new ArrayList<Long>();
		removedStations = new ArrayList<Long>();
		removedVehicles = new ArrayList<Long>();
	}
	
	public UpdatedLinijaDTO(Linija line) {
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
		
		List<VoziloDTO> temp3 = new ArrayList<VoziloDTO>();
		for(Vozilo vehicle : line.getVozila()) {
			VoziloDTO vehicleDTO = new VoziloDTO(vehicle);
			temp3.add(vehicleDTO);
		}
		vehicles = temp3;
		
		removedZones = new ArrayList<Long>();
		removedStations = new ArrayList<Long>();
		removedVehicles = new ArrayList<Long>();
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

	public List<Long> getRemovedZones() {
		return removedZones;
	}

	public void setRemovedZones(List<Long> removedZones) {
		this.removedZones = removedZones;
	}

	public List<Long> getRemovedStations() {
		return removedStations;
	}

	public void setRemovedStations(List<Long> removedStations) {
		this.removedStations = removedStations;
	}

	public List<Long> getRemovedVehicles() {
		return removedVehicles;
	}

	public void setRemovedVehicles(List<Long> removedVehicles) {
		this.removedVehicles = removedVehicles;
	}

	public UpdatedLinijaDTO(Long id, String name, List<ZonaDTO> zones, List<StanicaDTO> stations,
			List<VoziloDTO> vehicles, List<Long> removedZones, List<Long> removedStations, List<Long> removedVehicles) {
		super();
		this.id = id;
		this.name = name;
		this.zones = zones;
		this.stations = stations;
		this.vehicles = vehicles;
		this.removedZones = removedZones;
		this.removedStations = removedStations;
		this.removedVehicles = removedVehicles;
	}
	
	
}
