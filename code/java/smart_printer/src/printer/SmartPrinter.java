package printer;

import java.util.HashMap;
import java.util.Scanner;


public class SmartPrinter {

    public enum Stato { SPENTA, AVVIO, MOSTRABADGE, INSERISCIPIN, PRONTA, INUSO, ERRORE, OUTOFSERVICE }
    public enum Servizio { PRINTBN, PRINTCOL, SCANSIONE, EXIT }
    public enum StatoMacchina { GUASTA, NONGUASTA }

    private Stato statoStampante;
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

    public SmartPrinter() {
        statoStampante = Stato.SPENTA;
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
    
    public void guastoStampante() {
    	if(statoStampante == Stato.AVVIO)
    		spiaGuasto = StatoMacchina.GUASTA;
    }
    
    public void riparazioneStampante() {
    	if(statoStampante == Stato.OUTOFSERVICE)
    		spiaGuasto = StatoMacchina.NONGUASTA;
    }

    public void accendiStampante() {
   
    }

    public void avvioStampante() {
    	
    }
    
    public void gestioneGuasto() {
    	
    }
    
    public void identificazioneUtente() {
    	
    }

    public void inserimentoPin() {
    	
    }
    
    public void sceltaServizio() {
    	
    }
    
    public void stampanteInUso() {
    	
    }
    
    public void gestioneErrore() {
    	
    }

    public void statoCorrente() {
        System.out.println("\n--- STATO CORRENTE ---");
        System.out.println("Stato: " + statoStampante);
        System.out.println("Guasto: " + spiaGuasto);
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
        
        while(!closeProgram) {
        	
        	switch(statoStampante) {
	        	case SPENTA:
	        		System.out.println("Vuoi accendere la stampante? (si/no): ");
	        		String rispostaAccensione = scanner.nextLine().toLowerCase();
	        		if(rispostaAccensione.equals("si"))
	        			accendiStampante();
	        		else
	        			closeProgram = true;
	        		break;
				case AVVIO:
					avvioStampante();
				case OUTOFSERVICE:
					gestioneGuasto();
				case MOSTRABADGE:
					identificazioneUtente();
				case INSERISCIPIN:
					inserimentoPin();
				case PRONTA:
					sceltaServizio();
				case INUSO:
					stampanteInUso();
				case ERRORE:
					gestioneErrore();
        	}
        	
        }
        
        scanner.close();
        
    }
}
