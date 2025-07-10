package printer;

import java.util.InputMismatchException;
import java.util.Scanner;
import printer.SmartPrinter.Servizio;

public class InterazioneStampante {
	
	private SmartPrinter stampante;
	
	public InterazioneStampante(SmartPrinter s) {
		stampante = s;
	}
	
	private static boolean controlloInputUtenteBool(String s) {
	    return s.equals("si") || s.equals("no");
	}
	
	private static boolean controlloInputUtenteSceltaServizio(String s) {
		return s.equals("BN") || s.equals("COL") || s.equals("S") ||
				s.equals("E");
	}
	
	private boolean controlloInputUtenteSceltaDevice(String s) {
		return s.equals("W") || s.equals("C") || s.equals("D") ||
				s.equals("N");
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
	        		stampante.statoCorrente();
	        		
	        		String rispostaAccensione;
	        		do {
		        		Log.print("Vuoi accendere la stampante? (si/no): ");
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
					stampante.statoCorrente();
					
	        		String rispostaGuasto;
	        		do {
	        			Log.print("Si è verificato un guasto? (si/no): ");
	        			rispostaGuasto = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaGuasto);
	        		}
	        		while(!validInput);
	        		
	        		if(rispostaGuasto.equals("si"))
	        			stampante.guastoStampante();
	        		
	        		stampante.avvioStampante();
	        		break;
	        		
				case OUTOFSERVICE:
					stampante.statoCorrente();
					
					String rispostaGuastoRiparato;
	        		do {
		        		Log.print("Il guasto è stato riparato? (si/no): ");
		        		rispostaGuastoRiparato = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaGuastoRiparato);
	        		}
	        		while(!validInput);
	        		
	        		if(rispostaGuastoRiparato.equals("si"))
	        			stampante.riparazioneStampante();
					
					stampante.gestioneGuasto();
	        		break;
	        		
				case MOSTRABADGE:
					stampante.statoCorrente();
					
					int badgeMostrato = 0;
					validInput = false;
					
					while(!validInput) {
						Log.print("Inserire un numero di Badge: ");
						  try {
						        badgeMostrato = scanner.nextInt();
						        validInput = true;
						    } catch (InputMismatchException e) {
						        Log.print("Input non valido. Inserisci un numero di Badge.");
						        scanner.nextLine(); 
						    }
					}
					
					scanner.nextLine(); // consuma il newline residuo dopo input corretto
					stampante.identificazioneUtente(badgeMostrato);
	        		break;
	        		
				case INSERISCIPIN:
					stampante.statoCorrente();
					
					int pinInserito = 0;
					validInput = false;
					
					while(!validInput) {
						Log.print("Inserisci il tuo Pin: ");
						  try {
						        pinInserito = scanner.nextInt();
						        validInput = true;
						    } catch (InputMismatchException e) {
						        Log.print("Input non valido. Inserisci un un numero .");
						        scanner.nextLine(); 
						    }
					}
					
					scanner.nextLine(); // consuma il newline residuo dopo input corretto
					stampante.inserimentoPin(pinInserito);
					break;
					
				case PRONTA:
					stampante.statoCorrente();
					
					String servizioScelto;
	        		do {
	        			Log.print("Quale operazione vuoi effettuare? (BN/COL/S/E): ");
	        			servizioScelto = scanner.nextLine().toUpperCase();
		        		validInput = controlloInputUtenteSceltaServizio(servizioScelto);
	        		}
	        		while(!validInput);
	        		
	        		
	        		String deviceDaCollegare;
	        		if(servizioScelto.equals("S")) {
	        			do {
		        			Log.print("Collega i device dove ricevere la scansione: (W/C/D/N): ");
		        			deviceDaCollegare = scanner.nextLine().toUpperCase();
			        		validInput = controlloInputUtenteSceltaDevice(deviceDaCollegare);
		        		}
		        		while(!validInput);
	        			
	        			stampante.collegoDevice(deviceDaCollegare);
	        		}
	        		
	        		stampante.sceltaServizio(servizioScelto);
					break;
					
				case INUSO:
					stampante.statoCorrente();
					
					if(stampante.getSelectedService() != Servizio.SCANSIONE) {
						String rispostaCartaInceppata;
						do {
			        		Log.print("La carta si è inceppata? (si/no): ");
			        		rispostaCartaInceppata = scanner.nextLine().toLowerCase();
			        		validInput = controlloInputUtenteBool(rispostaCartaInceppata);
		        		}
		        		while(!validInput);
						
						if(rispostaCartaInceppata.equals("si"))
							stampante.cartaInceppata();
					}
					
					stampante.stampanteInUso();
					break;
					
				case ERRORE:
					stampante.statoCorrente();
					
					String rispostaCartaAncoraInceppata;
					do {
		        		Log.print("La carta è ancora inceppata? (si/no): ");
		        		rispostaCartaAncoraInceppata = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaCartaAncoraInceppata);
	        		}
	        		while(!validInput);
					
					if(rispostaCartaAncoraInceppata.equals("no"))
						stampante.sistemaCarta();
					
					stampante.gestioneErrore();
					break;
        	}
        	
        }
        
        scanner.close();
        
    }

	

}
