package tim7.TIM7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import tim7.TIM7.model.Osoba;
import tim7.TIM7.repositories.OsobaRepository;

@Service
public class OsobaService {

	@Autowired
	OsobaRepository osobaRepository;
	
	
	public Osoba findOne(Long id) {
		return osobaRepository.findById(id).get();
	}

	public List<Osoba> findAll() {
		return osobaRepository.findAll();
	}


	public Osoba save(Osoba osoba) {
		return osobaRepository.save(osoba);
	}

	public Osoba findByUsername(String username) {
		return osobaRepository.findByKorIme(username);
	}
	public void delete(Long id) {
		Osoba osoba=findOne(id);
		osoba.setObrisan(true);
		save(osoba);
	}
	

}
