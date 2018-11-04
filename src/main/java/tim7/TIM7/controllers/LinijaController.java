package tim7.TIM7.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tim7.TIM7.services.LinijaService;

@RestController
@RequestMapping("/linije")
public class LinijaController {

	@Autowired
	LinijaService linijaService;
	
}
