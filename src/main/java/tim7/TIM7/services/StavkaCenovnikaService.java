package tim7.TIM7.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.repositories.StavkaCenovnikaRepository;
import tim7.TIM7.repositories.StavkaRepository;

@Service
public class StavkaCenovnikaService {

	@Autowired
	StavkaCenovnikaRepository stavkaCenovnikaRepository;
	
	@Autowired
	StavkaRepository stavkaRepository;
	
	public void save(StavkaCenovnika stavka){
		System.out.println(stavka.getCena());
		System.out.println(stavka.getStavka().getLinija().getNaziv());

		stavkaRepository.save(stavka.getStavka());
		stavkaCenovnikaRepository.save(stavka);
	}

	

}
