package prenotazioni;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import animale.Pet;
import animale.Taglia;
import animale.TipoAnimale;
import proprietario.Proprietario;
import proprietario.ProprietarioStandard;
import proprietario.ProprietarioVip;
import struttura.Struttura;

public class GestioneToilettaturaTest {

	@Test
	public void testAddToilettatura() {
		Struttura oasi=Struttura.getInstance();
		//Creo proprietari
		Proprietario p1=new ProprietarioStandard("PRGCTEGAG15465AF","Davide","Gamba");
		Proprietario p2=new ProprietarioVip("MRYSIDPCHSD546SK","Mirco","Mazzoleni");
		Proprietario p3=new ProprietarioVip("AHJDHJDHJDDKHJKL","Violeta","Parra");
		oasi.registraProprietario(p1);
		oasi.registraProprietario(p2);
		oasi.registraProprietario(p3);
		Pet pet1=new Pet("Trilli",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet2=new Pet("Amore",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet3= new Pet("Fido",Taglia.MEDIO,TipoAnimale.CANE);
		Pet pet4=new Pet("Pluto",Taglia.GRANDE,TipoAnimale.CANE);
		Pet pet5=new Pet("Titti",Taglia.PICCOLO,TipoAnimale.PENNUTO);
		Pet pet6=new Pet("Splint",Taglia.PICCOLO,TipoAnimale.CRICETO);
		Pet pet7= new Pet("Silvestro",Taglia.MEDIO,TipoAnimale.CANE);
		Pet pet8= new Pet("Bull",Taglia.GRANDE,TipoAnimale.CANE);
		Pet pet9=new Pet("Sissi",Taglia.PICCOLO,TipoAnimale.GATTO);
		Pet pet10=new Pet("Franz",Taglia.MEDIO,TipoAnimale.CANE);
		Pet pet11=new Pet("Scooby",Taglia.MEDIO,TipoAnimale.CANE);
		// Associo pet a padrone
		p1.inserisciPet(pet1);
		p2.inserisciPet(pet2);
		p2.inserisciPet(pet3);
		p3.inserisciPet(pet4);
		p2.inserisciPet(pet5);
		p1.inserisciPet(pet6);
		p3.inserisciPet(pet7);
		p3.inserisciPet(pet8);
		p1.inserisciPet(pet9);
		p1.inserisciPet(pet10);
		p2.inserisciPet(pet11);
		

		// GESTIONE TOILETTATURA
		System.out.println("PARTE DEL MAIN DI PROVA PER LA GESTIONE TOILETTATURA");
		// Creazione prenotazioni e verifica sul tipo animale, solo gatti o cani
		try {
			// Creazione prenotazioni e verifica sul tipo animale, solo gatti o cani,Proprietario registrato con Pet
			PrenotazioneToilettatura toile1=new PrenotazioneToilettatura(p1,pet1,LocalDateTime.of(2022, 1, 4, 13, 0));
			PrenotazioneToilettatura toile2=new PrenotazioneToilettatura(p2,pet2,LocalDateTime.of(2022, 1, 4, 13, 10));
			PrenotazioneToilettatura toile3=new PrenotazioneToilettatura(p2,pet3,LocalDateTime.of(2022, 1, 4, 15, 25));
			PrenotazioneToilettatura toile4=new PrenotazioneToilettatura(p3,pet4,LocalDateTime.of(2022, 1, 4, 15, 10));
			PrenotazioneToilettatura toile6=new PrenotazioneToilettatura(p3,pet7,LocalDateTime.of(2022, 1, 4, 16, 10));
			PrenotazioneToilettatura toile7=new PrenotazioneToilettatura(p3,pet8,LocalDateTime.of(2022, 1, 4, 13, 05));
			PrenotazioneToilettatura toile8=new PrenotazioneToilettatura(p1,pet9,LocalDateTime.of(2022, 1, 4, 16, 10));
			PrenotazioneToilettatura toile9=new PrenotazioneToilettatura(p1,pet10,LocalDateTime.of(2022, 1, 4, 12, 50));
			PrenotazioneToilettatura toile10=new PrenotazioneToilettatura(p2,pet11,LocalDateTime.of(2022, 1, 4, 11, 50));
			
			// Inserimento a sistema delle toilettature senza conflitti e controllo corretti addebiti di costi
			GestioneToilettatura.addToilettatura(toile1);
			assertEquals(GestioneToilettatura.getElencoToilettature().get(0),toile1);
			GestioneToilettatura.addToilettatura(toile2);
			assertEquals(GestioneToilettatura.getElencoToilettature().get(1),toile2);
			GestioneToilettatura.addToilettatura(toile4);
			assertEquals(GestioneToilettatura.getElencoToilettature().get(2),toile4);
			GestioneToilettatura.addToilettatura(toile6);
			assertEquals(GestioneToilettatura.getElencoToilettature().get(3),toile6);
			GestioneToilettatura.addToilettatura(toile10);
			assertEquals(GestioneToilettatura.getElencoToilettature().get(4),toile10);
			
			// Inserimento a sistema delle toilettature con conflitti temporali
			GestioneToilettatura.addToilettatura(toile3);
			assertFalse(GestioneToilettatura.getElencoToilettature().contains(toile3));
			GestioneToilettatura.addToilettatura(toile7);	
			assertFalse(GestioneToilettatura.getElencoToilettature().contains(toile7));
			GestioneToilettatura.addToilettatura(toile8);
			assertFalse(GestioneToilettatura.getElencoToilettature().contains(toile8));
			GestioneToilettatura.addToilettatura(toile9);	
			assertFalse(GestioneToilettatura.getElencoToilettature().contains(toile9));
			
			GestioneToilettatura.stampaElencoToilettature();
		
		}
		catch(ToilettaturaNonValidaException t) {
			System.out.println(t.getMessage());
		}
	}

}
