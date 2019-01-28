package tim7.TIM7.dto;

import java.util.ArrayList;

import tim7.TIM7.model.StatusKorisnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;

public class LinijeZoneTipovi {
	private ArrayList<LinijaDTO> linije;
	private TipKarteCenovnik[] tipoviKarata;
	private TipVozila[] vrstePrevoza;
	private ArrayList<StatusKorisnika> statusiKorisnika;
	
	public LinijeZoneTipovi(ArrayList<LinijaDTO> linijeDTO, TipKarteCenovnik[] tipKarteCenovniks,
			TipVozila[] tipVozilas, ArrayList<StatusKorisnika> statusiKorisnika) {
		super();
		this.linije = linijeDTO;
		this.tipoviKarata = tipKarteCenovniks;
		this.vrstePrevoza = tipVozilas;
		this.statusiKorisnika = statusiKorisnika;

	}

	
	
	



	public ArrayList<StatusKorisnika> getStatusiKorisnika() {
		return statusiKorisnika;
	}







	public void setStatusiKorisnika(ArrayList<StatusKorisnika> statusiKorisnika) {
		this.statusiKorisnika = statusiKorisnika;
	}







	public LinijeZoneTipovi() {
		super();
		this.linije = new ArrayList<LinijaDTO>();
		this.statusiKorisnika = new ArrayList<StatusKorisnika>();
	}

	public ArrayList<LinijaDTO> getLinije() {
		return linije;
	}

	public void setLinije(ArrayList<LinijaDTO> linije) {
		this.linije = linije;
	}

	public TipKarteCenovnik[] getTipoviKarata() {
		return tipoviKarata;
	}

	public TipVozila[] getVrstePrevoza() {
		return vrstePrevoza;
	}

	public void setVrstePrevoza(TipVozila[] vrstePrevoza) {
		this.vrstePrevoza = vrstePrevoza;
	}

	public void setTipoviKarata(TipKarteCenovnik[] tipoviKarata) {
		this.tipoviKarata = tipoviKarata;
	}

	
	
	
	

}
