package prenotazioni;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import animale.Pet;
import animale.Taglia;
import animale.TipoAnimale;
import proprietario.Proprietario;
import proprietario.ProprietarioStandard;
import struttura.Struttura;

public class PrenotazionePensioneTest {

	@Test
	public void testPrenotazionePensione() {
		Struttura oasi=Struttura.getInstance();
		Proprietario p1=new ProprietarioStandard("PRGCTEGAG15465AF","Davide","Gamba");
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		oasi.registraProprietario(p1);
		p1.inserisciPet(pet1);
		try {
			PrenotazionePensione preno1=new PrenotazionePensione(p1,pet1,LocalDate.of(2022,1, 28),LocalDate.of(2022,2, 7));
			assertEquals(p1,preno1.getProprietario());
			assertEquals(pet1,preno1.getAnimale());
			assertEquals(LocalDate.of(2022,1, 28),preno1.getDataInizio());
			assertEquals(LocalDate.of(2022,2, 7),preno1.getDataFine());
	
		} catch (PrenotazioneNonValidaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
