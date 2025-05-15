package proprietario;

public class ProprietarioVip extends Proprietario {
	private static final int puntiscontoil=2;
	private int puntiFedelta;
	
	public ProprietarioVip(String cod,String name, String surname) {
		super(cod,name,surname);
		puntiFedelta=0;
	}
	
	public void incrementaPuntiFedelta() {
		puntiFedelta +=1;
	}
	
	@Override
	public void getInfoProprietario() {
		System.out.println("Codice Fiscale: "+cf+"; Nome: "+nome+"; numero di pets: "+listaPets.size()+ "; punti fedelta: " +puntiFedelta);
	}
	
	@Override
	public int getPuntiFedelta() {
		return puntiFedelta;
	}
	
	@Override
	public void utilizzaScontoToilettatura() {
		puntiFedelta-=puntiscontoil;		//Ogni volta che utilizzo lo sconto sottraggo punti
	}
}
