package tim7.TIM7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.LinijaRepository;

@Service
public class LinijaService {

	@Autowired
	LinijaRepository linijaRepository;


	public Linija findOne(Long id) {
		return linijaRepository.findById(id).get();
	}

	public List<Linija> findAll() {
		return linijaRepository.findAll();
	}
	public Linija findByName(String name) {
		return linijaRepository.findByNaziv( name);
	}

	public Linija save(Linija linija) {
		return linijaRepository.save(linija);
	}

	
	public void delete(Long id) {
		Linija linija=findOne(id);
		linija.setObrisan(true);
		save(linija);
	}
}
