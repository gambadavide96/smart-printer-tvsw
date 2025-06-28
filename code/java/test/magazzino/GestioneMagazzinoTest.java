package magazzino;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import animale.Pet;
import animale.Taglia;
import animale.TipoAnimale;
import prenotazioni.GestionePensione;
import prenotazioni.PrenotazioneNonValidaException;
import prenotazioni.PrenotazionePensione;
import proprietario.ProprietarioStandard;
import struttura.DimensioneGabbia;
import struttura.Gabbia;
import struttura.Stanza;
import struttura.Struttura;

public class GestioneMagazzinoTest {

	@Test
	public void testAggiungiScorteCaneSecco() {
	// scorte cane secco 800	
	GestioneMagazzino.aggiungiScorteCaneSecco(200);
	assertEquals(1000, GestioneMagazzino.sCaneSecco.getQuantita(), 0);
	
	}

	@Test
	public void testAggiungiScorteCaneUmido() {
		//scorte cane umido 600
	    GestioneMagazzino.aggiungiScorteCaneUmido(200);
		assertEquals(800,GestioneMagazzino.sCaneUmido.getQuantita(),0);
	}

	@Test
	public void testSottraiScorteCaneSecco() {
		//scorte cane secco 1000
		GestioneMagazzino.sottraiScorteCaneSecco(200);
		assertEquals(800,GestioneMagazzino.sCaneSecco.getQuantita(),0);
		
	}

	@Test
	public void testSottraiScorteCaneUmido() {
		// scorte cane umido 1000
		GestioneMagazzino.sottraiScorteCaneUmido(400);
		assertEquals(600,GestioneMagazzino.sCaneUmido.getQuantita(),0);
	}

	@Test
	public void testScorteSufficentiMeseCorrente() {
		
		//creo stanze e gabbie e le inserisco in struttura 
		Stanza s1=new Stanza(30);
		Stanza s2=new Stanza(40);
		Struttura.getInstance().aggiungiStanza(s1);
		Struttura.getInstance().aggiungiStanza(s2);
		
		Gabbia g1=new Gabbia(DimensioneGabbia.SMALL);
		Gabbia g2=new Gabbia(DimensioneGabbia.MEDIUM);
		Gabbia g3=new Gabbia(DimensioneGabbia.LARGE);
		Struttura.getInstance().aggiungiGabbia(g1);
		Struttura.getInstance().aggiungiGabbia(g2);
		Struttura.getInstance().aggiungiGabbia(g3);
		
		// assegno le gabbie alle stanze
		s1.inserisciGabbia(g1);
		s2.inserisciGabbia(g2);
		s2.inserisciGabbia(g3);
		
		//creo un propietario e i suoi animali
		ProprietarioStandard p=new ProprietarioStandard("1234567891012345","mirco" ,"mazzoleni");
		Pet a=new Pet("titti", TipoAnimale.PENNUTO);
		Pet a1=new Pet("fido",Taglia.GRANDE,TipoAnimale.CANE);
		Pet a2=new Pet("hamtaro",TipoAnimale.CRICETO);
		
		//associo gli animali al propietario
		p.inserisciPet(a1);
		p.inserisciPet(a2);
		p.inserisciPet(a);
		
		//registro propietario in struttura 
		Struttura.getInstance().registraProprietario(p);
		
	    //creo prenotazioni per i vari pet di propietario mirco mazzoleni
		try {
		    PrenotazionePensione pren = new PrenotazionePensione(p, a, LocalDate.of(2022,01,22),LocalDate.of(2022,01,29));
			PrenotazionePensione pren1=new PrenotazionePensione(p, a1, LocalDate.of(2022,01,22),LocalDate.of(2022,01,29));
			PrenotazionePensione pren2=new PrenotazionePensione(p, a2, LocalDate.of(2022,01,22),LocalDate.of(2022,01,29));
			
			//inserisco la prenotazione per la pensione
			GestionePensione.addPrenotazione(pren);
			GestionePensione.addPrenotazione(pren1);
			GestionePensione.addPrenotazione(pren2);	
			
		} catch (PrenotazioneNonValidaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//aggiungo scorte al magazzino
		GestioneMagazzino.aggiungiScorteCaneSecco(2000);
		GestioneMagazzino.aggiungiScorteCaneUmido(1000);
		GestioneMagazzino.aggiungiScorteCriceto(400);
		GestioneMagazzino.aggiungiScortePennuto(400);
		
		
		assertTrue(GestioneMagazzino.scorteSufficentiMese(LocalDate.of(2022, 1, 1)));
		
		//scorte tot rimaste cane 900 scorte tot rimaste pennuto 50 scorte tot rimaste criceto 50
		
	}
	
	@Test
	public void testScorteSufficentiMeseCorrenteFalse() {
		
     GestioneMagazzino.sottraiScorteCaneSecco(1000);
	assertFalse(GestioneMagazzino.scorteSufficentiMese(LocalDate.of(2022, 1, 1)));
	
	}

}
