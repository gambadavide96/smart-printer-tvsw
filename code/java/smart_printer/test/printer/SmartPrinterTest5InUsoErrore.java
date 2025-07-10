package printer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import printer.SmartPrinter.Servizio;
import printer.SmartPrinter.Stato;

public class SmartPrinterTest5InUsoErrore {
	
	private SmartPrinter stampante;
	
	@Before
	public void setup() {
		
		stampante = new SmartPrinter();
	    Utente davide = new Utente("Davide", 2096, 1, 1000);
	    stampante.aggiungiUtente(davide);
	    
	    assertFalse(stampante.stampanteInUso()); //Azione non consentita
	    assertFalse(stampante.gestioneErrore()); //Azione non consentita
	    
	    stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
	}

	@Test
	public void testStampanteInUsoScansione() {
		
		//Seleziono scansione come operazione
		stampante.setCollegatoWireless(true);
		stampante.scansione();
		assertEquals(Stato.INUSO, stampante.getPrinterState());
		//Il servizio selezionato è la scansione
		assertEquals(Servizio.SCANSIONE,stampante.getSelectedService());
		
		assertTrue(stampante.stampanteInUso()); //La scansione viene effettuata senza problemi
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		//I device sono stati scollegati
		assertFalse(stampante.isCollegatoWireless());
		assertFalse(stampante.isCollegatoCavo());
		
	}
	
	@Test
	public void testStampanteInUsoNotCartaInceppata() {
		
		//Seleziono una stampa come operazione
		stampante.stampaBN();
		assertEquals(Stato.INUSO, stampante.getPrinterState());
		//Il servizio selezionato è la stampa BN
		assertEquals(Servizio.PRINTBN,stampante.getSelectedService());
		
		assertTrue(stampante.stampanteInUso()); //La stampa viene effettuata senza problemi
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
	}
	
	@Test
	public void testStampanteInUsoCartaInceppata() {
		
		//Seleziono una stampa come operazione
		stampante.stampaCOL();
		assertEquals(Stato.INUSO, stampante.getPrinterState());
		//Il servizio selezionato è la stampa COL
		assertEquals(Servizio.PRINTCOL,stampante.getSelectedService());
		
		stampante.cartaInceppata(); //la carta si inceppa
		assertFalse(stampante.stampanteInUso()); //La stampa si interrompe
		assertEquals(1000, stampante.getUtenteCorrente().getCredito()); //Il credito utente viene rimborsato
		assertEquals(Stato.ERRORE,stampante.getPrinterState());
		
	}
	
	@Test
	public void testGestioneErrore() {
		//Seleziono una stampa come operazione
		stampante.stampaCOL();
		stampante.cartaInceppata(); //la carta si inceppa
		stampante.stampanteInUso(); //la stampa si interrompe
		assertEquals(Stato.ERRORE,stampante.getPrinterState());
		//la carta è ancora inceppata
		assertFalse(stampante.gestioneErrore());
		//la carta viene sistemata
		stampante.sistemaCarta();
		assertTrue(stampante.gestioneErrore());
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
	}

}
