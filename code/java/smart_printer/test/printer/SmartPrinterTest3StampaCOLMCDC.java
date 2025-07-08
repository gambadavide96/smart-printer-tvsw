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

//Test Suite parametrica MCDC per la stampa COL

@RunWith(Parameterized.class)
public class SmartPrinterTest3StampaCOLMCDC {
	
	private SmartPrinter stampante;
	private Utente davide;
	
	private int tonerNero;
	private int tonerColore;
    private int carta;
    private int credito;
    private boolean expectedResult;
    
    public SmartPrinterTest3StampaCOLMCDC(int creditoCorrente, int tonerNero, 
    		int tonerColore,int carta, boolean expectedResult) 
    {
        this.tonerNero = tonerNero;
        this.tonerColore = tonerColore;
        this.carta = carta;
        this.credito = creditoCorrente;
        this.expectedResult = expectedResult;
    }
    
    @Parameters
    public static Collection<Object[]> stampaCOLParametri() {
        return Arrays.asList(new Object[][] {
        	//Credito,tonerNero,TonerColore,carta,expected
            {45, 100, 100, 500, false},		// credito insufficiente
            {1000, 5,5, 10, true},			// Caso ok
            {1000, 4,5, 10, false},			// decide tonerNero
            {1000, 5,4, 10, false},			// decide tonerColore
            {1000, 5,5, 9, false}			// decide Carta
            
        });
    }
    
	@Before
	public void setup() {
	    stampante = new SmartPrinter();
	    davide = new Utente("Davide", 2096, 1, credito);
	    stampante.aggiungiUtente(davide);
	    
	    assertFalse(stampante.stampaCOL()); //Operazione non consentita
	    
	    stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
		//Imposto risorse per il test parametrico
		stampante.setTonerNero(tonerNero);
		stampante.setTonerColore(tonerColore);
        stampante.setFogliCarta(carta);
		
	}
	
	@Test
	public void stampaCOLTest() {
		//TODO
		boolean result = stampante.stampaCOL();
        assertEquals(expectedResult, result);
	}
	

}
