package magazzino;

public abstract class Scorte {
	private int quantita;

	public Scorte(int q) {
		quantita = q;
	}

	public int getQuantita() {
		return quantita;
	}

	public void rimuoviQuantita(int n) {
		if (quantita>=Math.abs(n)) {
			quantita -= n;
		}else {
			System.out.println("La quantita che vuoi togliere eccede le scorte");
		}
	}

	public void aggiungiQuantita(int n) {
		quantita += n;
	}
}
