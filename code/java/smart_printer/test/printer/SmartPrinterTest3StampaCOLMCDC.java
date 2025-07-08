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
    private Stato expectedState;
    
    public SmartPrinterTest3StampaCOLMCDC(int creditoCorrente, int tonerNero, 
    		int tonerColore,int carta, boolean expectedResult,Stato expectedState) 
    {
        this.tonerNero = tonerNero;
        this.tonerColore = tonerColore;
        this.carta = carta;
        this.credito = creditoCorrente;
        this.expectedResult = expectedResult;
        this.expectedState = expectedState;
    }
    
    @Parameters
    public static Collection<Object[]> stampaCOLParametri() {
        return Arrays.asList(new Object[][] {
        	//Credito,tonerNero,TonerColore,carta,expected
            {45, 100, 100, 500, false,Stato.PRONTA},	// credito insufficiente
            {1000, 5,5, 10, true,Stato.INUSO},			// Caso ok
            {1000, 4,5, 10, false,Stato.PRONTA},		// decide tonerNero
            {1000, 5,4, 10, false,Stato.PRONTA},		// decide tonerColore
            {1000, 5,5, 9, false,Stato.PRONTA}			// decide Carta
            
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
		boolean result = stampante.stampaCOL();
        assertEquals(expectedResult, result);
        assertEquals(expectedState,stampante.getPrinterState());
	}
	

}
