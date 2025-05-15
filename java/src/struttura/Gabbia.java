package struttura;

public class Gabbia {
	protected static int count=0;
	protected int idGabbia;
	protected DimensioneGabbia grandezza;
	protected StatoGabbia stato;
	
	public Gabbia(DimensioneGabbia dim) {
		this.grandezza=dim;
		count++;
		idGabbia=count;
		stato=StatoGabbia.NONASSEGNATA;
	}
	
	public int getIdGabbia() {
		return idGabbia;
	}
	
	public DimensioneGabbia getGrandezza() {
		return grandezza;
	}
	
	public StatoGabbia getStatoGabbia() {
		return stato;
	}
	
	public String getInfoGabbia() {
		return " idGabbia: "+idGabbia+"; Grandezza: "+grandezza+"; Stato: "+stato;
	}
	
	public void cambiaStatoInAssegnato() {
		stato=StatoGabbia.ASSEGNATA;
	}
	
	public void cambiaStatoInNonAssegnato() {
		stato=StatoGabbia.NONASSEGNATA;
	}
}
