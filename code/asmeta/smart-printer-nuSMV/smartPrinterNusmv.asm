asm smartPrinterNusmv

import StandardLibrary
import CTLLibrary

signature:
	
	abstract domain Badge
	enum domain Stato = {SPENTA | AVVIO | MOSTRABADGE | INSERISCIPIN | PRONTA | INUSO | ERRORE | OUTOFSERVICE}
	enum domain Servizio = {PRINTBN | PRINTCOL | SCANSIONE | EXIT}
	enum domain Accensione = {ON | OFF}
	enum domain StatoMacchina = {GUASTA | NONGUASTA}
	domain QuantitaCarta subsetof Integer	// Il numero di fogli per la stampa
	domain LivelloToner subsetof Integer	// Il livello del toner in %
	domain Secondi subsetof Integer
	domain Pin subsetof Integer
	domain Credit subsetof Integer
	
	dynamic controlled printerState: Stato
	dynamic controlled tonerNero: LivelloToner
	dynamic controlled tonerColore: LivelloToner
	dynamic controlled fogliCarta: QuantitaCarta
	dynamic controlled currentBadge: Badge
	dynamic controlled userCredit: Badge -> Credit
	dynamic controlled secondi: Secondi
	dynamic controlled selectedService: Servizio
	
	dynamic monitored onOff: Accensione
	dynamic monitored spiaGuasto: StatoMacchina
	dynamic monitored insertedBadge: Badge 
	dynamic monitored insertedPin: Pin 
	dynamic monitored chooseService: Servizio
	dynamic monitored connectedByWireless: Boolean
	dynamic monitored connectedByCable: Boolean
	dynamic monitored cartaInceppata: Boolean
	
	static pin: Badge -> Pin
	
	derived checkCredit: Badge -> Boolean
	
	static davide: Badge
	static matteo: Badge
	
definitions:

	domain QuantitaCarta = {0 : 500}
	domain LivelloToner = {0 : 100}
	domain Secondi = {0 : 5}
	//Modifica per NuSMV
	domain Pin = {0 : 1000}
	domain Credit = {0 : 1000}
	
	function pin($b in Badge) =
		switch($b)
			case davide : 1
			case matteo : 2
		endswitch
	
	//Controlla che l'utente abbia abbastanza credito per l'operazione di stampa
	function checkCredit($b in Badge) =
		userCredit($b) >= 50
	
	macro rule r_accendiStampante($b in Accensione) = 
		if(printerState = SPENTA and $b = ON) then
			printerState := AVVIO
		endif
	
	//la macchina si avvia in 3s, se sorge un problema si accende spia guasto e passa in outofservice
	rule r_avvioStampante = 
		if(spiaGuasto = GUASTA) then
			printerState := OUTOFSERVICE
		else
			printerState := MOSTRABADGE
		endif
		
	//Se il guasto è stato riparato, la macchina si avvia, altrimenti rimane outofservice	
	rule r_gestioneGuasto($g in StatoMacchina) = 
		if($g = NONGUASTA) then
			printerState := AVVIO
		endif
		
	rule r_identificazione_utente =
		if(exist $b in Badge with $b = insertedBadge) then
				par
					currentBadge := insertedBadge
					printerState := INSERISCIPIN
				endpar
		endif
		
	rule r_inserimento_pin = 
		if(insertedPin = pin(currentBadge)) then
				printerState := PRONTA
		else
			printerState := MOSTRABADGE
		endif
		
		
		rule r_stampa_bn = 
			if(checkCredit(currentBadge)) then 
				if(tonerNero >= 5 and fogliCarta >= 10) then
					par
						printerState := INUSO
						selectedService := chooseService
						tonerNero := tonerNero - 5
						fogliCarta := fogliCarta - 10
						userCredit(currentBadge) := userCredit(currentBadge) - 50
					endpar
				endif
			endif
		
		rule r_stampa_col = 
			if(checkCredit(currentBadge)) then 
				if(tonerNero >= 5 and tonerColore >= 5 and fogliCarta >= 10) then
					par
						printerState := INUSO
						selectedService := chooseService
						tonerNero := tonerNero - 5
						tonerColore := tonerColore - 5
						fogliCarta := fogliCarta - 10
						userCredit(currentBadge) := userCredit(currentBadge) - 50
					endpar
				endif
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
							userCredit(currentBadge) := userCredit(currentBadge) + 50
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
							userCredit(currentBadge) := userCredit(currentBadge) + 50
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
			
		
	main rule r_main = 
		switch printerState
			case SPENTA:
				r_accendiStampante[onOff]
			case AVVIO:
				r_avvioStampante[]
			case OUTOFSERVICE:
				r_gestioneGuasto[spiaGuasto]
			case MOSTRABADGE:
				r_identificazione_utente[]
			case INSERISCIPIN:
				r_inserimento_pin[]
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
	
	function userCredit($b in Badge) = switch($b)
										case davide : 1000
										case matteo : 1000
									endswitch
	
	

