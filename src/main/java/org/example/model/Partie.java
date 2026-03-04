package org.example.model;

import java.util.ArrayList;
import java.util.Stack;
import org.example.model.Operation;
import org.example.model.Plaque;

public class Partie {
    private int valeurCible;
    private int nbEtapes;
    private Config config;
    // le chonometre si option chrono est active
    private ArrayList<Plaque> tirage;
    // supression de la classe Tirage car elle represente seulement une liste
    // deplaques
    private Stack<Operation> historique;

    public Partie(Config config) {
        this.config = config;
        initialiserTirage();
        this.historique = new Stack<>();

    }

    public Config getConfig() {
        return config;
    }

    public ArrayList<Plaque> getTirage() {
        return tirage;
    }

    public Stack<Operation> getHistorique() {
        return historique;
    }

    public int getValeurCible() {
        return valeurCible;
    }

    public int getNbEtapes() {
        return nbEtapes;
    }

    private void initialiserTirage() {
        this.tirage = new ArrayList<>();
        // mecanisme de tirage pour remplir les plaques disponibles selon la config
        // avec un algorithme de génération de nombres aléatoires dans la plage définie
        // par la config
        // et respectant les contraintes de nombre de plaques et de valeurs cibles.
        // il doit donc etre possible depuis notre cible genere un tirage qui permet
        // d'atteindre la clible en respectant les regles du jeu.
    }

    public void effectuerOperation(Plaque p1, Operateur op, Plaque p2) {
        try {
            // 1. On laisse l'opérateur vérifier si le coup est légal (géré dans operation)
            // 2. On calcule le résultat
            // 3. On crée la nouvelle plaque et on met à jour l'historique...

        } catch (ArithmeticException e) {
            // 4. Gestion propre de l'erreur pour l'IHM
            afficherAlerte(e.getMessage());
        }
    }

    public void annulerDerniereOperation() {
        // si non vide alors
        // on retire l'operation
        // on remet les plaques d'origine à disponible
        // on retire la plaque resultat
    }

    private void afficherAlerte(String message) {
    }
}
