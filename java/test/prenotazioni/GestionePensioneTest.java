package prenotazioni;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import animale.Pet;
import animale.Taglia;
import animale.TipoAnimale;
import proprietario.Proprietario;
import proprietario.ProprietarioStandard;
import proprietario.ProprietarioVip;
import struttura.Struttura;

public class GestionePensioneTest {

	@Test
	public void testAddPrenotazione() {
		Struttura oasi=Struttura.getInstance();
		//Creo proprietari
		Proprietario p1=new ProprietarioStandard("PRGCTEGAG15465AF","Davide","Gamba");
		Proprietario p2=new ProprietarioVip("MRYSIDPCHSD546SK","Mirco","Mazzoleni");
		Proprietario p3=new ProprietarioVip("AHJDHJDHJDDKHJKL","Violeta","Parra");
		oasi.registraProprietario(p1);
		oasi.registraProprietario(p2);
		oasi.registraProprietario(p3);
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet2=new Pet("Amore",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet3= new Pet("Fido",Taglia.MEDIO,TipoAnimale.CANE);
		Pet pet4=new Pet("Pluto",Taglia.GRANDE,TipoAnimale.CANE);
		Pet pet5=new Pet("Titti",Taglia.PICCOLO,TipoAnimale.PENNUTO);
		Pet pet6=new Pet("Splint",Taglia.PICCOLO,TipoAnimale.CRICETO);
		Pet pet7= new Pet("Silvestro",Taglia.MEDIO,TipoAnimale.CANE);
		Pet pet8= new Pet("Bull",Taglia.GRANDE,TipoAnimale.CANE);
		Pet pet9=new Pet("Sissi",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet10=new Pet("Franz",Taglia.MEDIO,TipoAnimale.CANE);
		Pet pet11=new Pet("Scooby",Taglia.MEDIO,TipoAnimale.CANE);
		// Associo pet a padrone
		p1.inserisciPet(pet1);
		p2.inserisciPet(pet2);
		p2.inserisciPet(pet3);
		p3.inserisciPet(pet4);
		p2.inserisciPet(pet5);
		p1.inserisciPet(pet6);
		p3.inserisciPet(pet7);
		p3.inserisciPet(pet8);
		p1.inserisciPet(pet9);
		p1.inserisciPet(pet10);
		p2.inserisciPet(pet11);
		
		//GESTIONE PRENOTAZIONI
		try {
		//Pet taglia small in gabbia 2
		PrenotazionePensione preno1=new PrenotazionePensione(p1,pet1,LocalDate.of(2022,1, 28),LocalDate.of(2022,2, 7));
		GestionePensione.addPrenotazione(preno1);
		assertTrue(!GestionePensione.getListaPrenotazioni().isEmpty());
		//Secondo Pet taglia small stessa gabbia ma data diversa e quindi prenotazione possibile in gabbia 2
		PrenotazionePensione preno2=new PrenotazionePensione(p2,pet2,LocalDate.of(2022,1, 5),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno2);
		assertEquals(oasi.getListaGabbie().get(1),preno2.getGabbia());
		//Pet taglia medium in gabbia 5
		PrenotazionePensione preno3=new PrenotazionePensione(p2,pet3,LocalDate.of(2022,1, 5),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno3);
		//Pet taglia large in gabbia 1
		PrenotazionePensione preno4=new PrenotazionePensione(p3,pet4,LocalDate.of(2022,1, 5),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno4);
		// Pet Taglia Medium Gabbia 9
		PrenotazionePensione preno5=new PrenotazionePensione(p3,pet7,LocalDate.of(2022,1, 4),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno5);
		// stessa taglia di pet3 ma data diversa quindi prenotazione possibile in gabbia 5
		PrenotazionePensione preno6=new PrenotazionePensione(p3,pet7,LocalDate.of(2022,2, 7),LocalDate.of(2022,2, 15));
		GestionePensione.addPrenotazione(preno6);
		assertEquals(oasi.getListaGabbie().get(4),preno6.getGabbia());
		// Assegno una cuccia piccola con la stessa data di quando la cuccia 2 è occupata, così occupo la cuccia 7
		PrenotazionePensione preno7=new PrenotazionePensione(p2,pet5,LocalDate.of(2022,1, 28),LocalDate.of(2022,2,7));
		GestionePensione.addPrenotazione(preno7);
		assertEquals(oasi.getListaGabbie().get(6),preno7.getGabbia());
		// Assegno una cuccia piccola quando la 2 e la 7 sono occupate cosi cambio stanza e vado nella cuccia 6
		PrenotazionePensione preno8=new PrenotazionePensione(p1,pet9,LocalDate.of(2022,1, 28),LocalDate.of(2022,2,7));
		GestionePensione.addPrenotazione(preno8);
		assertEquals(oasi.getListaGabbie().get(5),preno8.getGabbia());
		// stessa data e taglia di pet7 quindi prenotazione in stanza 3 gabbia 9 
		PrenotazionePensione preno9=new PrenotazionePensione(p1,pet10,LocalDate.of(2022,2, 6),LocalDate.of(2022,2, 9));
		GestionePensione.addPrenotazione(preno9);
		assertEquals(oasi.getListaGabbie().get(8),preno9.getGabbia());
		// stessa data e taglia di pet7 quindi prenotazione non possibile per mancanza di gabbie 
		PrenotazionePensione preno10=new PrenotazionePensione(p2,pet11,LocalDate.of(2022,2, 4),LocalDate.of(2022,2, 8));
		GestionePensione.addPrenotazione(preno10);
		assertNull(preno10.getGabbia());
		// stessa data e taglia di pet7 quindi prenotazione non possibile per mancanza di gabbie 
		PrenotazionePensione preno11=new PrenotazionePensione(p2,pet11,LocalDate.of(2022,2, 6),LocalDate.of(2022,2, 12));
		GestionePensione.addPrenotazione(preno11);
		assertNull(preno11.getGabbia());
		
		
		GestionePensione.stampaElencoPrenotazioni();
		
		}
		catch(PrenotazioneNonValidaException e){
			System.out.println("Prenotazione non Valida:");
		}
	}
	
	@Test
	public void testConteggioFabbisognoCani() {
		Struttura oasi=Struttura.getInstance();
		oasi.inizializzaStruttura();
		Proprietario p12=new ProprietarioStandard("PRGCTEGAG15465AF","Daniele","Gritti");
		oasi.registraProprietario(p12);
		Pet pet18= new Pet("Big",Taglia.GRANDE,TipoAnimale.CANE);
		p12.inserisciPet(pet18);
		
		try {
			//Pet taglia small in gabbia 2
			PrenotazionePensione preno1=new PrenotazionePensione(p12,pet18,LocalDate.of(2022,1, 15),LocalDate.of(2022,1, 20));
			GestionePensione.addPrenotazione(preno1);
			GestionePensione.stampaElencoPrenotazioni();
			
			}
			catch(PrenotazioneNonValidaException e){
				System.out.println("Prenotazione non Valida:");
			}
		
		
		int fabbisogno=GestionePensione.conteggioFabbisognoCani(LocalDate.of(2022, 1, 1));
		assertEquals(1500,fabbisogno);
	}
	
	
	
}
