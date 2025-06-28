package prenotazioni;
import proprietario.*;

import java.time.*;
import java.util.ArrayList;

import animale.*;
import struttura .*;

public class PrenotazionePensione {
	
	public int idPrenotazione;
	private Proprietario padrone;	//Padrone che affida i suoi animali
	private Pet pet; 				//Pet per il quale si vuole prenotare
	private LocalDate dataInizio;	//Data di inizio prenotazione
	private LocalDate dataFine;		//Data di fine prenotazione
	private Gabbia cucciaAssegnata;	//Posto assegnato al pet
	
	
	public PrenotazionePensione(Proprietario padro,Pet animale,LocalDate inizio,LocalDate fine) throws PrenotazioneNonValidaException {
		padrone=padro;
		pet=animale;
		dataInizio=inizio;
		dataFine=fine;
		//Controllo che la data di fine sia dopo quella di inizio, che il proprietario sia registrato e che il pet appartenga al suo padrone
		if(dataFine.isBefore(dataInizio) || Struttura.getInstance().proprietarioNonRegistrato(padrone) || padrone.petNonRegistrato(pet))
			throw new PrenotazioneNonValidaException("Prenotazione non valida!");
	}
	
	
	public Pet getAnimale() {
		return pet;
	}

	
	public Gabbia getGabbia() {
		return cucciaAssegnata;
	}
	
	public LocalDate getDataInizio() {
		return dataInizio;
	}
	
	public LocalDate getDataFine() {
		return dataFine;
	}
	
	public Proprietario getProprietario() {
		return padrone;
	}
	
	public String getInfoPrenotazionePensione() {
		return "Padrone: "+padrone.getNome()+"; Pet "+pet.getNomePet()+"; Data Inizio "+dataInizio+"; Data Fine: "+dataFine+
				"; Cuccia Assegnata: "+cucciaAssegnata.getIdGabbia();
	}
	
	public void assegnaGabbia(Gabbia g) {
		cucciaAssegnata=g;
	}

	public ProprietarioVip getProprietarioVip() {
		if (padrone instanceof ProprietarioVip)
		return (ProprietarioVip) padrone;
		else
			return null;
			
	}
}
