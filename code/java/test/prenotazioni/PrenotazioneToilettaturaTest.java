package prenotazioni;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import animale.Pet;
import animale.Taglia;
import animale.TipoAnimale;
import proprietario.Proprietario;
import proprietario.ProprietarioStandard;
import proprietario.ProprietarioVip;
import struttura.Struttura;

public class PrenotazioneToilettaturaTest {

	@Test
	public void testPrenotazioneToilettatura() {
		Struttura oasi=Struttura.getInstance();
		oasi.inizializzaStruttura();
		Proprietario p1=new ProprietarioStandard("PRGCTEGAG15465AF","Davide","Gamba");
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		oasi.registraProprietario(p1);
		p1.inserisciPet(pet1);
		try {
			PrenotazioneToilettatura toile1=new PrenotazioneToilettatura(p1,pet1,LocalDateTime.of(2022, 1, 4, 13, 0));
			assertEquals(p1,toile1.getProprietario());
			assertEquals(pet1,toile1.getAnimale());
		} catch (ToilettaturaNonValidaException e) {
			e.getMessage();
		}
	}

	@Test
	public void testCalcolaAddebito() {
		Struttura oasi=Struttura.getInstance();
		Proprietario p1=new ProprietarioStandard("PRGCTEGAG15465AF","Davide","Gamba");
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		oasi.registraProprietario(p1);
		p1.inserisciPet(pet1);
		try {
			PrenotazioneToilettatura toile1=new PrenotazioneToilettatura(p1,pet1,LocalDateTime.of(2022, 1, 4, 13, 0));
			toile1.calcolaAddebito();
			assertEquals(20,toile1.getCostoToilettatura());
		} catch (ToilettaturaNonValidaException e) {
			e.getMessage();
		}
	}
}