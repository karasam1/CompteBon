package com.comptebon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SauvegardePartie implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cible;
    private List<Integer> plaquesInitiales;
    private List<Integer> plaquesActuelles;
    private List<String> historiqueCalculs;

    // Constructeur
    public SauvegardePartie(int cible, List<Integer> initiales, List<Integer> actuelles, List<String> calculs) {
        this.cible = cible;
        this.plaquesInitiales = new ArrayList<>(initiales);
        this.plaquesActuelles = new ArrayList<>(actuelles);
        this.historiqueCalculs = new ArrayList<>(calculs);
    }

    // --- LES GETTERS (Obligatoires pour que le Contrôleur puisse lire les données) ---

    public int getCible() {
        return cible;
    }

    public List<Integer> getPlaquesInitiales() {
        return plaquesInitiales;
    }

    public List<Integer> getPlaquesActuelles() {
        return plaquesActuelles;
    }

    public List<String> getHistoriqueCalculs() {
        return historiqueCalculs;
    }
}