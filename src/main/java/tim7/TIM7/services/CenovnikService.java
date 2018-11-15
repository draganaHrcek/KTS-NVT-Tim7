package tim7.TIM7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Karta;
import tim7.TIM7.repositories.CenovnikRepository;

@Service
public class CenovnikService {

	@Autowired
	CenovnikRepository cenovnikRepository;
	
	public Cenovnik findOne(Long id) {
		return cenovnikRepository.findById(id).get();
	}

	public List<Cenovnik> findAll() {
		return cenovnikRepository.findAll();
	}


	public Cenovnik save(Cenovnik cenovnik) {
		return cenovnikRepository.save(cenovnik);
	}

	public void delete(Long id) {
		Cenovnik cenovnik=findOne(id);
		cenovnik.setObrisan(true);
		save(cenovnik);
	}
}
