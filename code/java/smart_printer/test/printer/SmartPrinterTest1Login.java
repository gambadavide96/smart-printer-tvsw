package printer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import printer.SmartPrinter.Stato;
import printer.SmartPrinter.StatoMacchina;


//Test Suite che copre tutto il processo di autenticazione e lo stato di guasto.

public class SmartPrinterTest1Login {
	
	private SmartPrinter stampante;
	private Utente matteo;
	private Utente davide;
	private Utente giovanni;
	
	@Before
	public void setup() {
	    stampante = new SmartPrinter();
	    matteo = new Utente("Matteo", 4723, 2, 1000);
	    davide = new Utente("Davide", 2096, 1, 1000);
	    giovanni = new Utente("Giovanni",3123,3,1000);
	    
	    assertTrue(stampante.aggiungiUtente(davide));
	    assertTrue(stampante.aggiungiUtente(matteo));
	}
	
	
	@Test
	public void testAggiungiUtente() {
		
		stampante.statoCorrente();
	    
		assertNotNull(stampante.getUtentebyNumBadge(davide.getBadgeId()));
		assertNotNull(stampante.getUtentebyNumBadge(matteo.getBadgeId()));
		
		assertFalse(stampante.aggiungiUtente(giovanni)); //Non è possibile aggiungere altri utenti
		
		assertNull(stampante.getUtentebyNumBadge(1111)); //ricerca con NumBadge casuale
	}

	//All'inizio la stampante è spenta e poi si accende
	@Test
	public void testAccendiStampante() {
		
		assertEquals(Stato.SPENTA,stampante.getPrinterState());
		assertTrue(stampante.accendiStampante()); 	//Stampante in accensione
		assertFalse(stampante.accendiStampante());	//La stampante è gia accesa
		assertEquals(Stato.AVVIO,stampante.getPrinterState());
	}
	
	@Test
	public void testAvvioStampante() {
		
		assertFalse(stampante.avvioStampante()); //azione non consentita
		stampante.accendiStampante();
		
		assertTrue(stampante.avvioStampante()); //La stampante si è avviata correttamente
		assertEquals(Stato.MOSTRABADGE,stampante.getPrinterState());
		
	}

	
	@Test
	public void testAvvioStampanteGuasta() {
	
		stampante.accendiStampante();
		
		stampante.guastoStampante(); //Stampante va in guasto
		assertEquals(StatoMacchina.GUASTA,stampante.getSpiaGuasto());
		
		assertFalse(stampante.avvioStampante()); //La stampante si avvia ma va outofservice
		assertEquals(Stato.OUTOFSERVICE,stampante.getPrinterState());
		
	}
	
	@Test
	public void testGestioneGuasto() {
		
		assertFalse(stampante.gestioneGuasto()); //azione non consentita
		
		stampante.accendiStampante();
		stampante.guastoStampante(); //Stampante va in guasto
		stampante.avvioStampante(); //La stampante si avvia ma va outofservice
		
		assertFalse(stampante.gestioneGuasto()); //La stampante è ancora guasta
		assertEquals(Stato.OUTOFSERVICE,stampante.getPrinterState());
		
		stampante.riparazioneStampante(); //riparo la stampante
		assertEquals(StatoMacchina.NONGUASTA,stampante.getSpiaGuasto());
		
		assertTrue(stampante.gestioneGuasto()); //La stampante è riparata correttamente
		assertEquals(Stato.MOSTRABADGE,stampante.getPrinterState());
		
	}
	
	@Test 
	public void testIdentificazioneUtente() {
		
		assertFalse(stampante.identificazioneUtente(0)); //azione non consentita
		
		stampante.accendiStampante();
		stampante.avvioStampante(); //La stampante si è avviata correttamente
		
		assertFalse(stampante.identificazioneUtente(111)); //Inserisco un Numbadge non valido
		assertEquals(Stato.MOSTRABADGE,stampante.getPrinterState());
		
		assertTrue(stampante.identificazioneUtente(4723)); //Inserisco un Numbadge valido
		assertEquals(Stato.INSERISCIPIN,stampante.getPrinterState());
		assertEquals(matteo,stampante.getUtenteCorrente());
		
		
	}
	
	@Test
	public void testInserimentoPin() {
		
		assertFalse(stampante.inserimentoPin(0)); //azione non consentita
		
		stampante.accendiStampante();
		stampante.avvioStampante(); //La stampante si è avviata correttamente
		stampante.identificazioneUtente(2096); //Inserisco un Numbadge valido
		
		assertFalse(stampante.inserimentoPin(0)); //inserisco pin sbagliato
		assertEquals(Stato.MOSTRABADGE,stampante.getPrinterState());
		
		stampante.identificazioneUtente(2096); 		//reinserisco il badge
		assertTrue(stampante.inserimentoPin(1));	//Inserisco Pin corretto
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
		
	}
	
	@Test
	public void testSceltaServizioBN() {
		
		assertFalse(stampante.sceltaServizio("x")); //Azione non consentita
		
		stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		
		//Chiamo un valore non valido
		assertFalse(stampante.sceltaServizio("x"));
		//Chiamo stampe e scansioni
		assertTrue(stampante.sceltaServizio("BN"));
		assertEquals(Stato.INUSO,stampante.getPrinterState());
	}
	
	@Test
	public void testSceltaServizioCOL() {
		
		stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		
		//Chiamo un valore non valido
		assertFalse(stampante.sceltaServizio("x"));
		//Chiamo stampe e scansioni
		assertTrue(stampante.sceltaServizio("COL"));
		assertEquals(Stato.INUSO,stampante.getPrinterState());
		
	}
	
	@Test
	public void testSceltaServizioS() {
		
		stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		
		//Chiamo un valore non valido
		assertFalse(stampante.sceltaServizio("x"));
		//Chiamo stampe e scansioni
		assertTrue(stampante.sceltaServizio("S"));
		//Con entrambi i device a false la scansione non viene effettuata e la
		//stampante va nello stato di PRONTA
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
	}
	
	@Test
	public void testSceltaServizioE() {
		
		stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		
		//Chiamo un valore non valido
		assertFalse(stampante.sceltaServizio("x"));
		//Chiamo stampe e scansioni
		assertTrue(stampante.sceltaServizio("E"));
		assertEquals(null,stampante.getUtenteCorrente()); //l'utente deve essere scollegato
		assertEquals(Stato.SPENTA,stampante.getPrinterState());
		
	}
	
	
}
