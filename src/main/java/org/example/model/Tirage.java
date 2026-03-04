package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tirage {
    private List<Plaque> nombresDisponibles;
    private int cible;

    public Tirage(List<Plaque> nombres, int cible) {
        this.nombresDisponibles = new ArrayList<>(nombres);
        this.cible = cible;
    }

    public List<Plaque> getNombresDisponibles() {
        return Collections.unmodifiableList(nombresDisponibles);
    }

    public int getCible() {
        return cible;
    }

}
