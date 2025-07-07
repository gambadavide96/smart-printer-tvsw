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

    Utente[] utenti;
    int numUtenti;
    private Utente utenteCorrente;
    
    public SmartPrinter() {
        printerState = Stato.SPENTA;
        tonerNero = 100;
        tonerColore = 100;
        fogliCarta = 500;
        spiaGuasto = StatoMacchina.NONGUASTA;
        utenti = new Utente[2];
        numUtenti = 0;
        collegatoWireless = false;
        collegatoCavo = false;
        cartaInceppata = false;
    }  
    
    private static String getNomeUtenteLoggato(Utente u) {
    	return u != null ? u.getNome() : "Nessuno";
    }
    
    protected boolean ricercaUtente(int numBadgeInserito) {
    	
    	for(int i = 0; i < numUtenti; i++) {
    		if(utenti[i].getBadgeId() == numBadgeInserito)
    			return true;
    	}
    	
    	return false;
    	
    }
    
    private Utente getUtentebyNumBadge(int numBadge) {
    	
    	for(Utente u: utenti) {
    		if(u.getBadgeId() == numBadge)
    			return u;
    	}
    	
    	return null;
    }
    
    

    protected Stato getPrinterState() {
		return printerState;
	}

	protected Servizio getSelectedService() {
		return selectedService;
	}

	protected StatoMacchina getSpiaGuasto() {
		return spiaGuasto;
	}

	protected int getTonerNero() {
		return tonerNero;
	}

	protected int getTonerColore() {
		return tonerColore;
	}

	protected int getFogliCarta() {
		return fogliCarta;
	}

	protected boolean isCollegatoWireless() {
		return collegatoWireless;
	}

	protected boolean isCollegatoCavo() {
		return collegatoCavo;
	}

	protected boolean isCartaInceppata() {
		return cartaInceppata;
	}

	protected Utente[] getUtenti() {
		return utenti;
	}

	protected Utente getUtenteCorrente() {
		return utenteCorrente;
	}

	protected boolean aggiungiUtente(Utente u) {
        if(numUtenti < utenti.length) {
        	utenti[numUtenti] = u;
        	numUtenti++;
        	System.out.println("Utente aggiunto: " + u.getNome() + " : " + u.getBadgeId());
        	return true;
        }
        else
        	System.out.println("Non è possibile aggiungere altri utenti");
        	return false;
        
    }
    
    protected void guastoStampante() {
    	if(printerState == Stato.AVVIO)
    		spiaGuasto = StatoMacchina.GUASTA;
    }
    
    protected void riparazioneStampante() {
    	if(printerState == Stato.OUTOFSERVICE)
    		spiaGuasto = StatoMacchina.NONGUASTA;
    }

    protected boolean accendiStampante() {
    	if(printerState == Stato.SPENTA) {
    		printerState = Stato.AVVIO;
    		System.out.println("Accensione in corso");
    		return true;
    	}
    	System.out.println("La stampante è già accesa");
    	return false;
    	
    }

    protected boolean avvioStampante() {
    	if(printerState == Stato.AVVIO) {
    		if(spiaGuasto == StatoMacchina.GUASTA) {
    			printerState = Stato.OUTOFSERVICE;
    			System.out.println("Stampante guasta");
    			return false;
        	}
        	else {
    			printerState = Stato.MOSTRABADGE;
    			System.out.println("Mostrare Badge");
    			return true;
    		}
    	}
    	System.out.println("Azione non consentita");
    	return false;
    }
    
    protected boolean gestioneGuasto() {
    	if(printerState == Stato.OUTOFSERVICE) {
    		if(spiaGuasto == StatoMacchina.NONGUASTA) {
    			printerState = Stato.MOSTRABADGE;
    			System.out.println("Mostrare Badge");
    			return true;
        	}
        	else {
        		System.out.println("Stampante ancora guasta");
        		return false;
        	}
    	}
    	System.out.println("Azione non consentita");
    	return false;
    }
    
    protected boolean identificazioneUtente(int numBadge) {
    	if(printerState == Stato.MOSTRABADGE) {
    	 	if(ricercaUtente(numBadge)) {
    			utenteCorrente = getUtentebyNumBadge(numBadge);
    			printerState = Stato.INSERISCIPIN;
    			System.out.println("Benvenuto " + utenteCorrente.getNome() + ", Inserisci Pin");
    			return true;
        	}
        	else {
        		System.out.println("Badge non riconosciuto");
        		return false;
        	}
    	}
    	System.out.println("Azione non consentita");
    	return false;
    }

    protected boolean inserimentoPin() {
    	//TODO
    	return false;
    }
    
    protected boolean sceltaServizio() {
    	//TODO
    	return true;
    	
    }
    
    protected boolean stampanteInUso() {
    	//TODO
    	return true;
    }
    
    protected boolean gestioneErrore() {
    	//TODO
    	return false;
    }

    protected void statoCorrente() {
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
    
}
