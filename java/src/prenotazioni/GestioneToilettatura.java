package prenotazioni;

import java.util.ArrayList;

public class GestioneToilettatura {

	private static ArrayList<PrenotazioneToilettatura> elencoToilettature=new ArrayList<PrenotazioneToilettatura>();

	public static boolean toilettaturaValida(PrenotazioneToilettatura tCandidata) {
		
		//Controllo che la data inserita nella prenotazione sia disponibile
		
		for(PrenotazioneToilettatura tPrenotata: elencoToilettature) {
			if(tCandidata.getInizioAppuntamento().isBefore(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isAfter(tPrenotata.getInizioAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isBefore(tPrenotata.getFineAppuntamento()) && tCandidata.getFineAppuntamento().isAfter(tPrenotata.getFineAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isAfter(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isBefore(tPrenotata.getFineAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isEqual(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isEqual(tPrenotata.getFineAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isEqual(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isBefore(tPrenotata.getFineAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isAfter(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isEqual(tPrenotata.getFineAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isEqual(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isAfter(tPrenotata.getFineAppuntamento()))
				return false;
			if(tCandidata.getInizioAppuntamento().isBefore(tPrenotata.getInizioAppuntamento()) && tCandidata.getFineAppuntamento().isEqual(tPrenotata.getFineAppuntamento()))
				return false;
		}
		return true;
	}

	public static void addToilettatura (PrenotazioneToilettatura t) {
		if(toilettaturaValida(t)) {
			t.calcolaAddebito();
			elencoToilettature.add(t);
		}
		else
			System.out.println("La prenotazione non è andata a buon fine,si prega di scegliere un'altra data");
	}

	public static void removeToilettatura (PrenotazioneToilettatura tCandidata) {
		for(PrenotazioneToilettatura tPrenotata: elencoToilettature ) 
			if(tCandidata.equals(tPrenotata)) {
				elencoToilettature.remove(tPrenotata);
				return;
			}
		System.out.println("Toilettatura non può essere cancellata perchè non è presente nell'elenco");

	}

	public static void stampaElencoToilettature() {
		for(PrenotazioneToilettatura tPrenotata: elencoToilettature ) 
			System.out.println(tPrenotata.getInfoPrenotazioneToilettatura());
	}
	
	public static ArrayList<PrenotazioneToilettatura>  getElencoToilettature() {
		return elencoToilettature;
	}

}

