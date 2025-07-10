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

@RunWith(Parameterized.class)
public class SmartPrinterTest4ScansioneMCDC {
	
	private SmartPrinter stampante;
	private Utente davide;
	
	private boolean collegatoWireless;
	private boolean collegatoCavo;
	private boolean expectedResult;
    private Stato expectedState;
    
    public SmartPrinterTest4ScansioneMCDC(boolean wireless,boolean cavo,
    		boolean expectedResult, Stato expectedState) {
    	
    	this.collegatoWireless = wireless;
    	this.collegatoCavo = cavo;
    	this.expectedResult = expectedResult;
    	this.expectedState = expectedState;
    	
    }
    
    @Parameters
    public static Collection<Object[]> scansioneParametri() {
        return Arrays.asList(new Object[][] {
        	//wireless,cavo,expectedResult,expectedState
            {false, false,false,Stato.PRONTA},	// entrambi C e W a false
            {false, true,true,Stato.INUSO},		// C vero
            {true, false,true,Stato.INUSO}		// W vero
            
        });
    }
    
    @Before
	public void setup() {
	    stampante = new SmartPrinter();
	    davide = new Utente("Davide", 2096, 1, 1000);
	    stampante.aggiungiUtente(davide);
	    
	    assertFalse(stampante.scansione()); //Operazione non consentita
	    
	    stampante.accendiStampante();
		stampante.avvioStampante();
		stampante.identificazioneUtente(2096);
		stampante.inserimentoPin(1);
		assertEquals(Stato.PRONTA,stampante.getPrinterState());
		
		//Imposto risorse per il test parametrico
		stampante.setCollegatoWireless(collegatoWireless);
		stampante.setCollegatoCavo(collegatoCavo);
		
	}

	@Test
	public void testScansione() {
		boolean result = stampante.scansione();
        assertEquals(expectedResult, result);
        assertEquals(expectedState,stampante.getPrinterState());
	}

}
