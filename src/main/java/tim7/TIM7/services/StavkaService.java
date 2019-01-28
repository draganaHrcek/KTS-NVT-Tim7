package tim7.TIM7.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.StavkaCenovnikaDto;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.repositories.StavkaRepository;

@Service
public class StavkaService {

	@Autowired
	StavkaRepository stavkaRepository;
	
	public Stavka findByDtoEdit(StavkaCenovnikaDto stavkaDTO) {
		for(Stavka stavka : stavkaRepository.findAll()){
			if(( stavkaDTO.getNazivLinije() == null || stavka.getLinija() == null ||
					stavka.getLinija().getNaziv().equals(stavkaDTO.getNazivLinije()))&&(
					stavkaDTO.getNazivZone() == null || stavka.getZona() == null ||
					stavka.getZona().getNaziv().equals(stavkaDTO.getNazivZone()))&&
					stavka.getTipKarte().ordinal() == (stavkaDTO.getTipKarte().ordinal())&&
					stavka.getVrstaPrevoza().ordinal() == stavkaDTO.getVrstaPrevoza().ordinal()){
				return stavka;
			}
		}
		return null;
	}
	
	public Stavka findByDto(StavkaCenovnikaDto stavkaDTO) {
		for(Stavka stavka : stavkaRepository.findAll()){
			if(stavkaDTO.getNazivLinije() == null && stavkaDTO.getNazivZone() == null)
				return null;
			if(stavkaDTO.getTipKarte() == null || stavkaDTO.getVrstaPrevoza() == null )
				return null;
			if(( stavkaDTO.getNazivLinije() != null || stavka.getLinija() == null ||
					stavka.getLinija().getNaziv().equals(stavkaDTO.getNazivLinije()))&&(
					stavkaDTO.getNazivZone() == null || stavka.getZona() == null ||
					stavka.getZona().getNaziv().equals(stavkaDTO.getNazivZone()))&&
					stavka.getTipKarte().ordinal() == (stavkaDTO.getTipKarte().ordinal())&&
					stavka.getVrstaPrevoza().ordinal() == stavkaDTO.getVrstaPrevoza().ordinal()){
				return stavka;
			}
		}
		return null;
	}
}
