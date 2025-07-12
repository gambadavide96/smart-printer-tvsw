asm smartPrinterNusmv

import StandardLibrary
import CTLLibrary

signature:
	
	enum domain Stato = {SPENTA | AVVIO | PRONTA | INUSO | ERRORE | OUTOFSERVICE}
	enum domain Servizio = {PRINTBN | PRINTCOL | SCANSIONE | EXIT}
	enum domain Accensione = {ON | OFF}
	enum domain StatoMacchina = {GUASTA | NONGUASTA}
	domain QuantitaCarta subsetof Integer	// Il numero di fogli per la stampa
	domain LivelloToner subsetof Integer	// Il livello del toner in %
	domain Secondi subsetof Integer
	
	dynamic controlled printerState: Stato
	dynamic controlled tonerNero: LivelloToner
	dynamic controlled tonerColore: LivelloToner
	dynamic controlled fogliCarta: QuantitaCarta
	dynamic controlled secondi: Secondi
	dynamic controlled selectedService: Servizio
	
	dynamic monitored onOff: Accensione
	dynamic monitored spiaGuasto: StatoMacchina
	dynamic monitored chooseService: Servizio
	dynamic monitored connectedByWireless: Boolean
	dynamic monitored connectedByCable: Boolean
	dynamic monitored cartaInceppata: Boolean
	
	
definitions:

	domain QuantitaCarta = {0 : 500}
	domain LivelloToner = {0 : 100}
	domain Secondi = {0 : 5}
	
	
	
	macro rule r_accendiStampante($b in Accensione) = 
		if(printerState = SPENTA and $b = ON) then
			printerState := AVVIO
		endif
	
	
	rule r_avvioStampante = 
		if(spiaGuasto = GUASTA) then
			printerState := OUTOFSERVICE
		else
			printerState := PRONTA
		endif
		
	//Se il guasto è stato riparato, la macchina è pronta, altrimenti rimane outofservice	
	rule r_gestioneGuasto($g in StatoMacchina) = 
		if($g = NONGUASTA) then
			printerState := PRONTA
		endif
		
		
		rule r_stampa_bn = 
			if(tonerNero >= 5 and fogliCarta >= 10) then
				par
					printerState := INUSO
					selectedService := chooseService
					tonerNero := tonerNero - 5
					fogliCarta := fogliCarta - 10
				endpar
			endif
		
		rule r_stampa_col = 
			if(tonerNero >= 5 and tonerColore >= 5 and fogliCarta >= 10) then
				par
					printerState := INUSO
					selectedService := chooseService
					tonerNero := tonerNero - 5
					tonerColore := tonerColore - 5
					fogliCarta := fogliCarta - 10
					endpar
			endif
			
		rule r_scansione = 
			if (connectedByWireless or connectedByCable) then
				par
					printerState := INUSO
					selectedService := chooseService
				endpar
			endif
			
		
	//Regola per gestire l'operativa della stampante una volta che è pronta
	//Posso spegnerla oppure lanciare un lavoro
	macro rule r_sceltaServizio = 
		switch chooseService
			case PRINTBN:
				r_stampa_bn[]
			case PRINTCOL:
				r_stampa_col[]
			case SCANSIONE:
				r_scansione[]
			case EXIT:
				printerState := SPENTA
		endswitch
		
	
	macro rule r_stampanteInUso = 
		switch selectedService
			case PRINTBN:
				if (secondi < 2) then
					secondi := secondi + 1
				else
					if(not cartaInceppata) then
						par
							printerState := PRONTA
							secondi := 0
						endpar
					else
						par
							printerState := ERRORE
							secondi := 0
						endpar
					endif
				endif
			case PRINTCOL:
				if (secondi < 3) then
					secondi := secondi + 1
				else
					if(not cartaInceppata) then
						par
							printerState := PRONTA
							secondi := 0
						endpar
					else
						par
							printerState := ERRORE
							secondi := 0
						endpar
					endif
				endif
			case SCANSIONE:
				if (secondi < 4) then
					secondi := secondi + 1
				else
					par
						printerState := PRONTA
						secondi := 0
					endpar
				endif
		endswitch
	
	rule r_gestioneErrore = 
		if(not cartaInceppata) then
			printerState := PRONTA
		endif
		
	
	// MODEL CHECKING
	
	//P1: è sempre possibile che esista uno stato futuro in cui se 
	//la stampante è pronta,nello stato successivo potrebbe essere in Uso
	CTLSPEC ag(ef(printerState = PRONTA implies ex(printerState = INUSO)))
	//P2: è sempre possibile che si verifichi uno stato futuro in cui se la macchina è fuori servizio
	//rimane fuori servizio
	CTLSPEC ag(ef(printerState = OUTOFSERVICE implies eg(printerState = OUTOFSERVICE)))
	//P3: Esiste uno stato futuro in cui il Toner nero è terminato mentre il Toner a colori no
	CTLSPEC ef(tonerNero = 0 and tonerColore > 0)
	//P4: Esiste uno stato futuro in cui sia il Toner nero che quello a colori sono terminati mentre i fogli no
	CTLSPEC ef(tonerNero = 0 and tonerColore = 0 and fogliCarta > 0)
	//P5: Non può esistere uno stato futuro in cui il toner a colori è terminato mentre il toner nero no
	CTLSPEC (not ef(tonerNero > 0 and tonerColore = 0))
	//P6: In qualsiasi stato si trovi la macchina, esiste un percorso che la porta nello stato futuro di PRONTA
	CTLSPEC ag(ef(printerState = PRONTA))
	//P7: Nella stampante ci saranno sempre almeno 300 fogli 
	// (200 fogli per finire il toner -> 20 stampe -> max 200 fogli consumati)
	CTLSPEC ag(fogliCarta >= 300)
	//P8: Una volta che il toner è finito, rimane a zero
	CTLSPEC ag(tonerNero = 0 implies ag(tonerNero = 0))
	//P9: Una stampa in Bianco e nero non dura più di 2 secondi
	CTLSPEC ag((selectedService = PRINTBN and printerState = INUSO)  
		implies au(secondi <= 2, (printerState = PRONTA or printerState = ERRORE))
	)
	//P10: Una stampa a colori non dura più di 3 secondi
	CTLSPEC ag((selectedService = PRINTCOL and printerState = INUSO)  
		implies au(secondi <= 3, (printerState = PRONTA or printerState = ERRORE))
	)
	//P11: Una scansione non dura più di 4 secondi
	CTLSPEC ag((selectedService = SCANSIONE and printerState = INUSO)  
		implies au(secondi <= 4, printerState = PRONTA)
		)
	
		
	main rule r_main = 
		switch printerState
			case SPENTA:
				r_accendiStampante[onOff]
			case AVVIO:
				r_avvioStampante[]
			case OUTOFSERVICE:
				r_gestioneGuasto[spiaGuasto]
			case PRONTA:
				r_sceltaServizio[]
			case INUSO:
				r_stampanteInUso[]
			case ERRORE:
				r_gestioneErrore[]
		endswitch
		
	
		
default init s0:

	function printerState = SPENTA
	function secondi = 0
	
	function tonerNero = 100
	function tonerColore = 100
	function fogliCarta = 500
	
	
	

