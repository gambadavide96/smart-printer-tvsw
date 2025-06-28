package magazzino;

import java.time.LocalDate;

import prenotazioni.GestionePensione;

public class GestioneMagazzino {
	static ScorteCane sCaneSecco = new ScorteCane(0, TipoAlimento.SECCO); //inizzializzo scorte cane
	static ScorteCane sCaneUmido = new ScorteCane(0, TipoAlimento.UMIDO);//inizzializzo scorte cane
	static ScorteGatto sGattoSecco = new ScorteGatto(0, TipoAlimento.SECCO);//inizzializzo scorte gatto
	static ScorteGatto sGattoUmido = new ScorteGatto(0, TipoAlimento.UMIDO);////inizzializzo scorte gatto
	static ScortePennuto sPennuto = new ScortePennuto(0);//inizzializzo scorte pennuto
	static ScorteCriceto sCriceto = new ScorteCriceto(0);//inizzializzo scorte criceto

	// aggiungo e sottraggo alle ScorteCane
	public static void aggiungiScorteCaneSecco(int i) {
		sCaneSecco.aggiungiQuantita(i);
	}
	public static void aggiungiScorteCaneUmido(int i) {
		sCaneUmido.aggiungiQuantita(i);
	}
	public static void sottraiScorteCaneSecco(int i) {
		sCaneSecco.rimuoviQuantita(i);
	}
	public static void sottraiScorteCaneUmido(int i) {
		sCaneUmido.rimuoviQuantita(i);
	}

	// aggiungo e sottraggo alle ScorteGatto
	public static void aggiungiScorteGattoSecco(int i) {
		sGattoSecco.aggiungiQuantita(i);
	}
	public static void aggiungiScorteGattoUmido(int i) {
		sGattoUmido.aggiungiQuantita(i);
	}
	public static void sottraiScorteGattoSecco(int i) {
		sGattoSecco.rimuoviQuantita(i);
	}
	public static void sottraiScorteGattoUmido(int i) {
		sGattoUmido.rimuoviQuantita(i);
	}

	// aggiungo e sottraggo alle ScortePennuto
	public static void aggiungiScortePennuto(int i) {
		sPennuto.aggiungiQuantita(i);
	}
	public static void sottraiScortePennuto(int i) {
		sPennuto.rimuoviQuantita(i);
	}

	// aggiungo e sottraggo alle ScorteCriceto
	public static void aggiungiScorteCriceto(int i) {
		sCriceto.aggiungiQuantita(i);
	}
	public static void sottraiScorteCriceto(int i) {
		sCriceto.rimuoviQuantita(i);
	}

	// Dato il mese corrente calcola se le scorte sono sufficienti rispetto alle prenotazioni del del mese stesso
	public static boolean scorteSufficentiMese(LocalDate d) {
		LocalDate mese = d;
		int scorteTotCane = sCaneSecco.getQuantita() + sCaneUmido.getQuantita();
		int scorteTotGatto = sGattoSecco.getQuantita() + sGattoUmido.getQuantita();

		int scorteFinaliCane = scorteTotCane - GestionePensione.conteggioFabbisognoCani(mese);
		int scorteFinaliGatto = scorteTotGatto - GestionePensione.conteggioFabbisognoGatti(mese);
		int scorteFinaliPennuto = sPennuto.getQuantita() - GestionePensione.conteggioFabbisognoPennuti(mese);
		int scorteFinaliCriceto = sCriceto.getQuantita() - GestionePensione.conteggioFabbisognoCriceti(mese);

		System.out.println("scorte finali cane: "+ scorteFinaliCane + "gr; scorte finali gatto: "+ scorteFinaliGatto + "gr; scorte finali pennuto: "+ scorteFinaliPennuto + "gr; scorte finali criceto: "+ scorteFinaliCriceto+"gr");

		if (scorteFinaliCane <0 || scorteFinaliGatto <0 || scorteFinaliPennuto <0 || scorteFinaliCriceto <0 ) {
			return false;
		}else {
			
		return true; 
		}
	}
}
