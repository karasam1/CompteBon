package com.comptebon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoteurJeu {
    private List<Integer> plaquesPossibles = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 50, 75, 100);
    // RESTRICTION SUR LES PLAQUES POSSIBLES
    private Random random = new Random();
    private List<String> etapesSolution = new ArrayList<>();

    public Tirage initialiserNouveauTirage(int nbPlaques, int min, int max) {
        List<Integer> plaques;
        int cible;
        boolean trouve;
        do {
            etapesSolution.clear();
            plaques = tirerPlaques(nbPlaques);
            cible = random.nextInt((max - min) + 1) + min;
            trouve = resoudre(new ArrayList<>(plaques), cible);
        } while (!trouve);

        return new Tirage(plaques, cible);
    }

    /**
     * NOUVEAUTÉ : Tente de résoudre le problème à partir de l'état actuel de
     * l'utilisateur.
     * Retourne la première étape de la nouvelle solution trouvée.
     */
    public String obtenirIndiceDynamique(List<Integer> plaquesActuelles, int cible) {
        etapesSolution.clear(); // On efface l'ancienne solution globale
        boolean possible = resoudre(new ArrayList<>(plaquesActuelles), cible);

        if (possible && !etapesSolution.isEmpty()) {
            return "Indice : " + etapesSolution.get(0); // On donne le premier pas du nouveau chemin
        } else {
            return "Désolé, aucune solution n'est possible avec vos plaques actuelles. Essayez d'annuler !";
        }
    }

    private boolean resoudre(List<Integer> nombres, int cible) {
        if (nombres.contains(cible))
            return true;
        if (nombres.size() < 2)
            return false;

        for (int i = 0; i < nombres.size(); i++) {
            for (int j = 0; j < nombres.size(); j++) {
                if (i == j)
                    continue;

                int a = nombres.get(i);
                int b = nombres.get(j);

                List<Integer> reste = new ArrayList<>();
                for (int k = 0; k < nombres.size(); k++) {
                    if (k != i && k != j)
                        reste.add(nombres.get(k));
                }

                if (tenter(a, b, a + b, "+", reste, cible))
                    return true;
                if (a - b > 0 && tenter(a, b, a - b, "-", reste, cible))
                    return true;
                if (a * b > 1 && tenter(a, b, a * b, "*", reste, cible))
                    return true;
                if (b != 0 && a % b == 0 && tenter(a, b, a / b, "/", reste, cible))
                    return true;
            }
        }
        return false;
    }

    private boolean tenter(int a, int b, int res, String op, List<Integer> reste, int cible) {
        String calcul = a + " " + op + " " + b + " = " + res;
        etapesSolution.add(calcul);

        if (res == cible)
            return true;

        List<Integer> suivante = new ArrayList<>(reste);
        suivante.add(res);

        if (resoudre(suivante, cible))
            return true;

        etapesSolution.remove(etapesSolution.size() - 1);
        return false;
    }

    private List<Integer> tirerPlaques(int nombre) {
        List<Integer> tirage = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            tirage.add(plaquesPossibles.get(random.nextInt(plaquesPossibles.size())));
        }
        return tirage;
    }

    public String getDerniereSolution() {
        return etapesSolution.isEmpty() ? "Aucune solution trouvée" : String.join("\n", etapesSolution);
    }

    public static record Tirage(List<Integer> plaques, int cible) {
    }
}