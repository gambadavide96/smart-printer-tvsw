package struttura;
import java.time.LocalDate;

import java.util.*;

import animale.Pet;
import prenotazioni.GestionePensione;
import prenotazioni.PrenotazionePensione;
import proprietario.Proprietario;

public final class Struttura {
	private  static Struttura oasiA;
	public String name;
	public  ArrayList<Stanza> listaStanze;
	private  ArrayList<Proprietario> listaProprietari;
	protected  ArrayList<Gabbia> listaGabbie;
	private boolean inizializzata=false;

	private Struttura() {
		name="Pensione Oasi Animali";
		listaStanze=new ArrayList<Stanza>();
		listaProprietari=new ArrayList<Proprietario>();
		listaGabbie=new ArrayList<Gabbia>();
	}

	public static Struttura getInstance() {
		if (oasiA == null)
			oasiA = new Struttura();
		return oasiA;
	}

	
	public void inizializzaStruttura() {
	//controllo che non sia gia'stata inizializzata precedentemente
		if(inizializzata==false) {
		//creo le stanze
		Stanza s1=new Stanza(18);
		Stanza s2=new Stanza(21);
		Stanza s3=new Stanza(32);
		
		//aggiungo le stanze alla lista stanze della struttura
		this.aggiungiStanza(s1);
		this.aggiungiStanza(s2);
		this.aggiungiStanza(s3);
		
		//creo tre gabbie small due medium e una large
		Gabbia g1=new Gabbia(DimensioneGabbia.LARGE);
		Gabbia g2=new Gabbia(DimensioneGabbia.SMALL);
		Gabbia g3=new Gabbia(DimensioneGabbia.LARGE);
		Gabbia g4=new Gabbia(DimensioneGabbia.LARGE);
		Gabbia g5=new Gabbia(DimensioneGabbia.MEDIUM);
		Gabbia g6=new Gabbia(DimensioneGabbia.SMALL);
		Gabbia g7=new Gabbia(DimensioneGabbia.SMALL);
		Gabbia g8=new Gabbia(DimensioneGabbia.LARGE);
		Gabbia g9=new Gabbia(DimensioneGabbia.MEDIUM);
		
		//aggiungo le gabbie in struttura 
		this.aggiungiGabbia(g1);
		this.aggiungiGabbia(g2);
		this.aggiungiGabbia(g3);
		this.aggiungiGabbia(g4);
		this.aggiungiGabbia(g5);
		this.aggiungiGabbia(g6);
		this.aggiungiGabbia(g7);
		this.aggiungiGabbia(g8);
		this.aggiungiGabbia(g9);
		
		//assegno gabbie g1,g2,g3,g7 a stanza s1 (posizione 0)
		this.listaStanze.get(0).inserisciGabbia(g1);
		this.listaStanze.get(0).inserisciGabbia(g2);
		this.listaStanze.get(0).inserisciGabbia(g3);
		this.listaStanze.get(0).inserisciGabbia(g7);
		
		//assegno gabbie g4,g6,g6 a stanza s2 (posizione 1)
		this.listaStanze.get(1).inserisciGabbia(g4);
		this.listaStanze.get(1).inserisciGabbia(g5);
		this.listaStanze.get(1).inserisciGabbia(g6);
		
		//assegno gabbia g8,g9 a stanza s3 (posizione 2)
		this.listaStanze.get(2).inserisciGabbia(g8);
		this.listaStanze.get(2).inserisciGabbia(g9);
		
		inizializzata=true;
	  }
		else {		
			System.out.println("la struttura e' gia' stata inizializzata!");
			
		}	
	}
	
	
	// Gestione delle stanze
	public  void aggiungiStanza(Stanza s) {
		if(listaStanze.size()<3)
			listaStanze.add(s);
		else
			System.out.println("Non e' possibile aggiungere altre stanze");
	}


	public void getInfoStruttura() {
		for(Stanza s:listaStanze) {
			System.out.print("Stanza numero: "+ s.getNumeroStanza()+" gabbie: ");
			for(Gabbia g:s.listaGabbie)
				System.out.print(g.getInfoGabbia());
			    System.out.println("");
		}
}

	public  ArrayList<Stanza> getStanzeconGabbia() {
		ArrayList<Stanza> listaStanzeconGabbia=new ArrayList<Stanza>();
		for(Stanza s:listaStanze) {
			if(!s.stanzaVuota())
				listaStanzeconGabbia.add(s);
		}
		if(listaStanzeconGabbia.isEmpty()) {
			System.out.println("Non ci sono Stanze con gabbie");
			return null;
		}
		else
			return listaStanzeconGabbia;
	}

	//Gestione Gabbie
	public void aggiungiGabbia(Gabbia g) {
		listaGabbie.add(g);
	}

	//Cancello Gabbia solo se presente e non assegnata ad una stanza
	public void cancellaGabbia(Gabbia g) {
		if(listaGabbie.contains(g)) {
			if(g.stato==StatoGabbia.NONASSEGNATA) {
				listaGabbie.remove(g);
			} else {
				System.out.println("Impossibile cancellare la gabbia poiche assegnata ad una stanza");
			}
		} else {
			System.out.println("Impossibile cancellare la gabbia poiche non presente in struttura");
		}
	}

	//Gestione dei proprietari
	public  void registraProprietario(Proprietario p) {
		for (Proprietario proprietario:listaProprietari) {
			if (proprietario.equals(p)) {
				System.out.println("Il proprietario risulta gia registrato");
			return;
			}
		}
		listaProprietari.add(p);
	}

	// se cancello un proprietario dal mio registro cancello anche tutte le sue prenotazioni
	public  void cancellaProprietario(Proprietario p) {
		for(Proprietario proprietario:listaProprietari) {
			if (proprietario.equals(p)) {
				listaProprietari.remove(proprietario);
				GestionePensione.cancellaPrenotazioniProprietario(p);
				break;
			}
		}
	}

	public  void stampaProprietariRegistrati() {
		for(Proprietario p:listaProprietari) {
			p.getInfoProprietario();
		}
	}
	public  ArrayList<Proprietario> getListaProprietari(){
		return listaProprietari;
	}
	
	public  ArrayList<Gabbia> getListaGabbie(){
		return listaGabbie;
	}
	

	public  boolean proprietarioNonRegistrato(Proprietario padrone) {
		for(Proprietario p:listaProprietari) {
			if(p.equals(padrone))
				return false;
		}
		return true;
	}

	public  int trovaStanza(Gabbia gabbia) {
		for(Stanza s:listaStanze) {
			if(!s.stanzaVuota() && s.gabbiaTrovata(gabbia))
				return s.getNumeroStanza();
		}
		return 0;
	}

	//Inserita una data ritorna le gabbie da pulire poiche' al loro interno c'e un pet
	public  void gabbieDaPulire(LocalDate date) {
		ArrayList<Gabbia> listaGabbie = new ArrayList<>();
		//Creo una lista di gabbie prenotate in quel giorno e quindi da pulire
		for(PrenotazionePensione p:GestionePensione.getListaPrenotazioni()) {
			//Verifico che la data non sia prima della data di Inizio e che non sia dopo la data di Fine
			if (!(date.isBefore(p.getDataInizio())) && !(date.isAfter(p.getDataFine()))) {
				listaGabbie.add(p.getGabbia());
			}
		}
		//Stampo la lista di gabbie trovate
		if(!listaGabbie.isEmpty()) {
			System.out.println("Le gabbie da pulire in data "+ date+" sono:");
			for(Gabbia g:listaGabbie) {
				System.out.println(" "+ g.getInfoGabbia()+" alla Stanza n:" +trovaStanza(g));
			}
		} else {
			System.out.println("Non ci sono gabbie da pulire in data " +date);
		} 
	}
}
