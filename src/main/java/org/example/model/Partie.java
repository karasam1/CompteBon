package org.example.model;

import java.util.Stack;

public class Partie {
    private int valeurCible;
    private int nbEtapes;
    private Config config;
    // est ce cela est pertinant de la rendre utilitaire car config
    private Tirage tirage;
    private Stack<TirageMemento> historique;

    public Partie(Config config) {
        this.config = config;
        // Le tirage serait initialisé ici ou passé en paramètre
    }

    public void demarrerNouvellePartie() {
        // Logique pour générer un tirage selon la config
    }

    public Config getConfig() {
        return config;
    }

    public Tirage getTirage() {
        return tirage;
    }

    public void setTirage(Tirage tirage) {
        this.tirage = tirage;
    }
}
