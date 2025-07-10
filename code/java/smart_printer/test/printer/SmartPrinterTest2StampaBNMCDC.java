package printer;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import printer.SmartPrinter.Stato;


//Test Suite parametrica MCDC per la stampa BN

@RunWith(Parameterized.class)
public class SmartPrinterTest2StampaBNMCDC {
	
	private SmartPrinter stampante;
	
	private int tonerNero;
    private int carta;
    private int credito;
    private boolean expectedResult;
    private Stato expectedState;
    
    public SmartPrinterTest2StampaBNMCDC(int creditoCorrente, int tonerNero, int carta, 
    		boolean expectedResult,Stato expectedState) {
        this.tonerNero = tonerNero;
        this.carta = carta;
        this.credito = creditoCorrente;
        this.expectedResult = expectedResult;
        this.expectedState = expectedState;
    }
    
    @Parameters
    public static Collection<Object[]> stampaBNParametri() {
        return Arrays.asList(new Object[][] {
        	//Credito, tonerNero, carta, expected
            {45, 100, 500, false,Stato.PRONTA},		// credito insufficiente
            {1000, 5, 10, true,Stato.INUSO},		// Caso ok
            {1000, 4, 10, false,Stato.PRONTA},		// decide toner
            {1000, 5, 9, false,Stato.PRONTA}		// decide carta
        });
    }
	
	@Before
	public void setup() {
	    stampante = new SmartPrinter();
	    Utente davide = new Utente("Davide", 2096, 1, credito);
	    stampante.aggiungiUtente(davide);
	    
	    assertFalse(stampante.stampaBN()); //Operazione non consentita
	    
	    stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
		//Imposto risorse per il test parametrico
		stampante.setTonerNero(tonerNero);
        stampante.setFogliCarta(carta);
		
	}

	@Test
	public void testStampaBN() {
		boolean result = stampante.stampaBN();
        assertEquals(expectedResult, result);
        assertEquals(expectedState,stampante.getPrinterState());
	}
	

}
