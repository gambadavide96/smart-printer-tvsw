package animale;

public class Pet {
	private static int count=0;
	private String nome;
	private Taglia grandezza;
	private TipoAnimale tipo;
	private int idPet;
	
	public Pet(String n,Taglia g,TipoAnimale t) {
		count++;
		idPet=count;
		nome=n;
		grandezza=g;
		tipo=t;
	}
	
	// costruttore per il quale non si indica la taglia poiche si sottointende piccola
	public Pet(String n,TipoAnimale t) {
		this(n,Taglia.PICCOLO,t);
	}
	
	public int getIdPet() {
		return idPet;
	}
	
	public String getNomePet() {
		return nome;
	}
	
	public Taglia getGrandezza() {
		return grandezza;
	}
	
	public TipoAnimale getTipoAnimale() {
		return tipo;
	}
	
	public String getInfoPet() {
		return "Nome: "+nome+"; Taglia: "+grandezza+"; Tipo: "+tipo;
	}
	
}
