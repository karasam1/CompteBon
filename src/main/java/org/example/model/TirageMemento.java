package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class TirageMemento {
    private final List<Integer> etat;

    public TirageMemento(List<Integer> etat) {
        this.etat = new ArrayList<>(etat);
    }

    public List<Integer> getEtat() {
        return new ArrayList<>(etat);
    }
}
