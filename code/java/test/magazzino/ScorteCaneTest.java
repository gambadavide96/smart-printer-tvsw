package magazzino;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScorteCaneTest {

	@Test
	public void testScorteCane() {
		ScorteCane s=new ScorteCane(200,TipoAlimento.SECCO);
		ScorteCane u= new ScorteCane(300, TipoAlimento.UMIDO);
		assertEquals(s.getQuantita(), 200, 0);
		assertEquals(u.getQuantita(), 300, 0);
	}

	@Test
	public void testRimuoviQuantita() {
		
		//creo scorte secco e umido
		ScorteCane s1=new ScorteCane(2000,TipoAlimento.SECCO);
		ScorteCane u2= new ScorteCane(3000, TipoAlimento.UMIDO);
		
		//tolgo 100 da secco e 1000 da umido
		s1.rimuoviQuantita(100);
		u2.rimuoviQuantita(1000);
		
		//controllo che sia rimasto 1900 di secco e 2000 di umido
		assertEquals(1900,s1.getQuantita(), 0);
		assertEquals(2000,u2.getQuantita(), 0);
	}

	@Test
	public void testAggiungiQuantita() {
		
		//creo scorte secco e umido
		ScorteCane s11=new ScorteCane(2000,TipoAlimento.SECCO);
		ScorteCane u12= new ScorteCane(3000, TipoAlimento.UMIDO);
				
		//aggiungo 100 a secco e 1000 a umido
		s11.aggiungiQuantita(100);
		u12.aggiungiQuantita(1000);
				
		//controllo che siano diventate 2100 di secco e 4000 di umido
	    assertEquals(2100,s11.getQuantita(), 0);
	    assertEquals(4000,u12.getQuantita(), 0);
		
	}

}
