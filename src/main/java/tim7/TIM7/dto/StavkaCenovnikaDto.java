package tim7.TIM7.dto;

import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;

public class StavkaCenovnikaDto {
	private Double cena;
	private TipKarteCenovnik tipKarte;
	private TipVozila vrstaPrevoza;
	private String nazivZone;
	private String nazivLinije;
	private Long id;
	
	public StavkaCenovnikaDto(StavkaCenovnika stavka) {
		this.cena = stavka.getCena();
		this.tipKarte = stavka.getStavka().getTipKarte();
		this.vrstaPrevoza = stavka.getStavka().getVrstaPrevoza();
		if( stavka.getStavka().getZona() != null){
			this.nazivZone = stavka.getStavka().getZona().getNaziv();
		}
		if(stavka.getStavka().getLinija() != null){
			this.nazivLinije = stavka.getStavka().getLinija().getNaziv();
		}
		this.id = stavka.getId();
		System.out.println("printam u dubokom konstruktory " + this.id);

	}

	public StavkaCenovnikaDto(){};
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
		this.cena = cena;
	}

	public TipKarteCenovnik getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(TipKarteCenovnik tipKarte) {
		this.tipKarte = tipKarte;
	}

	public TipVozila getVrstaPrevoza() {
		return vrstaPrevoza;
	}

	public void setVrstaPrevoza(TipVozila vrstaPrevoza) {
		this.vrstaPrevoza = vrstaPrevoza;
	}

	public String getNazivZone() {
		return nazivZone;
	}

	public void setNazivZone(String nazivZone) {
		this.nazivZone = nazivZone;
	}

	public String getNazivLinije() {
		return nazivLinije;
	}

	public void setNazivLinije(String nazivLinije) {
		this.nazivLinije = nazivLinije;
	}
	
	

}
