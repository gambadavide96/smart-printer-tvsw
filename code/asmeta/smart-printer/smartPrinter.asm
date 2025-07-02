asm smartPrinter

import StandardLibrary

signature:

	enum domain Stato = {SPENTA | AVVIO | PRONTA | INUSO | ERRORE | OUTOFSERVICE}
	enum domain Servizio = {PRINTBN | PRINTCOL | SCANSIONE}
	domain QuantitaCarta subsetof Integer	// Il numero di fogli per la stampa
	domain LivelloToner subsetof Integer	// Il livello del toner in %
	domain Secondi subsetof Integer
	
	dynamic controlled tonerNero: LivelloToner
	dynamic controlled tonerColore: LivelloToner
	dynamic controlled fogliCarta: QuantitaCarta
	dynamic controlled printerState: Stato
	dynamic controlled secondi: Secondi
	
	dynamic monitored onOff: Boolean
	dynamic monitored selectedService: Servizio
	
	
	
definitions:

	domain QuantitaCarta = {0 : 500}
	domain LivelloToner = {0 : 100}
	domain Secondi = {0 : 3}
	
	rule r_accendiStampante($b in Boolean) = 
		if(printerState = SPENTA and $b = true) then
			printerState := AVVIO
		endif
		
	rule r_avviaStampante = 
		if(printerState = AVVIO and secondi < 3) then
			secondi := secondi + 1
		else
			par
				printerState := PRONTA
				secondi := 0 
			endpar
		endif
		

	main rule r_main = 
		seq
			r_accendiStampante[onOff]
			r_avviaStampante[]
		endseq
	
			
		
default init s0:

	function printerState = SPENTA
	function secondi = 0
	
	function tonerNero = 100
	function tonerColore = 100
	function fogliCarta = 200

