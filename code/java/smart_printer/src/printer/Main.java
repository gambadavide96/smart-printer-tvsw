package printer;

public class Main {

	public static void main(String[] args) {
		
		//Creo due utenti
		Utente davide = new Utente("Davide", 2096, 1, 1000);
		Utente matteo = new Utente("Matteo", 4723, 2, 1000);
		
		//Creo stampante e inserisco i due utenti
		SmartPrinter stampante = new SmartPrinter();
		stampante.aggiungiUtente(davide);
		stampante.aggiungiUtente(matteo);
		
		//Stato corrente della stampante
		stampante.statoCorrente();
		
		
		

	}

}
