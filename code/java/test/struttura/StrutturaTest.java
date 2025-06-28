package struttura;

import static org.junit.Assert.*;

import org.junit.Test;

import proprietario.Proprietario;
import proprietario.ProprietarioVip;

public class StrutturaTest {

	@Test
	public void testRegistraProprietario() {
		Proprietario p2=new ProprietarioVip("MRYSIDPCHSD546SK","Mirco","Mazzoleni");
		Struttura oasi=Struttura.getInstance();
		oasi.registraProprietario(p2);
		assertFalse(Struttura.getInstance().getListaProprietari().isEmpty());


	}

	@Test
	public void testCancellaProprietario() {
		Proprietario p1=new ProprietarioVip("MRYSIDPCHSD546SK","Davide","Gamba");
		Struttura.getInstance().registraProprietario(p1);
		assertFalse(Struttura.getInstance().getListaProprietari().isEmpty());
		Struttura.getInstance().cancellaProprietario(p1);
		assertTrue(Struttura.getInstance().proprietarioNonRegistrato(p1));

	}

	@Test
	public void testProprietarioNonRegistrato() {
		Proprietario p2=new ProprietarioVip("MRYSIDPCHSD546SK","Mirco","Mazzoleni");
		Struttura.getInstance().registraProprietario(p2);
		assertFalse(Struttura.getInstance().proprietarioNonRegistrato(p2));
		Proprietario p3=new ProprietarioVip("AHJDHJDHJDDKHJKL","Violeta","Parra");
		assertTrue(Struttura.getInstance().proprietarioNonRegistrato(p3));
	}

	@Test
	public void testTrovaStanza() {
		Stanza t1=new Stanza(18);
		Gabbia g1=new Gabbia(DimensioneGabbia.LARGE);
		Gabbia g2=new Gabbia(DimensioneGabbia.SMALL);
		Struttura.getInstance().aggiungiStanza(t1);
		Struttura.getInstance().aggiungiGabbia(g1);
		Struttura.getInstance().listaStanze.get(0).inserisciGabbia(g1);
		assertEquals(1,Struttura.getInstance().trovaStanza(g1));
		assertEquals(0,Struttura.getInstance().trovaStanza(g2));
	}

}
