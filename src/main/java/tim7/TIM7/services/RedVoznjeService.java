package tim7.TIM7.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tim7.TIM7.dto.RasporedVoznjeDTO;
import tim7.TIM7.dto.RedVoznjeDTO;
import tim7.TIM7.model.DanUNedelji;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.RasporedVoznje;
import tim7.TIM7.model.RedVoznje;
import tim7.TIM7.repositories.LinijaRepository;
import tim7.TIM7.repositories.RasporedVoznjeRepository;
import tim7.TIM7.repositories.RedVoznjeRepository;

@Service
public class RedVoznjeService {
	
	@Autowired 
	RedVoznjeRepository redVoznjeRepository;
	
	@Autowired
	RasporedVoznjeRepository rasporedVoznjeRepository;
	
	@Autowired
	LinijaRepository linijaRepository;
	
	
	public RedVoznje findById(Long id){
		return redVoznjeRepository.findById(id).get();
	}
	
	public List<RedVoznje> findAll(){
		return redVoznjeRepository.findAll();
	}
	
	public RedVoznje save(RedVoznje redVoznje){
		return redVoznjeRepository.save(redVoznje);
	}
	
	
	public void delete(Long id){
		RedVoznje redVoznje = findById(id);
		redVoznje.setObrisan(true);
		for (RasporedVoznje rv : redVoznje.getRasporediVoznje()){
			rv.setObrisan(true);
			rasporedVoznjeRepository.save(rv);
		}
		save(redVoznje);
	}
	
	
	//za postavljanje aktuelnog reda voznje
	public RedVoznjeDTO getTrenutniRedVoznje(){
		List<RedVoznje> aktivniRedoviVoznje=redVoznjeRepository.findByObrisanFalse();
		if (aktivniRedoviVoznje.size()==0){
			return null;
		}else{
			Calendar now = Calendar.getInstance();
			if (aktivniRedoviVoznje.size()==1){
				if (now.getTime().after(aktivniRedoviVoznje.get(0).getDatumObjavljivanja())){					
					return new RedVoznjeDTO(aktivniRedoviVoznje.get(0));
				}else{
					return null;
				}
			}else{
				RedVoznje stari;
				RedVoznje novi;
				if (aktivniRedoviVoznje.get(0).getDatumObjavljivanja().before(aktivniRedoviVoznje.get(1).getDatumObjavljivanja())){
					stari=aktivniRedoviVoznje.get(0);
					novi=aktivniRedoviVoznje.get(1);
				}else{
					stari=aktivniRedoviVoznje.get(1);
					novi=aktivniRedoviVoznje.get(0);
				}
				if (now.getTime().after(novi.getDatumObjavljivanja())){
					delete(stari.getId());
					return new RedVoznjeDTO(novi);
				}else{
					return new RedVoznjeDTO(stari);
				}
			}
		}
		
	}
	
	//dobavljanje buduceg reda voznje
	public RedVoznjeDTO getBuduciRedVoznje() {
		//ne mora da postoji trenutni da bi postojao buduci
		/*RedVoznjeDTO trenutniRedVoznjeDto = getTrenutniRedVoznje();
		RedVoznje buduciRedVoznje=redVoznjeRepository.findByIdNotAndObrisanFalse(trenutniRedVoznjeDto.getId());
		if (buduciRedVoznje==null){
			return null;
		}else{
			return new RedVoznjeDTO(buduciRedVoznje);
		}*/
		
		RedVoznjeDTO trenutniRedVoznjeDto = getTrenutniRedVoznje();
		RedVoznjeDTO buduciRedVodnje=null;
		if (trenutniRedVoznjeDto==null){
			List<RedVoznje> neobrisaniRedoviVoznje = redVoznjeRepository.findByObrisanFalse();
			if (neobrisaniRedoviVoznje.size()==0){
				return null;
			}else{
				return new RedVoznjeDTO(neobrisaniRedoviVoznje.get(0));
			}
		}else{
			RedVoznje buduciRedVoznje=redVoznjeRepository.findByIdNotAndObrisanFalse(trenutniRedVoznjeDto.getId());
			if (buduciRedVoznje==null){
				return null;
			}else{
				return new RedVoznjeDTO(buduciRedVoznje);
			}
		}
		
		

	}
	
	
	//za dobijanje zeljenog rasporeda
	public RasporedVoznjeDTO getSpecificRasporedVoznje(DanUNedelji danUNedelji, String nazivLinije){
		RedVoznjeDTO trenutniRedVoznje = getTrenutniRedVoznje();
		if (trenutniRedVoznje==null){
			return null;
		}
		Long idTrenutnogRedaVoznje=trenutniRedVoznje.getId();
		RedVoznje redVoznje = findById(idTrenutnogRedaVoznje);
		Linija linija = linijaRepository.findByNaziv(nazivLinije);
		RasporedVoznje rasporedVoznje = rasporedVoznjeRepository.findByDanUNedeljiAndLinijaAndRedVoznje(danUNedelji, linija, redVoznje);
		if (rasporedVoznje==null){
			return null;
		}else{
			return new RasporedVoznjeDTO(rasporedVoznje);
		}
		
	}
	
	
	//za kreiranje novog reda voznje, postavljace se samo godina, mesec i dan, ne znam da li vreme da bude 00:00 ili 01:00, za sad 0
	public String createRedVoznje(Date datumObjavljivanja){
		RedVoznjeDTO buduciRedVoznje=getBuduciRedVoznje();
		Calendar sutrasnjiDatum=Calendar.getInstance();
		sutrasnjiDatum.add(Calendar.DAY_OF_MONTH, 1);
		sutrasnjiDatum.getTime().setHours(0);
		sutrasnjiDatum.getTime().setMinutes(0);
		if (buduciRedVoznje!=null){
			return "POSTOJI";
		}else if(datumObjavljivanja.before(sutrasnjiDatum.getTime())){
			return "LOS DATUM"; 
		}else{
			RedVoznje noviRedVoznje = new RedVoznje(false, datumObjavljivanja);
			save(noviRedVoznje);
			return "KREIRAN";
		}
	}
	
	
	//za brisanje buduceg reda voznje, moze da se obrise samo ako nije prosao datum objavljivanja
	//iako postavljanje trenutnog reda voznje postavlja i status da li je obrisan, za svaki slucaj
	//pisem proveru
	
	public String deleteBuduciRedVoznje(){
		RedVoznjeDTO buduciRedVoznje = getBuduciRedVoznje();
		if (buduciRedVoznje==null){
			return "NE POSTOJI";
		}else{
			delete(buduciRedVoznje.getId());
			return "OBRISAN";
		}
	}

	
}
