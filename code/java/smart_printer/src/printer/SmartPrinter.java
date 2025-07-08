package printer;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import printer.SmartPrinter.Servizio;
import printer.SmartPrinter.Stato;


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
    
    private static int getCreditoUtente(Utente u) {
    	return u != null ? u.getCredito() : 0;
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
    
	protected void collegoDevice(String device) {
			
		switch (device) {
			case "W":
				collegatoWireless = true;
				break;
			case "C":
				collegatoCavo = true;
				break;
			case "D":
				collegatoWireless = true;
				collegatoCavo = true;
				break;
			case "N":
				System.out.println("Non hai collegato nessun device");
				break;
			}
			
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

	protected void setTonerNero(int tonerNero) {
		this.tonerNero = tonerNero;
	}

	protected void setTonerColore(int tonerColore) {
		this.tonerColore = tonerColore;
	}

	protected void setFogliCarta(int fogliCarta) {
		this.fogliCarta = fogliCarta;
	}

	protected void setCollegatoWireless(boolean collegatoWireless) {
		this.collegatoWireless = collegatoWireless;
	}

	protected void setCollegatoCavo(boolean collegatoCavo) {
		this.collegatoCavo = collegatoCavo;
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
    
    protected void cartaInceppata() {
    	if(printerState == Stato.INUSO)
    		cartaInceppata = true;
    }
    
    protected void cartaNonInceppata() {
    	if(printerState == Stato.ERRORE)
    		cartaInceppata = false;
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

   
 protected boolean inserimentoPin(int insertedPin) {
    	
    	if(printerState == Stato.INSERISCIPIN) {
    		if(utenteCorrente.verificaPin(insertedPin)) {
	    		printerState = Stato.PRONTA;
	    		System.out.println("Selezionare una azione");
	    		return true;
    		}
	    	else {
	    		printerState = Stato.MOSTRABADGE;
	    		utenteCorrente = null;
	    		System.out.println("Pin errato");
	    		return false;
	    	}
    	}
    	
    	System.out.println("Azione non consentita");
    	return false;
    }
 
 
 	protected boolean stampaBN() {
 		if(printerState == Stato.PRONTA) {
 			if(utenteCorrente.haCreditoSufficiente()) {
 	 			if(tonerNero >= 5 && fogliCarta >= 10) {
 					printerState = Stato.INUSO;
 					selectedService = Servizio.PRINTBN;
 					tonerNero = tonerNero - 5;
 					fogliCarta = fogliCarta - 10;
 					utenteCorrente.scalaCredito(50);
 					System.out.println("Stampa BN in corso");
 					return true;
 	 			}
 				else {
 					System.out.println("Risorse finite, caricare stampante");
 					return false;
 				}
 	 		}
 	 		else {
 	 			System.out.println("Credito insufficiente");
 	 			return false;
 	 		}
 		}
 		System.out.println("Azione non consentita");
    	return false;
 	}
 	
 	protected boolean stampaCOL() {
 		if(printerState == Stato.PRONTA) {
 			if(utenteCorrente.haCreditoSufficiente()) {
 	 			if(tonerNero >= 5 && tonerColore >=5 && fogliCarta >= 10) {
 					printerState = Stato.INUSO;
 					selectedService = Servizio.PRINTBN;
 					tonerNero = tonerNero - 5;
 					tonerColore = tonerColore - 5;
 					fogliCarta = fogliCarta - 10;
 					utenteCorrente.scalaCredito(50);
 					System.out.println("Stampa a Colori in corso");
 					return true;
 	 			}
 				else {
 					System.out.println("Risorse finite, caricare stampante");
 					return false;
 				}
 	 		}
 	 		else {
 	 			System.out.println("Credito insufficiente");
 	 			return false;
 	 		}
 		}
 		System.out.println("Azione non consentita");
    	return false;
 	}
 	
 	protected boolean scansione() {
 		if(printerState == Stato.PRONTA) {
 			if (collegatoWireless || collegatoCavo) {
 				printerState = Stato.INUSO;
 				selectedService = Servizio.SCANSIONE;
 				System.out.println("Scansione in corso");
 				return true;
 			}
 	 		else {
 	 			System.out.println("Collega un dispositivo per effettuare la scansione");
 	 			return false;
 	 		}
 		}
 		System.out.println("Azione non consentita");
    	return false;
 	}
    
    protected boolean sceltaServizio(String sceltaServizio) {
    	
    	if(printerState == Stato.PRONTA) {
    		
    		switch (sceltaServizio) {
			case "BN":
				stampaBN();
				return true;
			case "COL":
				stampaCOL();
				return true;
			case "S":
				scansione();
				return true;
			case "E":
				printerState = Stato.SPENTA;
				System.out.println("Stampante spenta");
				return true;
    		}
    		
    	}
    	
    	System.out.println("Azione non consentita");
    	return false;
    	
    }
    
    protected boolean stampanteInUso() {
    	
    	switch(selectedService) {
    		case SCANSIONE:
    			printerState = Stato.PRONTA;
    			System.out.println("Scansione terminata con successo");
    			//Si scollegano i device
    			collegatoWireless = false;
    			collegatoCavo = false;
    			return true;
    		default:
    			if(!cartaInceppata) {
    				printerState = Stato.PRONTA;
    				System.out.println("Stampa terminata con successo");
    				return true;
    			}
    			else {
    				printerState = Stato.ERRORE;
    				System.out.println("Carta inceppata");
					utenteCorrente.rimborsaCredito(50);
					return false;
    			}
    	}
    }
    
    protected boolean gestioneErrore() {
    	if(!cartaInceppata) {
    		printerState = Stato.PRONTA;
    		System.out.println("Carta sistemata");
    		return true;
    	}
    	return false;
    }

    protected void statoCorrente() {
        System.out.println("\n--- STATO CORRENTE ---");
        System.out.println("Stato: " + printerState);
        System.out.println("Guasto: " + spiaGuasto);
        System.out.println("Utente loggato: " + getNomeUtenteLoggato(utenteCorrente));
        System.out.println("Credito utente: " + getCreditoUtente(utenteCorrente));
        System.out.println("Toner Nero: " + tonerNero);
        System.out.println("Toner Colore: " + tonerColore);
        System.out.println("Carta: " + fogliCarta);
        System.out.println("Collegato Wireless: " + collegatoWireless);
        System.out.println("Collegato Cavo: " + collegatoCavo);
        System.out.println("Carta inceppata: " + cartaInceppata);
        System.out.println("------------------------\n");
    }
    
}
