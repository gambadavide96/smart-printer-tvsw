package prenotazioni;
import animale.*;
import java.time.*;
import java.util.ArrayList;
import proprietario.*;
import struttura.*;


public class GestionePensione {

	private static ArrayList<PrenotazionePensione> elencoPrenotazioni=new ArrayList<PrenotazionePensione>();

	public static boolean prenotazioneValida(PrenotazionePensione e) {
		// Creo array di gabbie idonee alla ricerca
		ArrayList<Gabbia> listaGabbieIdonee=new ArrayList<Gabbia>();
		//Controllo che la struttura abbia almeno una stanza con una gabbia al suo interno
		ArrayList<Stanza>listaStanzeconGabbie =Struttura.getInstance().getStanzeconGabbia();

		for(Stanza s:listaStanzeconGabbie) {
			//Creo lista con gabbie idonee per dimensione in una stanza
			for(Gabbia g: s.getListaGabbie()) {
				switch(e.getAnimale().getGrandezza()) {
				case PICCOLO:
					if(g.getGrandezza().equals(DimensioneGabbia.SMALL))
						listaGabbieIdonee.add(g);
					break;

				case MEDIO:
					if(g.getGrandezza().equals(DimensioneGabbia.MEDIUM))
						listaGabbieIdonee.add(g);
					break;
				case GRANDE:
					if(g.getGrandezza().equals(DimensioneGabbia.LARGE))
						listaGabbieIdonee.add(g);
					break;

				default: return false;
				}
			}
			//Cerco tra le gabbie idonee per dimensione in una stanza una libera per la prenotazione
			for(Gabbia g:listaGabbieIdonee) {
				//Se la gabbia g è stata prenotata controllo la coincidenza sulle date
				if(GestionePensione.getGabbiePrenotate().contains(g)) {
					boolean dataNonCoincidente=true;
					for(PrenotazionePensione p:elencoPrenotazioni) {
						if(p.getGabbia().equals(g)) {
							//se la data di fine prenotazione non è precedente all'inizio di quella prenotata e la data di inizio prenotazione non è dopo la fine di quella prenotata sono in un caso di coincidenza
							if(!(e.getDataFine().isBefore(p.getDataInizio())) && !(e.getDataInizio().isAfter(p.getDataFine()))) {
								dataNonCoincidente=false;	//Si verifica almeno una coincidenza con le prenotazioni gia presenti
							}
						}
					}
					//Una volta scandite tutte le prenotazioni se non ci sono coincidenze assegno la gabbia,altrimento continuo la ricerca
					if(dataNonCoincidente) {
						e.assegnaGabbia(g);
						return true;
					}
					//Altrimenti se la gabbia non figura tra quelle prenotate la assegno	
				} else {
					e.assegnaGabbia(g);
					return true;
				}
			}	
		}
		return false;
	}


	public static void addPrenotazione(PrenotazionePensione e) {
		if(prenotazioneValida(e)) {
			elencoPrenotazioni.add(e);
			// Controlla se proprietario è Vip
			if(e.getProprietarioVip()!=null) {
				e.getProprietarioVip().incrementaPuntiFedelta();
			}
		} else {
			System.out.println("La prenotazione non è andata a buon fine");
		}
	}

	public static void removePrenotazione(PrenotazionePensione e) {
		for (PrenotazionePensione p : elencoPrenotazioni) {
			if(p.equals(e)) {
				elencoPrenotazioni.remove(p);
				return;
			}
		}
		System.out.println("La prenotazione non puo essere cancellata perchè non è presente nell'elenco");
	}
	
	//Restitusce tutte le gabbie che sono state prenotate
	public static ArrayList<Gabbia> getGabbiePrenotate(){
		ArrayList<Gabbia> gabbiePrenotate=new ArrayList <Gabbia>();
		for(PrenotazionePensione p:elencoPrenotazioni) {
			gabbiePrenotate.add(p.getGabbia());
		}
		return gabbiePrenotate;
	}
	
	//Ritorna la lista di prenotazioni
	public static ArrayList<PrenotazionePensione> getListaPrenotazioni(){
		return elencoPrenotazioni;
	}
	
	//Stampa la lista di prenotazioni
	public static void stampaElencoPrenotazioni() {
		for(PrenotazionePensione p:elencoPrenotazioni) {
			System.out.println(p.getInfoPrenotazionePensione());
	}
	}
	
	//Cancella tutte le prenotazioni di un proprietario
	public static void cancellaPrenotazioniProprietario(Proprietario proprietario) {
		boolean trovato=false;
		for (PrenotazionePensione p:elencoPrenotazioni) {
			if(p.getProprietario().equals(proprietario)) {
				elencoPrenotazioni.remove(p);
				trovato = true;
				break;
			}
		}
		if (trovato) {
			cancellaPrenotazioniProprietario(proprietario);
		}
	}
	
	/*Calcolo il fabbisogno per specie inserita una data in ingresso */
	
	public static int conteggioFabbisognoCani(LocalDate meseInserito) {
		//Ultimo giorno del mese che varia a seconda del mese
		LocalDate ultimoGiornoMeseCorrente = LocalDate.of(meseInserito.getYear(),meseInserito.getMonth(),meseInserito.lengthOfMonth());
		int giorni;
		int fabbisogno=0;
		
		for(PrenotazionePensione p:elencoPrenotazioni) {
			//Cerco le prenotazioni per cani con date successive all'odierna e che iniziano nel corrispettivo mese
			if(p.getAnimale().getTipoAnimale()==TipoAnimale.CANE && (meseInserito.isBefore(p.getDataInizio()) || meseInserito.equals(p.getDataInizio())) && meseInserito.getMonth()==p.getDataInizio().getMonth()) {
				
				giorni = calcoloGiorniNelMese(p,ultimoGiornoMeseCorrente);
				
				switch(p.getAnimale().getGrandezza()) {
				case PICCOLO:
					fabbisogno +=50*giorni;
					break;
				case MEDIO:
					fabbisogno +=150*giorni;
					break;
				case GRANDE:
					fabbisogno +=300*giorni;
					break;
				}
			}
		} 
		return fabbisogno;
	}
	
	public static int conteggioFabbisognoGatti(LocalDate meseInserito) {
		//Ultimo giorno del mese che varia a seconda del mese
		LocalDate ultimoGiornoMeseCorrente = LocalDate.of(meseInserito.getYear(),meseInserito.getMonth(),meseInserito.lengthOfMonth());
		int giorni ;
		int fabbisogno=0;
		
		for(PrenotazionePensione p:elencoPrenotazioni) {
			//Cerco le prenotazioni per gatti con date successive/uguale all'odierna e che iniziano nel corrispettivo mese
			if(p.getAnimale().getTipoAnimale()==TipoAnimale.GATTO && (meseInserito.isBefore(p.getDataInizio()) || meseInserito.equals(p.getDataInizio())) && meseInserito.getMonth()==p.getDataInizio().getMonth()) {
				giorni = calcoloGiorniNelMese(p,ultimoGiornoMeseCorrente);
				fabbisogno += 50*giorni;
			}
		} 
		return fabbisogno;
	}
	
	public static int conteggioFabbisognoPennuti(LocalDate meseInserito) {
		//Ultimo giorno del mese che varia a seconda del mese
		LocalDate ultimoGiornoMeseCorrente = LocalDate.of(meseInserito.getYear(),meseInserito.getMonth(),meseInserito.lengthOfMonth());
		int giorni ;
		int fabbisogno=0;
		
		for(PrenotazionePensione p:elencoPrenotazioni) {
			//Cerco le prenotazioni per pennuti con date successive all'odierna e che iniziano nel corrispettivo mese
			if(p.getAnimale().getTipoAnimale()==TipoAnimale.PENNUTO && (meseInserito.isBefore(p.getDataInizio()) || meseInserito.equals(p.getDataInizio())) && meseInserito.getMonth()==p.getDataInizio().getMonth()) {
				giorni = calcoloGiorniNelMese(p,ultimoGiornoMeseCorrente);
				fabbisogno += 50*giorni;
			}
		} 
		return fabbisogno;
	}
	
	public static int conteggioFabbisognoCriceti(LocalDate meseInserito) {
		//Ultimo giorno del mese che varia a seconda del mese
		LocalDate ultimoGiornoMeseCorrente = LocalDate.of(meseInserito.getYear(),meseInserito.getMonth(),meseInserito.lengthOfMonth());
		int giorni;
		int fabbisogno=0;
		
		for(PrenotazionePensione p:elencoPrenotazioni) {
			//Cerco le prenotazioni per criceti con date successive all'odierna e che iniziano nel corrispettivo mese
			if(p.getAnimale().getTipoAnimale()==TipoAnimale.CRICETO && (meseInserito.isBefore(p.getDataInizio()) || meseInserito.equals(p.getDataInizio())) && meseInserito.getMonth()==p.getDataInizio().getMonth()) {
				giorni = calcoloGiorniNelMese(p,ultimoGiornoMeseCorrente);
				fabbisogno += 50*giorni;
			}
		}
		return fabbisogno;
	}
	
	//Calcola i giorni di prenotazione interni al mese corrente
	public static int calcoloGiorniNelMese(PrenotazionePensione p, LocalDate ultimoGiornoMeseCorrente) {
		if (p.getDataFine().isAfter(ultimoGiornoMeseCorrente)) {
			return p.getDataInizio().until(ultimoGiornoMeseCorrente).getDays();
		} else {
			return p.getDataInizio().until(p.getDataFine()).getDays();
		}
	}
	
}
