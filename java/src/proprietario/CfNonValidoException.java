package proprietario;

/**
 * Eccezione chiamata quando si inserisce un codice fiscale che non è valido (lunghezza diversa da 16)
 */

public class CfNonValidoException extends RuntimeException {
	public CfNonValidoException (String messaggio){
		super(messaggio);
	}
}
