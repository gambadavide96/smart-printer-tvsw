asm smartPrinter

import StandardLibrary
import CTLLibrary

signature:
	
	abstract domain Badge
	enum domain Stato = {SPENTA | AVVIO | MOSTRABADGE | INSERISCIPIN | PRONTA | INUSO | ERRORE | OUTOFSERVICE}
	enum domain Servizio = {PRINTBN | PRINTCOL | SCANSIONE | NONE}
	domain QuantitaCarta subsetof Integer	// Il numero di fogli per la stampa
	domain LivelloToner subsetof Integer	// Il livello del toner in %
	domain Secondi subsetof Integer
	
	dynamic controlled printerState: Stato
	dynamic controlled tonerNero: LivelloToner
	dynamic controlled tonerColore: LivelloToner
	dynamic controlled fogliCarta: QuantitaCarta
	dynamic controlled currentBadge: Badge
	dynamic controlled userCredit: Badge -> Integer
	dynamic controlled secondi: Secondi
	
	dynamic monitored onOff: Boolean
	dynamic monitored spiaGuasto: Boolean
	dynamic monitored insertedBadge: Badge 
	dynamic monitored insertedPin: Integer 
	dynamic monitored selectedService: Servizio
	
	static pin: Badge -> Integer
	
	derived checkCredit: Badge -> Boolean
	
	static badge1: Badge
	static badge2: Badge
	static none: Badge
	
	
	
definitions:

	domain QuantitaCarta = {0 : 500}
	domain LivelloToner = {0 : 100}
	domain Secondi = {0 : 3}
	
	function pin($b in Badge) =
		switch($b)
			case badge1 : 1
			case badge2 : 2
		endswitch
	
	//Controlla che l'utente abbia abbastanza credito per l'operazione di stampa
	function checkCredit($b in Badge) =
		userCredit($b) >= 50
	
	rule r_accendiStampante($b in Boolean) = 
		if(printerState = SPENTA and $b = true) then
			printerState := AVVIO
		endif
	
	//la macchina si avvia in 3s, se sorge un problema si accende spia guasto e passa in outofservice
	rule r_avvioStampante($g in Boolean) = 
		if(spiaGuasto) then
			par
				printerState := OUTOFSERVICE
				secondi := 0 
			endpar
		else
			if(secondi < 1) then
				secondi := secondi + 1
			else
				par
					printerState := MOSTRABADGE
					secondi := 0 
				endpar
			endif
		endif
		
	//Se il guasto è stato riparato, la macchina si avvia, altrimenti rimane outofservice	
	rule r_gestioneGuasto($g in Boolean) = 
		if(not $g) then
			printerState := AVVIO
		endif
		
	rule r_identificazione_utente =
		if(exist $b in Badge with $b = insertedBadge) then
				par
					currentBadge := insertedBadge
					printerState := PRONTA
				endpar
		endif
	
	//Regola per gestire l'operativa della stampante una volta che è pronta
	//Posso spegnerla oppure lanciare un lavoro
	macro rule r_utilizzoStampante($b in Boolean,$s in Servizio) =
		if (printerState = PRONTA and $b = true) then
			par
				printerState := SPENTA
				currentBadge := none	//modificare questa parte
			endpar
		endif
		
	main rule r_main = 
		switch printerState
			case SPENTA:
				r_accendiStampante[onOff]
			case AVVIO:
				r_avvioStampante[spiaGuasto]
			case OUTOFSERVICE:
				r_gestioneGuasto[spiaGuasto]
			case MOSTRABADGE:
				r_identificazione_utente[]
			case PRONTA:
				r_utilizzoStampante[onOff,selectedService]
		endswitch
		
	
		
default init s0:

	function printerState = SPENTA
	function secondi = 0
	
	function tonerNero = 100
	function tonerColore = 100
	function fogliCarta = 200
	
	function userCredit($b in Badge) = switch($b)
										case badge1 : 1000
										case badge2 : 1000
									endswitch

