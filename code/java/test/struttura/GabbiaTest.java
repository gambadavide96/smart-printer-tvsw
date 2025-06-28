package struttura;

import static org.junit.Assert.*;

import org.junit.Test;

public class GabbiaTest {

	@Test
	public void testGabbia() {
		//creo  gabbia g1 large
     Gabbia g1=new Gabbia(DimensioneGabbia.LARGE);
     
     //testo che grandezza di g1 sia large
     assertEquals(g1.grandezza, DimensioneGabbia.LARGE);

	}

	@Test
	public void testGetStatoGabbia() {
		
        // creo struttura 
		Struttura strut=Struttura.getInstance();
		
		//creo  gabbia g2 medium
	     Gabbia g2=new Gabbia(DimensioneGabbia.MEDIUM);
	     
	     //creo stanza 
	     Stanza s1=new Stanza(30);
	     
	     // inserisco la stanza s1  e la gabbia g2 in struttura
	     strut.aggiungiStanza(s1);
	     strut.aggiungiGabbia(g2);
	      
	     //inserisco la gabbia g2 nella stanza 
	     s1.inserisciGabbia(g2);
	     
	     
		assertEquals(g2.getStatoGabbia(), StatoGabbia.ASSEGNATA);
	}

	@Test
	public void testCambiaStatoInAssegnato() {
	
		// creo struttura 
				Struttura strut2=Struttura.getInstance();
				
				//creo  gabbia g5 large
			     Gabbia g5=new Gabbia(DimensioneGabbia.LARGE);
			     
			     //creo stanza 
			     Stanza s3=new Stanza(30);
			     
			     // inserisco la stanza s3 e la gabbia g5 in struttura
			     strut2.aggiungiStanza(s3);
			     strut2.aggiungiGabbia(g5);
		//controllo che stato sia non assegnato
		 assertEquals(g5.stato, StatoGabbia.NONASSEGNATA);
		 //cambio stato in assegnato
		 g5.cambiaStatoInAssegnato();
		 assertEquals(g5.stato, StatoGabbia.ASSEGNATA); 
	}

	@Test
	public void testCambiaStatoInNonAssegnato() {
		
		// creo struttura 
		Struttura strut44=Struttura.getInstance();
		
		//creo  gabbia g45 large
	     Gabbia g45=new Gabbia(DimensioneGabbia.LARGE);
	     
	     //creo stanza 
	     Stanza s43=new Stanza(30);
	     
	     // inserisco la stanza s43 e la gabbia g45 in struttura
	     strut44.aggiungiStanza(s43);
	     strut44.aggiungiGabbia(g45);
	     
	     //assegno la gabbia alla stanza
	     s43.inserisciGabbia(g45);
	     
         //controllo che stato sia  assegnato
         assertEquals(g45.stato, StatoGabbia.ASSEGNATA);
         
          //cambio stato in non assegnato
          g45.cambiaStatoInNonAssegnato();
          
         assertEquals(g45.stato, StatoGabbia.NONASSEGNATA); 
		
		
	}

}
