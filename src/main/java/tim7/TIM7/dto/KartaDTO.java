package tim7.TIM7.dto;

import java.util.Date;

public class KartaDTO {
	
	Date datumIsteka;
	Double cena;
	String tipKarte;
	String tipPrevoza;
	String linijaZona;
	
	public String getLinijaZona() {
		return linijaZona;
	}
	public void setLinijaZona(String linijaZona) {
		this.linijaZona = linijaZona;
	}
	public KartaDTO() {
		super();
	}
	public Date getDatumIsteka() {
		return datumIsteka;
	}
	public void setDatumIsteka(Date datumIsteka) {
		this.datumIsteka = datumIsteka;
	}
	public Double getCena() {
		return cena;
	}
	public void setCena(Double cena) {
		this.cena = cena;
	}
	public String getTipKarte() {
		return tipKarte;
	}
	public void setTipKarte(String tipKarte) {
		this.tipKarte = tipKarte;
	}
	public String getTipPrevoza() {
		return tipPrevoza;
	}
	public void setTipPrevoza(String tipPrevoza) {
		this.tipPrevoza = tipPrevoza;
	}
	
	
}
