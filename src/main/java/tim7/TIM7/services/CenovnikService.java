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
import tim7.TIM7.model.StatusKorisnika;
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
		
		ArrayList<StatusKorisnika> statusi = new ArrayList<StatusKorisnika>();
		for(StatusKorisnika status : StatusKorisnika.values()){
			if(!status.equals(StatusKorisnika.RADNIK)){
				statusi.add(status);
			}
		}
		
		return new LinijeZoneTipovi((ArrayList<LinijaDTO>) linijeDTO, 
				TipKarteCenovnik.values(), TipVozila.values(), statusi);
	}

	public String addCenovnik(CenovnikDTO cenovnikDto) {
		Cenovnik cenovnik = new Cenovnik();
		
		for(StavkaCenovnikaDto stavkaDTO : cenovnikDto.getStavkeCenovnika()){
			if(stavkaDTO.getCena() == null){
				return "Greska! Nije moguce dodati ove stavke";
			}
			Stavka stavka = stavkaService.findByDto(stavkaDTO);
			if (stavka == null){
				stavka = newStavka(stavkaDTO);
				if(stavka == null){
					return "Greska! Nije moguce dodati ove stavke";
				}				
			}
			//System.out.println(stavka.getLinija().getNaziv());
			StavkaCenovnika stavkaCenovnika = new StavkaCenovnika(stavkaDTO.getCena(),
					stavka,cenovnik, false);
			cenovnik.getStavke().add(stavkaCenovnika);
		}
		if(cenovnik.getStavke().size() <= 0){
			return "Greska! Mora imati bar jednu stavku";
		}
		if(!checkWithFutureDates(cenovnikDto.getDatumObjavljivanja(),null)){
			return "Greska! Postoji vec cenovnik sa tim datumom objavljivanja";
		}
		if(!setAndCheckDate(cenovnik, cenovnikDto.getDatumObjavljivanja())){
			return "Greska! Datum objavljivanja mora biti bar dva dana od sad";

		}
		if(!setAndCheckPopusti(cenovnik, cenovnikDto)){
			return "Greska! Popusti moraju biti izmedju 0 i 100";
		}
		saveCenovnik(cenovnik);
		return "Cenovnik je uspesno kreiran";
		
	}
	
	private boolean setAndCheckPopusti(Cenovnik cenovnik, CenovnikDTO cenovnikDto) {
		if(isProcentAndNotNull(cenovnikDto.getPopustDjak()) && 
				isProcentAndNotNull(cenovnikDto.getPopustNezaposlen()) && 
				isProcentAndNotNull(cenovnikDto.getPopustPenzioner()) &&
				isProcentAndNotNull(cenovnikDto.getPopustStudent())){
			cenovnik.setPopustDjak(cenovnikDto.getPopustDjak());
			cenovnik.setPopustNezaposlen(cenovnikDto.getPopustNezaposlen());
			cenovnik.setPopustPenzioner(cenovnikDto.getPopustPenzioner());
			cenovnik.setPopustStudent(cenovnikDto.getPopustStudent());
			return true;
		}
		return false;
	}

	public Stavka newStavka(StavkaCenovnikaDto stavkaDTO){
		try{
			if(stavkaDTO.getTipKarte() ==null || stavkaDTO.getVrstaPrevoza() == null ||
					stavkaDTO.getCena() ==null || (stavkaDTO.getNazivLinije()==null && stavkaDTO.getNazivZone() ==null))
				return null;
			Linija linija = null;
			Zona zona = null;
			if(stavkaDTO.getNazivLinije()!= null){
				linija = linijaService.findByName(stavkaDTO.getNazivLinije());
			}
			if(stavkaDTO.getNazivZone()!= null){
				zona = zonaService.findByName(stavkaDTO.getNazivZone());
			}
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
	
	public boolean checkWithFutureDates(Date date, Long id){
		if(date == null){
			return false;
		}
		Calendar day = Calendar.getInstance();
		Calendar future = Calendar.getInstance();
		day.setTime(date);
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		for(Cenovnik cenovnik : cenovnikRepository.findAllByObrisanFalse()){
			if((id != null && cenovnik.getId() != id )|| id == null){
				future.setTime(cenovnik.getDatumObjavljivanja());
				future.set(Calendar.HOUR_OF_DAY, 0);
				future.set(Calendar.MINUTE, 0);
				future.set(Calendar.SECOND, 0);
				if(day.equals(future)){
					return false;
				}
			}
		}
		return true;
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
	
	public boolean isProcentAndNotNull(Integer number){
		return number!=null && isProcent(number);
	}
	
	
	public boolean isProcent(Integer number){
		return number>=0 && number<=100;
	}

	public CenovnikDTO editCenovnik(CenovnikDTO cenovnikDto) {
		System.out.println(cenovnikDto.getId());
		Cenovnik cenovnik = findOne(cenovnikDto.getId());
		
		if(cenovnik == null ){
			System.out.println("null");
			return null;
		}
		
		if(cenovnikDto.getDatumObjavljivanja() != null){	
			if(!checkWithFutureDates(cenovnikDto.getDatumObjavljivanja(), cenovnikDto.getId())){
				return null;
			}
			if(!setAndCheckDate(cenovnik, cenovnikDto.getDatumObjavljivanja())){
				return null;
	
			}
		}
		if(!setAndCheckPopustiEdit(cenovnik, cenovnikDto)){
			return null;
		}
		cenovnik = cenovnikRepository.save(cenovnik);
		for(StavkaCenovnika s : cenovnik.getStavke()){
			System.out.println("printam: " + s.getCena());
		}
		CenovnikDTO res =  new CenovnikDTO(cenovnik);
		//for(S)
		return res;
		
	}

	private boolean setAndCheckPopustiEdit(Cenovnik cenovnik, CenovnikDTO cenovnikDto) {
		if(isProcentAndNotNull(cenovnikDto.getPopustDjak()))
			cenovnik.setPopustDjak(cenovnikDto.getPopustDjak());
		else if (cenovnikDto.getPopustDjak()!= null)
			return false;
		if(isProcentAndNotNull(cenovnikDto.getPopustStudent()))
			cenovnik.setPopustStudent(cenovnikDto.getPopustStudent());
		else if (cenovnikDto.getPopustStudent() != null)
			return false;
		if(isProcentAndNotNull(cenovnikDto.getPopustNezaposlen()))
			cenovnik.setPopustNezaposlen(cenovnikDto.getPopustNezaposlen());
		else if (cenovnikDto.getPopustNezaposlen() != null)
			return false;
		if(isProcentAndNotNull(cenovnikDto.getPopustPenzioner()))
			cenovnik.setPopustPenzioner(cenovnikDto.getPopustPenzioner());
		else if (cenovnikDto.getPopustPenzioner() != null)
			return false;
		return true;
	}

	public ArrayList<CenovnikDTO> getBuduci() {
		deleteIstekli();
		ArrayList<CenovnikDTO> retVal= new ArrayList<CenovnikDTO>(); 
		try{
			ArrayList<Cenovnik> cenovnici = (ArrayList<Cenovnik>) cenovnikRepository.findAllByObrisanFalse();
			Collections.sort(cenovnici, new SortCenovniciByDate());
			cenovnici.remove(0);
			for(Cenovnik c : cenovnici){
				retVal.add(new CenovnikDTO(c));
			}
			return retVal;	
		}
		catch(Exception e){
			return null;
		}
	}
	
	
}
