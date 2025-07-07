package esecuzione;

import printer.InterazioneStampante;
import printer.SmartPrinter;
import printer.Utente;

public class Main {

	public static void main(String[] args) {
		
		Utente davide = new Utente("Davide", 2096, 1, 1000);
		Utente matteo = new Utente("Matteo", 4723, 2, 1000);
		
		SmartPrinter stampante = new SmartPrinter();
		
		//Interazione con la stampante
		InterazioneStampante interazione = new InterazioneStampante(stampante);
		
		interazione.aggiungiUtente(davide);
		interazione.aggiungiUtente(matteo);
		
		//Uso la stampante
		interazione.usoStampante();
		

	}

}
