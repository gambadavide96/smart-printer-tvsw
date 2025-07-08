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


//Test Suite MCDC per la stampa BN

@RunWith(Parameterized.class)
public class SmartPrinterTest2StampaBN {
	
	private SmartPrinter stampante;
	private Utente davide;
	
	private int tonerNero;
    private int carta;
    private int credito;
    private boolean expected;
    
    public SmartPrinterTest2StampaBN(int creditoCorrente, int tonerNero, int carta, boolean expectedResult) {
        this.tonerNero = tonerNero;
        this.carta = carta;
        this.credito = creditoCorrente;
        this.expected = expectedResult;
    }
    
    @Parameters
    public static Collection<Object[]> stampaBNParametri() {
        return Arrays.asList(new Object[][] {
        	//Credito, carta,tonerNero,expected
            {45, 100, 500, false},		// credito insufficiente
            {1000, 5, 10, true},		// Caso ok
            {1000, 4, 10, false},		// decide toner
            {1000, 5, 9, false},		// decide carta
        });
    }
	
	@Before
	public void setup() {
	    stampante = new SmartPrinter();
	    davide = new Utente("Davide", 2096, 1, credito);
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
	public void stampaBNTest() {
		boolean result = stampante.stampaBN();
        assertEquals(expected, result);
	}
	

}
