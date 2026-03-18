package com.comptebon.model;

public class Operation {
    private final Plaque p1;
    private final Plaque p2;
    private final Operateur operateur;
    private Plaque resultat;

    public Operation(Plaque p1, Plaque p2, Operateur operateur) {
        this.p1 = p1;
        this.p2 = p2;
        this.operateur = operateur;
        setResultat();
    }

    public Plaque getP1() {
        return p1;
    }

    public Plaque getP2() {
        return p2;
    }

    private void setResultat() {
        this.resultat = new Plaque(this.operateur.calculer(this.p1.getValeur(), this.p2.getValeur()));
    }

    public Operateur getOperateur() {
        return operateur;
    }

    public Plaque getResultat() {
        return resultat;
    }

    @Override
    public String toString() {
        return p1.getValeur() + " " + operateur.getSymbole() + " " + p2.getValeur() + " = " + resultat.getValeur();
    }
}
