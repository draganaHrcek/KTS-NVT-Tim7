package tim7.TIM7.dto;

import java.util.ArrayList;
import java.util.List;

import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;

public class LinijeZoneTipovi {
	private ArrayList<LinijaDTO> linije;
	private TipKarteCenovnik[] tipoviKarata;
	private TipVozila[] vrstePrevoza;
	
	public LinijeZoneTipovi(ArrayList<LinijaDTO> linijeDTO, TipKarteCenovnik[] tipKarteCenovniks,
			TipVozila[] tipVozilas) {
		super();
		this.linije = linijeDTO;
		this.tipoviKarata = tipKarteCenovniks;
		this.vrstePrevoza = tipVozilas;
	}

	public LinijeZoneTipovi() {
		super();
		this.linije = new ArrayList<LinijaDTO>();
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
