package struttura;

import static org.junit.Assert.*;

import org.junit.Test;

public class StanzaTest {

	@Test
	public void testStanza() {
		//creo stanza 
		Stanza s=new Stanza(20);
        assertEquals(s.getMetratura(), 20, 0);
	}

	@Test
	public void testCalcolaMetraturaGabbie() {
		//creo struttura 
		Struttura strut=Struttura.getInstance();
		
		//creo stanza 
		Stanza s1=new Stanza(40);
		
		//inserisco la stanza in struttura 
		strut.aggiungiStanza(s1);
		
		//small 1mq medium 2 mq large 4mq
		// 2 medium + 1 large = 2*2+4=8
		Gabbia g1= new Gabbia(DimensioneGabbia.MEDIUM);
		Gabbia g2= new Gabbia(DimensioneGabbia.MEDIUM);
		Gabbia g3= new Gabbia(DimensioneGabbia.LARGE);
		
		//aggiungo le gabbie alla struttura
		strut.aggiungiGabbia(g1);
		strut.aggiungiGabbia(g2);
		strut.aggiungiGabbia(g3);
		
		//associo le gabbie alla stanza
		s1.inserisciGabbia(g1);
		s1.inserisciGabbia(g2);
		s1.inserisciGabbia(g3);
		
	   assertEquals(s1.calcolaMetraturaGabbie(), 8, 0);
	}

	@Test
	public void testStanzaVuota() {
	
		//creo struttura 
				Struttura strut=Struttura.getInstance();
				
				//creo stanza 
				Stanza s3=new Stanza(40);
				Stanza s4=new Stanza(30);
				
				//inserisco la stanza in struttura 
				strut.aggiungiStanza(s3);
		        strut.aggiungiStanza(s4);
		        
		        //test s3 vuota 
				assertTrue(s3.stanzaVuota());
				
				//creo gabbia g1
				Gabbia g4=new Gabbia(DimensioneGabbia.LARGE);
				//assegno gabbia a stanza e struttura
				strut.aggiungiGabbia(g4);
				s4.inserisciGabbia(g4);
				
				//test s4 non vuota
		        assertFalse(s4.stanzaVuota());
	}

}
