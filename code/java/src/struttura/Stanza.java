package struttura;

import java.util.ArrayList;
import prenotazioni.GestionePensione;
import prenotazioni.PrenotazionePensione;

public class Stanza {
	private static int count=0;
	private int numeroStanza;
	private double metratura;
	protected ArrayList<Gabbia> listaGabbie;
	
	public Stanza(int m){
		metratura=m;
		count++;
		numeroStanza=count;	
		listaGabbie=new ArrayList<>();
	}
	
	public int getNumeroStanza() {
		return numeroStanza;
	}
	
	public double getMetratura() {
		return metratura;
	}
	
	public int getNumeroGabbie() {
		return listaGabbie.size();
		}
	
	public void getInfoGabbie() {
		if(listaGabbie.isEmpty()) {
			System.out.println("Nella stanza numero "+numeroStanza+" non sono presenti gabbie");
		}
		System.out.println("Info gabbie presenti nella stanza " + numeroStanza + ":");
		for(Gabbia p:listaGabbie) {
			System.out.println(p.getInfoGabbia());
		}
	}

	//Se la gabbia e' presente all'interno della stanza ritorna il numero della stanza
	public boolean gabbiaTrovata(Gabbia gabbia) {
		for(Gabbia g:listaGabbie) {
			if(g.equals(gabbia))
				return true;
		}
		return false;
	}
	// Calcola il numero di metri occupati dalle gabbie della stanza
	public double calcolaMetraturaGabbie() {
		double count = 0;
		for(Gabbia g:listaGabbie) {
			count += g.getGrandezza().getMetriq();
		}
		return count;
	}
	
	//Inserisco gabbia in stanza
	public void inserisciGabbia(Gabbia g) {
		
		//controllo se presente in struttura 
		if (!Struttura.getInstance().listaGabbie.contains(g)) {
			System.out.println("Impossibile inserire la gabbia perche' non e' presente in struttura");
		   return;
		}
		
		//controllo se non e'stata gia'assegnata
		
		if(g.getStatoGabbia()==StatoGabbia.ASSEGNATA) {
			System.out.println("impossibile inserire la gabbia perche' gia assegnata in una stanza");
		    return;
		}

		// posso inserire una gabbia solo se c'e spazio nel 70% della metratura della stanza.
			double spazioLibero = (metratura*0.7)-calcolaMetraturaGabbie();
			if (spazioLibero > g.getGrandezza().getMetriq()) {
				listaGabbie.add(g);
				g.cambiaStatoInAssegnato();
			} else {
				System.out.println("Spazio insufficente, non e' possibile inserire una gabbia di dimensione: " + g.getGrandezza());
			}
		} 


	//Tolgo la gabbia all'interno della stanza solo se non risulta prenotata
	public void togliGabbia(Gabbia g) {
		if (listaGabbie.contains(g)) {
			for (PrenotazionePensione p : GestionePensione.getListaPrenotazioni()) {
				if(p.getGabbia().equals(g)) {
					System.out.println("Impossibile togliere la Gabbia poiche risulta prenotata");
					return;
				}
			}
			//La gabbia non risulta prenotata, la rimuovo dalla lista e cambio il suo stato
			listaGabbie.remove(g);
			g.cambiaStatoInNonAssegnato();
		} else {
			System.out.println("Impossibile cancellare la gabbia poiche non appartiene alla stanza");
		}
	}
	
	public boolean stanzaVuota() {
		return listaGabbie.isEmpty();
	}
	
	public ArrayList<Gabbia> getListaGabbie(){
		return listaGabbie;
	}
}

