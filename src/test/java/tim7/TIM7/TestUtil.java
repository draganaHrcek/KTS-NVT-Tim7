package tim7.TIM7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tim7.TIM7.model.Cenovnik;
import tim7.TIM7.model.Linija;
import tim7.TIM7.model.Stavka;
import tim7.TIM7.model.StavkaCenovnika;
import tim7.TIM7.model.TipKarteCenovnik;
import tim7.TIM7.model.TipVozila;
import tim7.TIM7.model.Zona;

public class TestUtil {
	public static String json(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		return mapper.writeValueAsString(object);
	}

	public static String generateToken(String userDetails) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", userDetails);
		claims.put("created", new Date(System.currentTimeMillis()));
		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + 18000 * 1000))
				.signWith(SignatureAlgorithm.HS512, "myXAuthSecret").compact();
	}

	// metoda za kreiranje test cenovnika , koristi se u testovima za racunanje cene
	// kupljene karte
	public static Cenovnik kreiranjeCenovnika() {

		Calendar before = Calendar.getInstance();
		Calendar after = Calendar.getInstance();
		before.set(2018, 9, 1);
		after.set(2019, 6, 30);

		Cenovnik trenutni = new Cenovnik(before.getTime(), after.getTime(), new ArrayList<>(), false, 20, 30, 10, 25);
		Stavka stavka1 = new Stavka(TipKarteCenovnik.DNEVNA, TipVozila.AUTOBUS, new Zona("gradksa", false),
				new Linija("linija1", false), false);
		Stavka stavka2 = new Stavka(TipKarteCenovnik.GODISNJA, TipVozila.TRAMVAJ, new Zona("prigradska", false),
				new Linija("linija2", false), false);

		StavkaCenovnika stavkaCenovnika1 = new StavkaCenovnika(50, stavka1, trenutni, false);
		StavkaCenovnika stavkaCenovnika2 = new StavkaCenovnika(1000, stavka2, trenutni, false);

		ArrayList<StavkaCenovnika> stavke = new ArrayList<StavkaCenovnika>();
		stavke.add(stavkaCenovnika1);
		stavke.add(stavkaCenovnika2);
		trenutni.setStavke(stavke);

		return trenutni;
	}
}
