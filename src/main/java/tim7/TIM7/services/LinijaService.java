package tim7.TIM7.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.repositories.LinijaRepository;

@Service
public class LinijaService {

	@Autowired
	LinijaRepository linijaRepository;

}
