package magazzino;

public class ScorteGatto extends Scorte {
	private TipoAlimento tipo;
	
	public ScorteGatto(int q,TipoAlimento tipo) {
		super(q);
		this.tipo=tipo;
	}

}
