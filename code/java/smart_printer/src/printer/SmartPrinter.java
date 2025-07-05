package printer;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


public class SmartPrinter {

    public enum Stato { SPENTA, AVVIO, MOSTRABADGE, INSERISCIPIN, PRONTA, INUSO, ERRORE, OUTOFSERVICE }
    public enum Servizio { PRINTBN, PRINTCOL, SCANSIONE, EXIT }
    public enum StatoMacchina { GUASTA, NONGUASTA }

    private Stato printerState;
    private Servizio selectedService;
    private StatoMacchina spiaGuasto;
    private int tonerNero;
    private int tonerColore;
    private int fogliCarta;
    
    private boolean collegatoWireless;
    private boolean collegatoCavo;
    private boolean cartaInceppata;

    private HashMap<Integer, Utente> utenti;
    private Utente utenteCorrente;
    
    private static boolean controlloInputUtenteBool(String s) {
    	return s.equals("si") || s.equals("no")  ? true : false;
    }
    
    private static String getNomeUtenteLoggato(Utente u) {
    	return u != null ? u.getNome() : "Nessuno";
    }
    
    public SmartPrinter() {
        printerState = Stato.SPENTA;
        tonerNero = 100;
        tonerColore = 100;
        fogliCarta = 500;
        spiaGuasto = StatoMacchina.NONGUASTA;
        utenti = new HashMap<>();
        collegatoWireless = false;
        collegatoCavo = false;
        cartaInceppata = false;
    }

    public void aggiungiUtente(Utente u) {
        utenti.put(u.getBadgeId(), u);
        System.out.println("Utente aggiunto: " + u.getNome() + " : " + u.getBadgeId());
    }
    
    private void guastoStampante() {
    	if(printerState == Stato.AVVIO)
    		spiaGuasto = StatoMacchina.GUASTA;
    }
    
    private void riparazioneStampante() {
    	if(printerState == Stato.OUTOFSERVICE)
    		spiaGuasto = StatoMacchina.NONGUASTA;
    }

    private void accendiStampante() {
    	if(printerState == Stato.SPENTA) {
    		printerState = Stato.AVVIO;
    		System.out.println("Accensione in corso");
    	}
    }

    private void avvioStampante() {
    	if(spiaGuasto == StatoMacchina.GUASTA) {
			printerState = Stato.OUTOFSERVICE;
			System.out.println("Stampante guasta");
    	}
    	else {
			printerState = Stato.MOSTRABADGE;
			System.out.println("Mostrare Badge");
		}
    }
    
    private void gestioneGuasto() {
    	
    	if(spiaGuasto == StatoMacchina.NONGUASTA) {
			printerState = Stato.MOSTRABADGE;
			System.out.println("Mostrare Badge");
    	}
    	else {
    		System.out.println("Stampante ancora guasta");
    	}
    }
    
    private void identificazioneUtente(int numBadge) {
    	if(utenti.containsKey(numBadge)) {
			utenteCorrente = utenti.get(numBadge);
			printerState = Stato.INSERISCIPIN;
			System.out.println("Benvenuto " + utenteCorrente.getNome() + ", Inserisci Pin");
    	}
    	else {
    		System.out.println("Badge non riconosciuto");
    	}
    }

    private void inserimentoPin() {
    	
    }
    
    private void sceltaServizio() {
    	
    }
    
    private void stampanteInUso() {
    	
    }
    
    private void gestioneErrore() {
    	
    }

    public void statoCorrente() {
        System.out.println("\n--- STATO CORRENTE ---");
        System.out.println("Stato: " + printerState);
        System.out.println("Guasto: " + spiaGuasto);
        System.out.println("Utente loggato: " + getNomeUtenteLoggato(utenteCorrente));
        System.out.println("Toner Nero: " + tonerNero);
        System.out.println("Toner Colore: " + tonerColore);
        System.out.println("Carta: " + fogliCarta);
        System.out.println("Collegato Wireless: " + collegatoWireless);
        System.out.println("Collegato Cavo: " + collegatoCavo);
        System.out.println("Carta inceppata: " + cartaInceppata);
        System.out.println("------------------------\n");
    }
    
    // Interazione con la stampante
    public void usoStampante() {
        Scanner scanner = new Scanner(System.in);
        boolean closeProgram = false;
        boolean validInput = false;
        
        while(!closeProgram) {
        	
        	switch(printerState) {
	        	case SPENTA:
	        		String rispostaAccensione;
	        		do {
		        		System.out.println("Vuoi accendere la stampante? (si/no): ");
		        		rispostaAccensione = scanner.nextLine().toLowerCase();
		        		validInput = controlloInputUtenteBool(rispostaAccensione);
	        		}
	        		while(!validInput);
	        			
	        		if(rispostaAccensione.equals("si"))
	        			accendiStampante();
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
	        			guastoStampante();
	        		
	        		avvioStampante();
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
	        			riparazioneStampante();
					
					gestioneGuasto();
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
					identificazioneUtente(badgeMostrato);
	        		break;
	        		
				case INSERISCIPIN:
					//inserimentoPin();
					statoCorrente();
					System.out.println("Cjeckpoint inserisci pin ");
	        		String check3 = scanner.nextLine().toLowerCase();
	        		
					break;
				case PRONTA:
					sceltaServizio();
					break;
				case INUSO:
					stampanteInUso();
					break;
				case ERRORE:
					gestioneErrore();
					break;
        	}
        	
        }
        
        scanner.close();
        
    }
}
