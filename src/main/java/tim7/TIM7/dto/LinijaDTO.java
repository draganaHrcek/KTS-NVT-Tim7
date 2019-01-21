package tim7.TIM7.dto;

import java.util.ArrayList;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Zona;

public class LinijaDTO {
	private Long id;
	private String naziv;
	private ArrayList<ZonaDTO> zone;
	
	public LinijaDTO() {
		super();
	}

	public LinijaDTO(Long id, String naziv, ArrayList<ZonaDTO> zone) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.zone = zone;
	}
	
	public LinijaDTO(String naziv) {
		super();
		this.naziv = naziv;
		this.zone = new ArrayList<ZonaDTO>();
	}

	public Long getId() {
		return id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public ArrayList<ZonaDTO> getZone() {
		return zone;
	}

	public void setZone(ArrayList<ZonaDTO> zone) {
		this.zone = zone;
	}
	
	public LinijaDTO(Linija linija){
		this.naziv = linija.getNaziv();
		this.id = linija.getId();
		this.zone = new ArrayList<ZonaDTO>();
		for (Zona zona : linija.getZone()){
			ZonaDTO zonaDTO= new ZonaDTO(zona);
			this.zone.add(zonaDTO);
		}
	}
	
	
	
	

}
