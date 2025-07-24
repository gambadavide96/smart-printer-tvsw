package printer;

//@ code_bigint_math

public class SmartPrinter {
	
    public enum Stato { SPENTA, AVVIO, MOSTRABADGE, INSERISCIPIN, PRONTA, INUSO, ERRORE, OUTOFSERVICE }
    public enum Servizio { PRINTBN, PRINTCOL, SCANSIONE, EXIT }
    public enum StatoMacchina { GUASTA, NONGUASTA }

    private /*@ spec_public */ Stato printerState;
    private /*@ spec_public */ Servizio selectedService;
    private /*@ spec_public */ StatoMacchina spiaGuasto;
    
    private /*@ spec_public */ int tonerNero;
    private /*@ spec_public */ int tonerColore;
    private /*@ spec_public */ int fogliCarta;
    
    private /*@ spec_public */ boolean collegatoWireless;
    private /*@ spec_public */ boolean collegatoCavo;
    private /*@ spec_public */ boolean cartaInceppata;

    private /*@ spec_public */ Utente[] utenti;
    private /*@ spec_public */ int numUtenti;
    private /*@ spec_public */ Utente utenteCorrente;
    
    static final String DENY = "Azione non consentita";
    
    //@ public invariant tonerNero >= 0;
    //@ public invariant tonerColore >= 0;
    //@ public invariant fogliCarta >= 300;
    
    //@ ensures printerState == Stato.SPENTA;
    //@ ensures tonerNero == 100;
    //@ ensures tonerColore == 100;
    //@ ensures fogliCarta == 500;
    //@ ensures spiaGuasto == StatoMacchina.NONGUASTA;
    //@ ensures utenti.length == 2;
    //@ ensures numUtenti == 0;
    //@ ensures collegatoWireless == false;
    //@ ensures collegatoCavo == false;
    //@ ensures cartaInceppata == false;
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
    
    /*********************** GETTERS AND SETTERS ****************************/
    
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
	
	private static String getNomeUtenteLoggato(Utente user) {
    	return user != null ? user.getNome() : "Nessuno";
    }
    
    private static int getCreditoUtente(Utente user) {
    	return user != null ? user.getCredito() : 0;
    }
    
    //@ requires numUtenti < utenti.length;
    //@ ensures this.utenti[\old(numUtenti)] == user;
    //@ ensures numUtenti == \old(numUtenti) + 1;
    //@ ensures \result == true;
    protected boolean aggiungiUtente(Utente user) {
        utenti[numUtenti] = user;
        numUtenti++;
        System.out.println("Utente aggiunto: " + user.getNome() + " : " + user.getBadgeId());
        return true;
    }
    
    protected Utente getUtentebyNumBadge(int numBadge) {
    	for(int i = 0; i < numUtenti; i++) {
    		if(utenti[i].getBadgeId() == numBadge)
    			return utenti[i];
    	}
    	
    	return null;
    }
    
    /**
     * Imposta il tipo di collegamento del dispositivo alla stampante.
     * @param device una stringa che rappresenta il tipo di collegamento
    */
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
			default:
				System.out.println("Valore inserito non valido, inserirne un altro"); //aggiungo default case
			}
			
	}
    
	/** 
	 * Imposta la stampante a guasta,indica un evento che ha guastato la stampante
	*/
    protected void guastoStampante() {
    	if(printerState == Stato.AVVIO)
    		spiaGuasto = StatoMacchina.GUASTA;
    }
    
    /** 
	 * Imposta la stampante a non guasta, indica un evento che ha riparato la stampante
	*/
    protected void riparazioneStampante() {
    	if(printerState == Stato.OUTOFSERVICE)
    		spiaGuasto = StatoMacchina.NONGUASTA;
    }
    
    /** 
	 * La carta si è inceppata durante il processo di una stampa
	*/
    protected void cartaInceppata() {
    	if(printerState == Stato.INUSO)
    		cartaInceppata = true;
    }
    
    /** 
	 * La carta viene sistemata dopo che era andata nello stato di errore.
	*/
    protected void sistemaCarta() {
    	if(printerState == Stato.ERRORE)
    		cartaInceppata = false;
    }
    
    
    /******************** REGOLE STAMPANTE *********************************/
    
    
    /**
     * Tenta di accendere la stampante se si trova nello stato @code {SPENTA}.
     * Se la stampante è spenta, lo stato viene aggiornato a @code {AVVIO}.
     * In caso contrario, viene emesso un messaggio.
     * 
     * @return {@code true} se la transizione di stato è avvenuta con successo,
     *         	{@code false} altrimenti.
	*/
    protected boolean accendiStampante() {
    	if(printerState == Stato.SPENTA) {
    		printerState = Stato.AVVIO;
    		System.out.println("Accensione in corso");
    		return true;
    	}
    	System.out.println("La stampante è già accesa");
    	return false;
    }

    /**
     * Porta la stampante dalla fase di accensione alla fase operativa, controllando se si sono verificati guasti
     * 
     * Se la stampante è in @code {AVVIO} ma si è verificato un guasto, lo stato viene aggiornato a @code {OUTOFSERVICE}.
     * Se la stampante è in @code {AVVIO} e non si è verificato un guasto, lo stato viene aggiornato a @code {MOSTRABADGE}.
     * Se la stampante non è in @code {AVVIO}, viene emesso un messaggio di Deny.
     * 
     * @return {@code true} se la transizione di stato è avvenuta con successo,
     *         	{@code false} altrimenti.
	*/
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
    	System.out.println(DENY);
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
    	System.out.println(DENY);
    	return false;
    }
    
    protected boolean identificazioneUtente(int numBadge) {
    	if(printerState == Stato.MOSTRABADGE) {
    		utenteCorrente = getUtentebyNumBadge(numBadge);
    	 	if(utenteCorrente != null) {
    			printerState = Stato.INSERISCIPIN;
    			System.out.println("Benvenuto " + utenteCorrente.getNome() + ", Inserisci Pin");
    			return true;
        	}
        	else {
        		System.out.println("Badge non riconosciuto");
        		return false;
        	}
    	}
    	System.out.println(DENY);
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
    	
    	System.out.println(DENY);
    	return false;
    }
 
 	//@ requires printerState == Stato.PRONTA;
 	//@ requires utenteCorrente.haCreditoSufficiente();
 	//@ requires tonerNero >= 5 && fogliCarta >= 10;
 	//@ ensures printerState == Stato.INUSO;
 	//@ ensures selectedService == Servizio.PRINTBN;
 	//@ ensures tonerNero == \old(tonerNero) - 5;
 	//@ ensures fogliCarta == \old(fogliCarta) - 10;
 	//@ ensures utenteCorrente.getCredito() == \old(utenteCorrente.getCredito()) - 50;
 	//@ ensures \result == true;
 	protected boolean stampaBN() {
 		printerState = Stato.INUSO;
 		selectedService = Servizio.PRINTBN;
 		tonerNero = tonerNero - 5;
 		fogliCarta = fogliCarta - 10;
 		utenteCorrente.scalaCredito(50);
 		System.out.println("Stampa BN in corso");
 		return true;
 	}
 	
 	//@ requires printerState == Stato.PRONTA;
 	//@ requires utenteCorrente.haCreditoSufficiente();
 	//@ requires tonerNero >= 5 && tonerColore >= 5 && fogliCarta >= 10;
 	//@ ensures printerState == Stato.INUSO;
 	//@ ensures selectedService == Servizio.PRINTCOL;
 	//@ ensures tonerNero == \old(tonerNero) - 5;
 	//@ ensures tonerColore == \old(tonerColore) - 5;
 	//@ ensures fogliCarta == \old(fogliCarta) - 10;
 	//@ ensures utenteCorrente.getCredito() == \old(utenteCorrente.getCredito()) - 50;
 	//@ ensures \result == true;
 	protected boolean stampaCOL() {
 		printerState = Stato.INUSO;
 		selectedService = Servizio.PRINTCOL;
 		tonerNero = tonerNero - 5;
 		tonerColore = tonerColore - 5;
 		fogliCarta = fogliCarta - 10;
 		utenteCorrente.scalaCredito(50);
 		System.out.println("Stampa a Colori in corso");
 		return true;
 	}
 	
 	//@ requires printerState == Stato.PRONTA;
 	//@ requires collegatoWireless || collegatoCavo;
 	//@ ensures printerState == Stato.INUSO;
 	//@ ensures selectedService == Servizio.SCANSIONE;
 	//@ ensures \result == true;
 	protected boolean scansione() {
 		printerState = Stato.INUSO;
 		selectedService = Servizio.SCANSIONE;
 		System.out.println("Scansione in corso");
 		return true;
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
				System.out.println("Stampante spenta");
				return true;
			default:
				System.out.println("Valore inserito non valido, inserirne un altro"); //aggiungo default case
    		}
    		
    	}
    	
    	System.out.println(DENY);
    	return false;
    	
    }
    
    protected boolean stampanteInUso() {
    	if(printerState == Stato.INUSO){
    		
    		if(selectedService == Servizio.SCANSIONE) {
	    		printerState = Stato.PRONTA;
	    		System.out.println("Scansione terminata con successo");
	    		//Si scollegano i device
	    		collegatoWireless = false;
	    		collegatoCavo = false;
	    		return true;
    		}
    		//è in corso una stampa
    		else {
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
    	System.out.println(DENY);
    	return false;
    }
    	
    
    protected boolean gestioneErrore() {
    	if(printerState == Stato.ERRORE) {
    		if(!cartaInceppata) {
        		printerState = Stato.PRONTA;
        		System.out.println("Carta sistemata");
        		return true;
        	}
        	return false;
    	}
    	
    	System.out.println(DENY);
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
