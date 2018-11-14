package tim7.TIM7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.model.Karta;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.repositories.KartaRepository;

@Service
public class KartaService {

	@Autowired
	KartaRepository kartaRepository;
	
	
	public Karta findOne(Long id) {
		return kartaRepository.findById(id).get();
	}

	public List<Karta> findAll() {
		return kartaRepository.findAll();
	}


	public Karta save(Karta karta) {
		return kartaRepository.save(karta);
	}

	public void delete(Long id) {
		Karta karta=findOne(id);
		karta.setObrisan(true);
		save(karta);
	}

}
