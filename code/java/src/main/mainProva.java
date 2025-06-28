package main;
 
import struttura.*;

import proprietario.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import animale.*;
import magazzino.GestioneMagazzino;
import prenotazioni.*;
import prenotazioni.PrenotazioneNonValidaException;

public class mainProva {

	public static void main(String[] args) {
		/* Struttura Oasi */
		
		//Creo Struttura
		Struttura oasi=Struttura.getInstance();
		
		//inizializzo la struttura,creazione di stanze e gabbie (Facade)
		oasi.inizializzaStruttura();
	    System.out.println("Informazioni sulla struttura:");
		oasi.getInfoStruttura();
	    
	//Creo proprietari
		Proprietario p1=new ProprietarioStandard("PRGCTEGAG15465AF","Davide","Gamba");
		Proprietario p2=new ProprietarioVip("MRYSIDPCHSD546SK","Mirco","Mazzoleni");
		Proprietario p3=new ProprietarioVip("AHJDHJDHJDDKHJKL","Violeta","Parra");
		
		//Registro i proprietari all'interno della struttura
		oasi.registraProprietario(p1);
		oasi.registraProprietario(p2);
		oasi.registraProprietario(p3);
		oasi.stampaProprietariRegistrati();
		System.out.println();	

	
		//Creo pet
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
		// stampo info proprietario 1 con pet
		p1.getInfoProprietario();
		p1.getInfoPets();
		System.out.println();
		
		// stampo info proprietario 2 con pet
		p2.getInfoProprietario();
		p2.getInfoPets();
		System.out.println();
		
		// stampo info proprietario 3 con pet
		p3.getInfoProprietario();
		p3.getInfoPets();
		System.out.println();
		
		// rimuovo pet non appartenente a proprietario (Pet non può essere cancellato perchè non appartiene a quel padrone)
		p3.cancellaPet(pet2);
		System.out.println();
		
	
		//GESTIONE PRENOTAZIONI
		try {
		//Pet taglia small in gabbia 2
		PrenotazionePensione preno1=new PrenotazionePensione(p1,pet1,LocalDate.of(2022,1, 28),LocalDate.of(2022,2, 7));
		GestionePensione.addPrenotazione(preno1);
		//Secondo Pet taglia small stessa gabbia ma data diversa e quindi prenotazione possibile in gabbia 2
		PrenotazionePensione preno2=new PrenotazionePensione(p2,pet2,LocalDate.of(2022,1, 5),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno2);
		//Pet taglia medium in gabbia 5
		PrenotazionePensione preno3=new PrenotazionePensione(p2,pet3,LocalDate.of(2022,1, 5),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno3);
		//Pet taglia large in gabbia 1
		PrenotazionePensione preno4=new PrenotazionePensione(p3,pet4,LocalDate.of(2022,1, 5),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno4);
		// Pet Taglia Medium Gabbia 9
		PrenotazionePensione preno5=new PrenotazionePensione(p3,pet7,LocalDate.of(2022,1, 4),LocalDate.of(2022,1, 10));
		GestionePensione.addPrenotazione(preno5);
		// stessa taglia di pet3 ma data diversa quindi prenotazione possibile in gabbia 5
		PrenotazionePensione preno6=new PrenotazionePensione(p3,pet7,LocalDate.of(2022,2, 7),LocalDate.of(2022,2, 15));
		GestionePensione.addPrenotazione(preno6);
		// Assegno una cuccia piccola con la stessa data di quando la cuccia 2 è occupata, così occupo la cuccia 7
		PrenotazionePensione preno7=new PrenotazionePensione(p2,pet5,LocalDate.of(2022,1, 28),LocalDate.of(2022,2,7));
		GestionePensione.addPrenotazione(preno7);
		// Assegno una cuccia piccola quando la 2 e la 7 sono occupate cosi cambio stanza e vado nella cuccia 6
		PrenotazionePensione preno8=new PrenotazionePensione(p1,pet9,LocalDate.of(2022,1, 28),LocalDate.of(2022,2,7));
		GestionePensione.addPrenotazione(preno8);
		// stessa data e taglia di pet7 quindi prenotazione in stanza 3 gabbia 9 
		PrenotazionePensione preno9=new PrenotazionePensione(p1,pet10,LocalDate.of(2022,2, 6),LocalDate.of(2022,2, 9));
		GestionePensione.addPrenotazione(preno9);
		// stessa data e taglia di pet7 quindi prenotazione non possibile per mancanza di gabbie 
		PrenotazionePensione preno10=new PrenotazionePensione(p2,pet11,LocalDate.of(2022,2, 4),LocalDate.of(2022,2, 8));
		GestionePensione.addPrenotazione(preno10);
		// stessa data e taglia di pet7 quindi prenotazione non possibile per mancanza di gabbie 
		PrenotazionePensione preno11=new PrenotazionePensione(p2,pet11,LocalDate.of(2022,2, 6),LocalDate.of(2022,2, 12));
		GestionePensione.addPrenotazione(preno11);
		
		GestionePensione.stampaElencoPrenotazioni();
		
		}
		catch(PrenotazioneNonValidaException e){
			System.out.println("Prenotazione non Valida:");
		}
		
		System.out.println();
		p2.getInfoProprietario();
		
		System.out.println();
		p1.getInfoProprietario();
		
		System.out.println();
		p3.getInfoProprietario();
		
	
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
			GestioneToilettatura.addToilettatura(toile2);
			GestioneToilettatura.addToilettatura(toile4);
			GestioneToilettatura.addToilettatura(toile6);
			GestioneToilettatura.addToilettatura(toile10);
			
			// Inserimento a sistema delle toilettature con conflitti temporali
			GestioneToilettatura.addToilettatura(toile3);	
			GestioneToilettatura.addToilettatura(toile7);	
			GestioneToilettatura.addToilettatura(toile8);
			GestioneToilettatura.addToilettatura(toile9);	
			
			//Controllo applicazione dello sconto,per le prove lo sconto è ora impostato  a 2 PrenotazioniPensione
			GestioneToilettatura.stampaElencoToilettature();
			 //Mirko non paga la prima toilettatura 
			System.out.println("Punti fedelta Mirko dopo aver utilizzato lo sconto: "+p2.getPuntiFedelta());		// I punti fedelta di Mirko sono stati scalati
			System.out.println();
		}
		catch(ToilettaturaNonValidaException t) {
			System.out.println(t.getMessage());
		}

		//PULIZIA GABBIE OCCUPATE
		//Inserita una data in ingresso mi ritorna le gabbie da pulire con una prenotazione in corso - prende anche gli estremi di prenotazione
		oasi.gabbieDaPulire(LocalDate.of(2022,1,28)); // Stampa le gabbia g2,g6,g7
		System.out.println();
		
		oasi.gabbieDaPulire(LocalDate.of(2022,2,7)); // Stampa le gabbia g2,g6,g7,g5,g9
		System.out.println();
		
		oasi.gabbieDaPulire(LocalDate.of(2022,12,7)); // Nessuna gabbia da pulire
		System.out.println();

		//MAGAZZINO
		//Inserisco alimenti nel magazzino
		GestioneMagazzino.aggiungiScorteCaneSecco(2700);
		GestioneMagazzino.aggiungiScorteCaneUmido(0);
		GestioneMagazzino.aggiungiScorteGattoSecco(150);
		GestioneMagazzino.aggiungiScorteGattoUmido(50);
		GestioneMagazzino.aggiungiScortePennuto(1000);
		GestioneMagazzino.aggiungiScorteCriceto(1000);

		//Controllo che le scorte di questo mese siano sufficienti sulla base delle prenotazioni di Gennaio
		System.out.println("Le scorte per il mese di gennaio sono sufficenti: "+ GestioneMagazzino.scorteSufficentiMese(LocalDate.of(2022, 1, 1)));
		System.out.println();
		
		//CANCELLAZIONE PROPRIETARIO
		//Cancello proprietario dall'elenco dei proprietari registrati -> cancella anche le sue prenotazioni dalla Pensione
		oasi.cancellaProprietario(p2);
		GestionePensione.stampaElencoPrenotazioni();
		System.out.println();
		
	}


}

