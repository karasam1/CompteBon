package org.example.model;

public class Plaque {
    private final int id;
    private final int valeur;
    private boolean disponible;

    private static int lastId = 0;

    public Plaque(int valeur) {
        this.id = lastId++;
        this.valeur = valeur;
        this.disponible = true;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
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
