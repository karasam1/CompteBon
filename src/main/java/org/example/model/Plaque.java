package org.example.model;

import org.example.util.Constants;

public class Plaque {
    private final int id;
    private static int lastId = Constants.INIT_PLAQUE_ID;
    private final int valeur;

    public Plaque(int valeur) {
        this.id = lastId++;
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return "Plaque " + id + ": " + String.valueOf(valeur);
    }
}
