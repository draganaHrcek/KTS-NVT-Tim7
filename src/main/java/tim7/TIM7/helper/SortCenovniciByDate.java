package tim7.TIM7.helper;

import java.util.Comparator;
import java.util.Date;

import tim7.TIM7.model.Cenovnik;

public class SortCenovniciByDate implements Comparator<Cenovnik> {

	@Override
	public int compare(Cenovnik c1, Cenovnik c2) {
		return c1.getDatumObjavljivanja().compareTo(c2.getDatumObjavljivanja());
	}

}
