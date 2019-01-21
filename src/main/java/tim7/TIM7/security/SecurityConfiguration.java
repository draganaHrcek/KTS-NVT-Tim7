package tim7.TIM7.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuthentication(
			AuthenticationManagerBuilder authenticationManagerBuilder)
			throws Exception {
		
		authenticationManagerBuilder
				.userDetailsService(this.userDetailsService).passwordEncoder(
						passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public AuthenticationTokenFilter authenticationTokenFilterBean()
			throws Exception {
		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
		authenticationTokenFilter
				.setAuthenticationManager(authenticationManagerBean());
		return authenticationTokenFilter;
	}

	//OVDE DODATI PRAVA PRISTUPA ZA SVAKI NOVI KONTROLER 
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors();
		httpSecurity
			.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				.antMatchers("/osoba/prijava").
					permitAll() 
				.antMatchers("/osoba/registracija").
					permitAll() 
				.antMatchers(HttpMethod.POST, "/cenovnici").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/linije/zaCenovnik").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/karte/kupovinaKarte").
					hasAuthority("KORISNIK")
				.antMatchers(HttpMethod.DELETE, "/linije/brisi/{id}").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/linije/mijenjaj").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/linije/dodaj").
					hasAuthority("ADMIN")
					.antMatchers(HttpMethod.DELETE, "/stanice/brisi/{id}").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/stanice/mijenjaj").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/stanice/dodaj").
					hasAuthority("ADMIN")
					.antMatchers(HttpMethod.DELETE, "/vozila/brisi/{id}").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/vozila/mijenjaj").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/vozila/dodaj").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/vozila/dodajULiniju/{lineId}/{vehicleId}").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/vozila/izbaciIzLinije/{id}").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/zone/brisi/{id}").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/zone/mijenjaj").
					hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/zone/dodaj").
					hasAuthority("ADMIN")
					.antMatchers(HttpMethod.POST, "/karte/izlistajKarte").
					hasAuthority("KORISNIK")
					.antMatchers(HttpMethod.POST, "/osoba/izmenaPodataka").
					hasAuthority("KORISNIK")
				.antMatchers(HttpMethod.POST, "/api/**")
				
				
				//ZA SADA NAM NE TREBAJU PRAVA PRISTUPA SVI MOGU SVE ZAHTEVE(POST, GET, PUT,....)
				// 	.hasAuthority("ROLE_ADMIN") //only administrator can add and edit data
				//.anyRequest()
				.authenticated();
				//if we use AngularJS on client side
				//.and().csrf().csrfTokenRepository(csrfTokenRepository()); 
		
		// Custom JWT based authentication
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
				UsernamePasswordAuthenticationFilter.class);
	} 
	
	/**
	 * If we use AngularJS as a client application, it will send CSRF token using 
	 * the name X-XSRF token. We have to tell Spring to expect this name instead of 
	 * X-CSRF-TOKEN (which is default one)
	 * @return
	 */
//	private CsrfTokenRepository csrfTokenRepository() {
//		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		  repository.setHeaderName("X-XSRF-TOKEN");
//		  return repository;
//	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("X-Auth-Token", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
