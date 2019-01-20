package tim7.TIM7.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tim7.TIM7.model.Administrator;
import tim7.TIM7.model.Korisnik;
import tim7.TIM7.model.Osoba;
import tim7.TIM7.model.Verifikator;
import tim7.TIM7.repositories.OsobaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private OsobaRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  Osoba user = userRepository.findByKorIme(username);

    if (user == null) {
      throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
    } else {
//    	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//    	for (UserAuthority ua: user.getUserAuthorities()) {
//    		grantedAuthorities.add(
//    			new SimpleGrantedAuthority(ua.getAuthority().getName()));
//    	}
    	
    	//Java 1.8 way 
    	
    	
    	//PROMENJEN NACIN DODELJIVANJA PRAVA KORISNICIMA U SKLADU SA NASIM MODELOM PODATAKA
    	List<GrantedAuthority> grantedAuthorities= new ArrayList<GrantedAuthority>();
    	if (user instanceof Korisnik) {
    		grantedAuthorities.add( new SimpleGrantedAuthority("KORISNIK"));
    		
    	}else if(user instanceof Administrator) {
    		grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
    		
    	}else if (user instanceof Verifikator) {
    		
    		grantedAuthorities.add(new SimpleGrantedAuthority("VERIFIKATOR"));
    	}else{
    		grantedAuthorities.add( new SimpleGrantedAuthority("KONDUKTER"));
    	}
    	
    	
    	return new org.springframework.security.core.userdetails.User(
    		  user.getKorIme(),
    		  user.getLozinka(),
    		  grantedAuthorities);
    }
  }

}
