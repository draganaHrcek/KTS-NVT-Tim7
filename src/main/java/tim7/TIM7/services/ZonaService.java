package tim7.TIM7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.ZonaRepository;

@Service
public class ZonaService {

	@Autowired
	ZonaRepository zonaRepository;

	
	public Zona findOne(Long id) {
		return zonaRepository.findById(id).get();
	}

	public List<Zona> findAll() {
		return zonaRepository.findAll();
	}
	public Zona findByName(String name) {
		return zonaRepository.findByNaziv( name);
	}

	public Zona save(Zona zona) {
		return zonaRepository.save(zona);
	}

	
	public void delete(Long id) {
		Zona zona=findOne(id);
		zona.setObrisan(true);
		save(zona);
	}
}
