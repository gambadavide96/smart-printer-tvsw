package printer;

import java.util.Objects;

public class Utente {
    private String nome;
    private int badgeId;
    private int pin;
    private int credito;

    public Utente(String nome, int badgeId, int pin, int credito) {
        this.nome = nome;
        this.badgeId = badgeId;
        this.pin = pin;
        this.credito = credito;
    }

    public /*@ pure @*/ int getBadgeId() {
        return badgeId;
    }

    public boolean verificaPin(int inputPin) {
        return this.pin == inputPin;
    }

    public /*@ pure @*/ boolean haCreditoSufficiente() {
        return this.credito >= 50;
    }

    public void scalaCredito(int amount) {
        this.credito -= amount;
    }

    public void rimborsaCredito(int amount) {
        this.credito += amount;
    }

    public /*@ pure @*/ int getCredito() {
        return credito;
    }

    public /*@ pure @*/ String getNome() {
        return nome;
    }
    
    @Override
    public /*@ pure @*/ boolean equals(Object obj) {
    	if (this == obj) 
    		return true;
    	
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        
        Utente u = (Utente) obj;
        return badgeId == u.badgeId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(badgeId);
    }
}
