package proprietario;
import java.util.ArrayList;
import animale.*;


public abstract class Proprietario {
	protected String cf;
	protected String nome;
	protected String cognome;
	protected ArrayList<Pet> listaPets;

	public Proprietario(String cod, String name, String surname) {
		// controllo lunghezza cod fiscale
		if(cod.length() != 16) {
			throw new CfNonValidoException("Inserire un codice fiscale corretto");
		}
		cf=cod;
		nome=name;
		cognome=surname;
		listaPets=new ArrayList<>();
	}

	public String getCf() {
		return cf;
	}

	public String getNome() {
		return nome;
	}

	public void getInfoPets() {
		if(listaPets.isEmpty()) {
			System.out.println(nome + " Non possiede pets");
		} else {
			for(Pet p:listaPets) {
				System.out.println(p.getInfoPet());
			}
		}
	}

	public Pet getPet(int id) {
		for(Pet p:listaPets) {
			if(p.getIdPet()==id)
				return p;
		}
		return null;
	}

	public void getInfoProprietario() {
		System.out.println("Codice Fiscale: "+cf+"; Nome: "+nome+"; numero di pets: "+listaPets.size());
	}

	public void inserisciPet(Pet p) {
		listaPets.add(p);
	}

	public void cancellaPet(Pet pet) {
		if (!listaPets.isEmpty()) {
			for(Pet p:listaPets) {
				if(p.equals(pet)) {
					listaPets.remove(p);
					return;
				}
			}
		}
		System.out.println("Il pet non appartiene a questo proprietario");
	}

	public boolean petNonRegistrato(Pet pet){
		for(Pet p:listaPets) {
			if(p.equals(pet))
				return false;
		}
		return true;
	}

	public int getPuntiFedelta() {
		 final int a=0;
		return a;
	}

	public void utilizzaScontoToilettatura() {
		System.out.println("Un cliente Standard non può utilizzare lo sconto");
	}

}
