package tim7.TIM7.dto;

import tim7.TIM7.model.Stanica;

public class StanicaDTO {
	
	private Long id;
	private String name;
	private double longitude;
	private double latitude;
	
	public StanicaDTO() {
		super();
	}
	
	public StanicaDTO(Stanica station) {
		super();
		id = station.getId();
		name = station.getOznaka();
		longitude = station.getLongituda();
		latitude = station.getLatituda();
	}

	public StanicaDTO(Long id, String name, double longitude, double latitude) {
		super();
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
}
