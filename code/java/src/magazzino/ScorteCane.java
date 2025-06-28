package magazzino;

public class ScorteCane extends Scorte{
	private TipoAlimento tipo;
	
	public ScorteCane(int q,TipoAlimento tipo) {
		super(q);
		this.tipo=tipo;
	}

}
