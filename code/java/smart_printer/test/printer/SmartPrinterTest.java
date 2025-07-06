package printer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import printer.SmartPrinter.Stato;
import printer.SmartPrinter.StatoMacchina;

public class SmartPrinterTest {
	
	private SmartPrinter stampante;
	private Utente matteo;
	private Utente davide;
	
	@Before
	public void setup() {
	    stampante = new SmartPrinter();
	    matteo = new Utente("Matteo", 4723, 2, 1000);
	    davide = new Utente("Davide", 2096, 1, 1000);
	    stampante.aggiungiUtente(matteo);
	    stampante.aggiungiUtente(davide);
	}
	
	
	@Test
	public void aggiungiUtenteTest() {
		assertTrue(stampante.getUtenti().containsKey(davide.getBadgeId()));
		assertTrue(stampante.getUtenti().containsKey(matteo.getBadgeId()));
	}

	//All'inizio la stampante è spenta e poi si accende
	@Test
	public void accendiStampanteTest() {
		
		assertEquals(Stato.SPENTA,stampante.getPrinterState());
		assertTrue(stampante.accendiStampante()); 	//Stampante in accensione
		assertFalse(stampante.accendiStampante());	//La stampante è gia accesa
		assertEquals(Stato.AVVIO,stampante.getPrinterState());
	}
	
	@Test
	public void avvioStampanteTest() {
		
		assertFalse(stampante.avvioStampante()); //azione non consentita
		stampante.accendiStampante();
		
		assertTrue(stampante.avvioStampante()); //La stampante si è avviata correttamente
		assertEquals(Stato.MOSTRABADGE,stampante.getPrinterState());
		
	}

	
	@Test
	public void avvioStampanteTestGuasta() {
	
		stampante.accendiStampante();
		
		stampante.guastoStampante(); //Stampante va in guasto
		assertEquals(StatoMacchina.GUASTA,stampante.getSpiaGuasto());
		
		assertFalse(stampante.avvioStampante()); //La stampante si avvia ma va outofservice
		assertEquals(Stato.OUTOFSERVICE,stampante.getPrinterState());
		
	}
	
	@Test
	public void gestioneGuastoTest() {
		
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
	public void identificazioneUtenteTest() {
		
		assertFalse(stampante.identificazioneUtente(0)); //azione non consentita
		
		stampante.accendiStampante();
		stampante.avvioStampante(); //La stampante si è avviata correttamente
		
		assertFalse(stampante.identificazioneUtente(111)); //Inserisco un Numbadge non valido
		assertEquals(Stato.MOSTRABADGE,stampante.getPrinterState());
		
		assertTrue(stampante.identificazioneUtente(4723)); //Inserisco un Numbadge valido
		assertEquals(Stato.INSERISCIPIN,stampante.getPrinterState());
		assertEquals(matteo,stampante.getUtenteCorrente());
		
	}
	
}
