package proprietario;

import static org.junit.Assert.*;

import org.junit.Test;

import animale.Pet;
import animale.Taglia;
import animale.TipoAnimale;

public class ProprietarioTest {

	@Test
	public void testProprietario() {
		ProprietarioVip p=new ProprietarioVip("aaaaaaaaaaaaaaaa","Mario","Rossi");
		assertEquals("aaaaaaaaaaaaaaaa",p.cf);
		assertEquals("Mario",p.nome);
		assertEquals("Rossi",p.cognome);
		assertNotNull(p.listaPets);
	}

	@Test
	public void testInserisciPet() {
		ProprietarioVip p=new ProprietarioVip("aaaaaaaaaaaaaaaa","Mario","Rossi");
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		p.inserisciPet(pet1);
		assertEquals(pet1,p.listaPets.get(0));
	}

	@Test
	public void testPetNonRegistrato() {
		ProprietarioVip p=new ProprietarioVip("aaaaaaaaaaaaaaaa","Mario","Rossi");
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet2=new Pet("Amore",Taglia.PICCOLO,TipoAnimale.GATTO);
		p.inserisciPet(pet1);
		assertEquals(true,p.petNonRegistrato(pet2));
		assertEquals(false,p.petNonRegistrato(pet1));
	}
	
}
