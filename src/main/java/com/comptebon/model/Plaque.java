package com.comptebon.model;

public class Plaque {
    private final int valeur;
    private boolean disponible;

    public Plaque(int valeur) {
        this.valeur = valeur;
        this.disponible = true;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return "Plaque : " + valeur;
    }
}
