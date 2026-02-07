package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tirage {
    private List<Integer> nombresDisponibles;
    private int cible;

    public Tirage(List<Integer> nombres, int cible) {
        this.nombresDisponibles = new ArrayList<>(nombres);
        this.cible = cible;
    }

    public List<Integer> getNombresDisponibles() {
        return Collections.unmodifiableList(nombresDisponibles);
    }

    public int getCible() {
        return cible;
    }

    // Pattern Memento : Sauvegarder l'état
    public TirageMemento sauvegarder() {
        return new TirageMemento(new ArrayList<>(nombresDisponibles));
    }

    // Pattern Memento : Restaurer l'état
    public void restaurer(TirageMemento memento) {
        this.nombresDisponibles = memento.getEtat();
    }
    
    // Méthodes métier pour manipuler le tirage (ex: effectuer une opération)
    public void utiliserNombres(int n1, int n2, int resultat) {
        nombresDisponibles.remove(Integer.valueOf(n1));
        nombresDisponibles.remove(Integer.valueOf(n2));
        nombresDisponibles.add(resultat);
        // Trier ou organiser si nécessaire
    }
}
