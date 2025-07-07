package printer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InterazioneStampante {
	
	private SmartPrinter stampante;
	
	public InterazioneStampante(SmartPrinter s) {
		stampante = s;
	}
	
	private static boolean controlloInputUtenteBool(String s) {
	    return s.equals("si") || s.equals("no")  ? true : false;
	}
	
	public boolean aggiungiUtente(Utente u) {
		return stampante.aggiungiUtente(u);
	}
	    
	
	public void statoCorrenteStampante() {
       stampante.statoCorrente();
    }
	
	// Interazione con la stampante
    public void usoStampante() {
        Scanner scanner = new Scanner(System.in);
        boolean closeProgram = false;
        boolean validInput = false;
        
        while(!closeProgram) {
        	
        	switch(stampante.getPrinterState()) {
	        	case SPENTA:
	        		String rispostaAccensione;
	        		do {
		        		System.out.println("Vuoi accendere la stampante? (si/no): ");
		        		rispostaAccensione = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaAccensione);
	        		}
	        		while(!validInput);
	        			
	        		if(rispostaAccensione.equals("si"))
	        			stampante.accendiStampante();
	        		else
	        			closeProgram = true;
	        		
	        		break;
	        		
				case AVVIO:
	        		String rispostaGuasto;
	        		do {
	        			System.out.println("Si è verificato un guasto? (si/no): ");
	        			rispostaGuasto = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaGuasto);
	        		}
	        		while(!validInput);
	        		
	        		if(rispostaGuasto.equals("si"))
	        			stampante.guastoStampante();
	        		
	        		stampante.avvioStampante();
	        		break;
	        		
				case OUTOFSERVICE:
					String rispostaGuastoRiparato;
	        		do {
		        		System.out.println("Il guasto è stato riparato? (si/no): ");
		        		rispostaGuastoRiparato = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaGuastoRiparato);
	        		}
	        		while(!validInput);
	        		
	        		if(rispostaGuastoRiparato.equals("si"))
	        			stampante.riparazioneStampante();
					
					stampante.gestioneGuasto();
	        		break;
	        		
				case MOSTRABADGE:
					int badgeMostrato = 0;
					validInput = false;
					
					while(!validInput) {
						System.out.println("Inserire un numero di Badge: ");
						  try {
						        badgeMostrato = scanner.nextInt();
						        validInput = true;
						    } catch (InputMismatchException e) {
						        System.out.println("Input non valido. Inserisci un numero di Badge.");
						        scanner.nextLine(); 
						    }
					}
					
					scanner.nextLine(); // consuma il newline residuo dopo input corretto
					stampante.identificazioneUtente(badgeMostrato);
	        		break;
	        		
				case INSERISCIPIN:
					//inserimentoPin();
					stampante.statoCorrente();
					System.out.println("Cjeckpoint inserisci pin ");
	        		String check3 = scanner.nextLine().toLowerCase();
	        		
					break;
				case PRONTA:
					stampante.sceltaServizio();
					break;
				case INUSO:
					stampante.stampanteInUso();
					break;
				case ERRORE:
					stampante.gestioneErrore();
					break;
        	}
        	
        }
        
        scanner.close();
        
    }

}
