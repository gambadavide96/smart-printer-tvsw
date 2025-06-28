package magazzino;

public class ScortePennuto extends Scorte {
	private final TipoAlimento tipo = TipoAlimento.SECCO;
	
	public ScortePennuto(int q) {
		super(q);
	}

}
