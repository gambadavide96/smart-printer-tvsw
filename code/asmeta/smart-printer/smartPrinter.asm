asm smartPrinter

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
	
	dynamic controlled printerState: Stato
	dynamic controlled tonerNero: LivelloToner
	dynamic controlled tonerColore: LivelloToner
	dynamic controlled fogliCarta: QuantitaCarta
	dynamic controlled currentBadge: Badge
	dynamic controlled userCredit: Badge -> Integer
	dynamic controlled secondi: Secondi
	dynamic controlled message: String
	
	dynamic monitored onOff: Accensione
	dynamic monitored spiaGuasto: StatoMacchina
	dynamic monitored insertedBadge: Badge 
	dynamic monitored insertedPin: Integer 
	dynamic monitored selectedService: Servizio
	
	static pin: Badge -> Integer
	
	derived checkCredit: Badge -> Boolean
	
	static davide: Badge
	static matteo: Badge
	
definitions:

	domain QuantitaCarta = {0 : 500}
	domain LivelloToner = {0 : 100}
	domain Secondi = {0 : 3}
	
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
				par
					printerState := AVVIO
					message := "Accensione in corso"
				endpar
		endif
	
	//la macchina si avvia in 3s, se sorge un problema si accende spia guasto e passa in outofservice
	rule r_avvioStampante = 
		if(spiaGuasto = GUASTA) then
			par
				printerState := OUTOFSERVICE
				secondi := 0 
				message := "Stampante guasta"
			endpar
		else
			if(secondi < 1) then
				secondi := secondi + 1
			else
				par
					printerState := MOSTRABADGE
					secondi := 0
					message := "Mostrare Badge"
				endpar
			endif
		endif
		
	//Se il guasto è stato riparato, la macchina si avvia, altrimenti rimane outofservice	
	rule r_gestioneGuasto($g in StatoMacchina) = 
		if($g = NONGUASTA) then
			par
				printerState := AVVIO
				message := "Accensione in corso"
			endpar
		endif
		
	rule r_identificazione_utente =
		if(exist $b in Badge with $b = insertedBadge) then
				par
					currentBadge := insertedBadge
					printerState := INSERISCIPIN
					message := "Inserire Pin"
				endpar
		endif
		
	rule r_inserimento_pin = 
		if(insertedPin = pin(currentBadge)) then
				par
					printerState := PRONTA
					message := "Selezionare una azione"
				endpar
		else
			par
				printerState := MOSTRABADGE
				message := "Pin errato"
				endpar
		endif
		
	
	//Regola per gestire l'operativa della stampante una volta che è pronta
	//Posso spegnerla oppure lanciare un lavoro
	macro rule r_utilizzoStampante = 
		switch selectedService
			case PRINTBN:
				message := "Prova BN"
			case PRINTCOL:
				message := "Prova COL"
			case SCANSIONE:
				message := "Prova Scansione"
			case EXIT:
				par
					printerState := SPENTA
					message := "Stampante spenta"
				endpar
		endswitch
			
		
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
				r_utilizzoStampante[]
		endswitch
		
	
		
default init s0:

	function printerState = SPENTA
	function secondi = 0
	
	function tonerNero = 100
	function tonerColore = 100
	function fogliCarta = 200
	
	function userCredit($b in Badge) = switch($b)
										case davide : 1000
										case matteo : 1000
									endswitch
	
	function message = "Premere ON per accendere"
	

