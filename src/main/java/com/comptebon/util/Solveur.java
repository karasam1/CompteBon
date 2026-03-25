package com.comptebon.util;

import java.util.ArrayList;
import java.util.List;

import com.comptebon.model.Partie;
import com.comptebon.model.Plaque;

/**
 * Utilitaire contenant l'algorithme de résolution du jeu "Le Compte est Bon".
 *
 * Cette classe offre des méthodes statiques permettant de déterminer si un tirage
 * peut atteindre une cible donnée en explorant toutes les combinaisons possibles
 * via un algorithme de retour sur trace (Backtracking). 
 */
public class Solveur {

    private static java.util.List<String> etapesSolution = new java.util.ArrayList<>();

    // Classe utilitaire : constructeur privé pour empêcher l'instanciation
    private Solveur() {
        throw new IllegalStateException("Utility class");
    }

    public static java.util.List<String> getEtapesSolution() {
        return etapesSolution;
    }

    /**
     * Point d'entrée "propre" pour vérifier la solvabilité d'une partie.
     * 
     * Il s'interface directement avec l'objetmétier "Partie", extrait le tirage
     * et la cible, puis délègue le calcul complexe à la méthode récursive surchargée.
     * 
     * @param partie L'objet Partie dont on veut vérifier la solvabilité.
     * @return true si une solution exacte existe, false sinon.
     */
    public static boolean resoudre(Partie partie) {
        etapesSolution.clear();
        return resoudre(partie.getTirage(), partie.getValeurCible());
    }

    /**
     * Surcharge statique pour relancer un calcul dynamiquement sans l'objet Partie complet.
     */
    public static boolean resoudreDynamique(List<Plaque> plaques, int cible) {
        etapesSolution.clear();
        return resoudre(plaques, cible);
    }

    /**
     * Cœur de l'algorithme : méthode de résolution récursive par Backtracking.
     * 
     * Elle teste systématiquement toutes les paires de plaques disponibles, applique
     * les 4 opérations mathématiques possibles, et s'appelle elle-même en réduisant
     * progressivement les plaques jusqu'à trouver la cible exacte ou aboutir à une impasse.
     * 
     * @param plaques La liste des plaques actuellement disponibles pour le calcul.
     * @param cible La valeur numérique finale à retrouver.
     * @return true si une combinaison d'opérations aboutit à la cible, false sinon.
     */
    public static boolean resoudre(List<Plaque> plaques, int cible) {
        // 1. Condition d'arrêt (succès) : on a réussi à fabriquer la cible
        for (Plaque plaque : plaques) {
            if (plaque.getValeur() == cible) {
                return true;
            }
        }

        // 2. Condition d'arrêt (échec) : si moins de 2 plaques, aucun calcul n'est possible
        if (plaques.size() < 2) {
            return false;
        }

        // 3. Parcourir toutes les paires possibles de plaques (Plaque A et Plaque B)
        for (int i = 0; i < plaques.size(); i++) {
            for (int j = 0; j < plaques.size(); j++) {
                if (i == j)
                    continue; // Empêche d'utiliser la "même" plaque physiquement deux fois

                int a = plaques.get(i).getValeur();
                int b = plaques.get(j).getValeur();

                // On construit le reste des plaques que l'on n'a pas touchées pour ce calcul
                List<Plaque> reste = new ArrayList<>();
                for (int k = 0; k < plaques.size(); k++) {
                    if (k != i && k != j) {
                        reste.add(plaques.get(k));
                    }
                }

                // 4. Lancement des vérifications des branches d'algorithmes (Addition, Soustraction, etc.)
                if (tenter(a, b, a + b, "+", reste, cible))
                    return true;
                if (a - b > 0 && tenter(a, b, a - b, "-", reste, cible))
                    return true;
                if (a * b > 1 && tenter(a, b, a * b, "×", reste, cible))
                    return true;
                if (b != 0 && a % b == 0 && tenter(a, b, a / b, "÷", reste, cible))
                    return true;
            }
        }

        // Si l'exploration complète ne donne rien, cette branche est un échec.
        return false;
    }

    /**
     * Construit temporairement une nouvelle liste de plaques afin de descendre
     * dans l'arborescence des calculs récursifs avec le résultat de l'opération qui 
     * vient d'être testée.
     * 
     * @param a Opérande gauche
     * @param b Opérande droit
     * @param res Le résultat du précédent calcul entre Plaque A et Plaque B.
     * @param op Le texte de l'opération concernée pour afficher la solution.
     * @param reste Les plaques inutilisées dans le calcul.
     * @param cible La valeur cible globale visée.
     * @return true si cette nouvelle branche mène vers la cible, false sinon.
     */
    private static boolean tenter(int a, int b, int res, String op, List<Plaque> reste, int cible) {
        String calculStr = a + " " + op + " " + b + " = " + res;
        etapesSolution.add(calculStr);

        List<Plaque> suivante = new ArrayList<>(reste);
        suivante.add(new Plaque(res));
        
        // Appel récursif qui descend d'un étage
        if (resoudre(suivante, cible)) {
            return true;
        }

        // Si échec complet en descendant, on retire notre calcul de la liste
        etapesSolution.remove(etapesSolution.size() - 1);
        return false;
    }

    /**
     * Optionnel pour l'instant : Analysera la situation présente via une nouvelle variation 
     * de "resoudre()" afin d'afficher à l'utilisateur le prochain calcul salvateur à effectuer.
     * 
     * @param partie La partie en cours nécessitant un indice.
     */
    public static String donnerIndice(Partie partie) {
        etapesSolution.clear();
        boolean resolvable = resoudre(partie.getTirage(), partie.getValeurCible());
        if (resolvable && !etapesSolution.isEmpty()) {
            return "Indice : " + etapesSolution.get(0);
        } else {
            return "Aucune solution trouvée avec le tirage actuel. Pensez à annuler !";
        }
    }
}
