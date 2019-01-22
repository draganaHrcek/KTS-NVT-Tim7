package tim7.TIM7.dto;

import java.util.Date;

import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.TipKarte;

public class KartaDTO {
	
	String kod;
	
	Date datumIsteka;
	Double cena;
	String tipKarte;
	String tipPrevoza;
	String linijaZona;
	String statusKorisnika;
	boolean cekiranaDnevnaKarta;
	
	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public boolean isCekiranaDnevnaKarta() {
		return cekiranaDnevnaKarta;
	}
	public void setCekiranaDnevnaKarta(boolean cekiranaDnevnaKarta) {
		this.cekiranaDnevnaKarta = cekiranaDnevnaKarta;
	}
	public String getStatusKorisnika() {
		return statusKorisnika;
	}
	public void setStatusKorisnika(String statusKorisnika) {
		this.statusKorisnika = statusKorisnika;
	}
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
