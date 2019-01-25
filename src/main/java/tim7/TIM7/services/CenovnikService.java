package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.CenovnikDTO;
import tim7.TIM7.dto.LinijaDTO;
import tim7.TIM7.dto.LinijeZoneTipovi;
import tim7.TIM7.dto.StavkaCenovnikaDto;
import tim7.TIM7.helper.SortCenovniciByDate;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Zona;
import tim7.TIM7.repositories.CenovnikRepository;

@Service
public class CenovnikService {

	@Autowired
	CenovnikRepository cenovnikRepository;
	
	@Autowired 
	LinijaService linijaService;
	
	@Autowired 
	ZonaService zonaService;
	
	@Autowired 
	StavkaCenovnikaService stavkaCenovnikaService;
	
	@Autowired 
	StavkaService stavkaService;
	
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

	public CenovnikDTO getTrenutniCenovnik(Cenovnik cenovnik) {		
		return new CenovnikDTO(cenovnik);
	}
	
	public LinijeZoneTipovi getSveZaCenovnik(){
		List<Linija> linije = linijaService.findAll();
		List<LinijaDTO> linijeDTO = new ArrayList<LinijaDTO>();
		
		for (Linija linija : linije) {
			linijeDTO.add(new LinijaDTO(linija));
		}
		return new LinijeZoneTipovi((ArrayList<LinijaDTO>) linijeDTO, 
				TipKarteCenovnik.values(), TipVozila.values());
	}

	public boolean addCenovnik(CenovnikDTO cenovnikDto) {
		Cenovnik cenovnik = new Cenovnik();
		
		for(StavkaCenovnikaDto stavkaDTO : cenovnikDto.getStavkeCenovnika()){
			Stavka stavka = stavkaService.findByDto(stavkaDTO);
			if (stavka == null){
				System.out.println("pravim novu");
				stavka = newStavka(stavkaDTO);
				if(stavka == null){
					return false;
				}				
			}
			System.out.println(stavka.getLinija().getNaziv());
			StavkaCenovnika stavkaCenovnika = new StavkaCenovnika(stavkaDTO.getCena(),
					stavka,cenovnik, false);
			cenovnik.getStavke().add(stavkaCenovnika);
		}
		if(!setAndCheckDate(cenovnik, cenovnikDto.getDatumObjavljivanja())){
			return false;
		}
		saveCenovnik(cenovnik);
		return true;
		
	}
	
	public Stavka newStavka(StavkaCenovnikaDto stavkaDTO){
		try{
			Linija linija = linijaService.findByName(stavkaDTO.getNazivLinije());
			Zona zona = zonaService.findByName(stavkaDTO.getNazivZone());
			Stavka stavka = new Stavka( stavkaDTO.getTipKarte(),
					stavkaDTO.getVrstaPrevoza(),zona, linija,false);
			return stavka;
		}catch(Exception e){
			return null;
		}		
	}
	
	public boolean setAndCheckDate(Cenovnik cenovnik, Date date){
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
		now.add(Calendar.DATE, 2);
		System.out.println(now);
		if(date.after(now.getTime())){
			cenovnik.setDatumObjavljivanja(date);
			return true;
		};
		return false;
		
	}
	
	public void saveCenovnik(Cenovnik cenovnik){
		cenovnikRepository.save(cenovnik);
		for (StavkaCenovnika stavka : cenovnik.getStavke()){
			stavkaCenovnikaService.save(stavka);
		}
	}

	public Cenovnik getTrenutni() {
		deleteIstekli();
		try{
			ArrayList<Cenovnik> cenovnici = (ArrayList<Cenovnik>) cenovnikRepository.findAllByObrisanFalse();
			Collections.sort(cenovnici, new SortCenovniciByDate());
			return cenovnici.get(0);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public void deleteIstekli(){
		ArrayList<Cenovnik> cenovnici = (ArrayList<Cenovnik>) cenovnikRepository.findAllByObrisanFalse();
		Collections.sort(cenovnici, new SortCenovniciByDate());
		Date now = Calendar.getInstance().getTime();
		for(int i = 0; i< cenovnici.size(); i++){
			if(i!= cenovnici.size()-1 &&
					cenovnici.get(i).getDatumObjavljivanja().before(now) &&
					cenovnici.get(i+1).getDatumObjavljivanja().before(now)){
				delete(cenovnici.get(i).getId());
			}
			else{
				break;
			}
		}
	}
	
	
}
