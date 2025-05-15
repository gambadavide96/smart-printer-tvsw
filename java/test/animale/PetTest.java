package animale;

import static org.junit.Assert.*;

import org.junit.Test;

public class PetTest {

	@Test
	public void testPetStringTagliaTipoAnimale() {
		//creo oggetto pet
		Pet p=new Pet("miao",Taglia.PICCOLO, TipoAnimale.GATTO);
		
	    assertEquals(p.getNomePet(), "miao");
	    assertEquals(p.getGrandezza(),Taglia.PICCOLO);
	    assertEquals(p.getTipoAnimale(), TipoAnimale.GATTO);
	}

	@Test
	public void testPetStringTipoAnimale() {
		
		//creo oggetto pet
		Pet p=new Pet("hamtaro", TipoAnimale.CRICETO);
		
		assertEquals(p.getNomePet(), "hamtaro");
		assertEquals(p.getTipoAnimale(), TipoAnimale.CRICETO);
	}

}
