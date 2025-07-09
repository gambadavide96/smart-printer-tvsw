package printer;

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

    private Utente[] utenti;
    private int numUtenti;
    private Utente utenteCorrente;
    
    final String denyMessage = "Azione non consentita";
    
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
    
    /*********************** GETTER AND SETTERS ****************************/
    
    protected Stato getPrinterState() {
		return printerState;
	}

	protected Servizio getSelectedService() {
		return selectedService;
	}

	protected StatoMacchina getSpiaGuasto() {
		return spiaGuasto;
	}
	
	protected Utente[] getUtenti() {
		return utenti;
	}

	protected Utente getUtenteCorrente() {
		return utenteCorrente;
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
	
	/*********************** UTILITY METHODS ****************************/
	
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
				Log.print("Non hai collegato nessun device");
				break;
			default:
				Log.print("Valore inserito non valido, inserirne un altro"); //aggiungo default case
			}
			
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
    
    protected boolean aggiungiUtente(Utente u) {
        if(numUtenti < utenti.length) {
        	utenti[numUtenti] = u;
        	numUtenti++;
        	Log.print("Utente aggiunto: " + u.getNome() + " : " + u.getBadgeId());
        	return true;
        }
        
        Log.print("Non è possibile aggiungere altri utenti");
    	return false;
        
    }
    
    /******************** REGOLE STAMPANTE *********************************/

    protected boolean accendiStampante() {
    	if(printerState == Stato.SPENTA) {
    		printerState = Stato.AVVIO;
    		Log.print("Accensione in corso");
    		return true;
    	}
    	Log.print("La stampante è già accesa");
    	return false;
    	
    }

    protected boolean avvioStampante() {
    	if(printerState == Stato.AVVIO) {
    		if(spiaGuasto == StatoMacchina.GUASTA) {
    			printerState = Stato.OUTOFSERVICE;
    			Log.print("Stampante guasta");
    			return false;
        	}
        	else {
    			printerState = Stato.MOSTRABADGE;
    			Log.print("Mostrare Badge");
    			return true;
    		}
    	}
    	Log.print(denyMessage);
    	return false;
    }
    
    protected boolean gestioneGuasto() {
    	if(printerState == Stato.OUTOFSERVICE) {
    		if(spiaGuasto == StatoMacchina.NONGUASTA) {
    			printerState = Stato.MOSTRABADGE;
    			Log.print("Mostrare Badge");
    			return true;
        	}
        	else {
        		Log.print("Stampante ancora guasta");
        		return false;
        	}
    	}
    	Log.print(denyMessage);
    	return false;
    }
    
    protected boolean identificazioneUtente(int numBadge) {
    	if(printerState == Stato.MOSTRABADGE) {
    	 	if(ricercaUtente(numBadge)) {
    			utenteCorrente = getUtentebyNumBadge(numBadge);
    			printerState = Stato.INSERISCIPIN;
    			Log.print("Benvenuto " + utenteCorrente.getNome() + ", Inserisci Pin");
    			return true;
        	}
        	else {
        		Log.print("Badge non riconosciuto");
        		return false;
        	}
    	}
    	Log.print(denyMessage);
    	return false;
    }

   
 protected boolean inserimentoPin(int insertedPin) {
    	
    	if(printerState == Stato.INSERISCIPIN) {
    		if(utenteCorrente.verificaPin(insertedPin)) {
	    		printerState = Stato.PRONTA;
	    		Log.print("Selezionare una azione");
	    		return true;
    		}
	    	else {
	    		printerState = Stato.MOSTRABADGE;
	    		utenteCorrente = null;
	    		Log.print("Pin errato");
	    		return false;
	    	}
    	}
    	
    	Log.print(denyMessage);
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
 					Log.print("Stampa BN in corso");
 					return true;
 	 			}
 				else {
 					Log.print("Risorse finite, caricare stampante");
 					return false;
 				}
 	 		}
 	 		else {
 	 			Log.print("Credito insufficiente");
 	 			return false;
 	 		}
 		}
 		Log.print(denyMessage);
    	return false;
 	}
 	
 	protected boolean stampaCOL() {
 		if(printerState == Stato.PRONTA) {
 			if(utenteCorrente.haCreditoSufficiente()) {
 	 			if(tonerNero >= 5 && tonerColore >=5 && fogliCarta >= 10) {
 					printerState = Stato.INUSO;
 					selectedService = Servizio.PRINTCOL;
 					tonerNero = tonerNero - 5;
 					tonerColore = tonerColore - 5;
 					fogliCarta = fogliCarta - 10;
 					utenteCorrente.scalaCredito(50);
 					Log.print("Stampa a Colori in corso");
 					return true;
 	 			}
 				else {
 					Log.print("Risorse finite, caricare stampante");
 					return false;
 				}
 	 		}
 	 		else {
 	 			Log.print("Credito insufficiente");
 	 			return false;
 	 		}
 		}
 		Log.print(denyMessage);
    	return false;
 	}
 	
 	protected boolean scansione() {
 		if(printerState == Stato.PRONTA) {
 			if (collegatoWireless || collegatoCavo) {
 				printerState = Stato.INUSO;
 				selectedService = Servizio.SCANSIONE;
 				Log.print("Scansione in corso");
 				return true;
 			}
 	 		else {
 	 			Log.print("Collega un dispositivo per effettuare la scansione");
 	 			return false;
 	 		}
 		}
 		Log.print(denyMessage);
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
				utenteCorrente = null;
				Log.print("Stampante spenta");
				return true;
			default:
				Log.print("Valore inserito non valido, inserirne un altro"); //aggiungo default case
    		}
    		
    	}
    	
    	Log.print(denyMessage);
    	return false;
    	
    }
    
    protected boolean stampanteInUso() {
    	if(printerState == Stato.INUSO){
    		
    		if(selectedService == Servizio.SCANSIONE) {
	    		printerState = Stato.PRONTA;
	    		Log.print("Scansione terminata con successo");
	    		//Si scollegano i device
	    		collegatoWireless = false;
	    		collegatoCavo = false;
	    		return true;
    		}
    		//è in corso una stampa
    		else {
	    		if(!cartaInceppata) {
	    			printerState = Stato.PRONTA;
	    			Log.print("Stampa terminata con successo");
	    			return true;
	    		}
	    		else {
	    			printerState = Stato.ERRORE;
	    			Log.print("Carta inceppata");
					utenteCorrente.rimborsaCredito(50);
					return false;
	    		}
    		}
	    }
    	Log.print(denyMessage);
    	return false;
    }
    	
    
    protected boolean gestioneErrore() {
    	if(printerState == Stato.ERRORE) {
    		if(!cartaInceppata) {
        		printerState = Stato.PRONTA;
        		Log.print("Carta sistemata");
        		return true;
        	}
        	return false;
    	}
    	
    	Log.print(denyMessage);
    	return false;
    	
    }

    protected void statoCorrente() {
        Log.print("\n--- STATO CORRENTE ---");
        Log.print("Stato: " + printerState);
        Log.print("Guasto: " + spiaGuasto);
        Log.print("Utente loggato: " + getNomeUtenteLoggato(utenteCorrente));
        Log.print("Credito utente: " + getCreditoUtente(utenteCorrente));
        Log.print("Toner Nero: " + tonerNero);
        Log.print("Toner Colore: " + tonerColore);
        Log.print("Carta: " + fogliCarta);
        Log.print("Collegato Wireless: " + collegatoWireless);
        Log.print("Collegato Cavo: " + collegatoCavo);
        Log.print("Carta inceppata: " + cartaInceppata);
        Log.print("------------------------\n");
    }
    
}
