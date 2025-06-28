package prenotazioni;

import java.time.*;

import java.util.*;
import animale.*;
import proprietario.*;
import struttura.Struttura;

public class PrenotazioneToilettatura {
	private static final int scontotoilet=2;				//Punti necessari per usufrure dello sconto		

	private Proprietario padrone;							//Padrone che affida i suoi animali
	private Pet animale; 									//Animali del padrone - solo cani e gatti
	private LocalDateTime inizioAppuntamento;				//Inizio appuntamento
	private LocalDateTime fineAppuntamento;					//Fine appuntamento
	private int addebito;									// Costo della toilettatura, in base alla taglia dell'animale

	public PrenotazioneToilettatura(Proprietario p,Pet a,LocalDateTime i) throws ToilettaturaNonValidaException{
		//Controllo che l'animale sia un cane o gatto e che il proprietario sia regitrato
		if(a.getTipoAnimale()==(TipoAnimale.CRICETO) || a.getTipoAnimale()==(TipoAnimale.PENNUTO) || p.petNonRegistrato(a)|| Struttura.getInstance().proprietarioNonRegistrato(p))
			throw new ToilettaturaNonValidaException("Toilettatura non Valida");

		padrone=p;
		animale=a;
		inizioAppuntamento=i;
		fineAppuntamento=inizioAppuntamento.plusMinutes(this.calcolaDurata(a.getGrandezza()));

		//l'addebito sarà calcolato dal metodo calcolaAddebito se la data della prenotazione è disponibile

	}

	public long calcolaDurata(Taglia t) {
		if(t==Taglia.PICCOLO)
			return 10;
		else if(t==Taglia.MEDIO)
			return 20;
		else
			return 30;
	}

	//Se il proprietario ha più di 10 punti la toilettatura è omaggio
	//Altrimenti la calcolo in base alla taglia	
	public void calcolaAddebito() {
		if(padrone.getPuntiFedelta()>=scontotoilet){			//Solo un proprietarioVip avrà almeno 10 punti
			addebito=0;												//Se il padrone ha almeno 2 punti usa lo sconto
			padrone.utilizzaScontoToilettatura();
		}
		else {
			switch(animale.getGrandezza()) {
			case PICCOLO: addebito=20;
			break;
			case MEDIO: addebito=35;
			break;
			case GRANDE: addebito=50;
			break;
			}
		}

	}

	public Proprietario getProprietario() {
		return padrone;
	}

	public Pet getAnimale() {
		return animale;
	}

	public LocalDateTime getInizioAppuntamento() {
		return inizioAppuntamento;
	}
	public LocalDateTime getFineAppuntamento() {
		return fineAppuntamento;
	}


	public int getCostoToilettatura() {
		return addebito;
	}

	public String getInfoPrenotazioneToilettatura() {

		return "Padrone: "+padrone.getNome()+"; Pet "+animale.getNomePet()+"; Data Inizio "+inizioAppuntamento+"; Data Fine: "+fineAppuntamento+
				"; Addebito:  "+addebito;
	}



}
