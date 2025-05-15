package prenotazioni;

public class PrenotazioneNonValidaException extends Exception {
	public PrenotazioneNonValidaException(String messaggio){
		super(messaggio);
	}
}
