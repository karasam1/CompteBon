package com.comptebon.model;

import java.util.ArrayList;
import java.util.Stack;

import com.comptebon.model.Operation;
import com.comptebon.model.Plaque;

/**
 * Modèle principal représentant une "Partie" du jeu Le Compte est Bon.
 * 
 * Cette classe agit comme l'état central d'une session de jeu. Elle contient
 * toutes les données en temps réel : la cible numérique à atteindre,
 * les plaques disponibles (le tirage en cours), les paramètres initiaux (via Config), 
 * ainsi que l'historique complet des coups effectués par le joueur.
 */
public class Partie {
    
    /** La valeur cible exacte générée aléatoirement que le joueur doit trouver. */
    private int valeurCible;
    
    /** Le nombre de calculs (ou étapes) effectués depuis le début de la partie. */
    private int nbEtapes;
    
    /** L'objet contenant les paramètres de jeu (nombre de plaques, cibles min/max). */
    private Config config;
    
    // Le chronomètre sera rajouté ici si l'option chrono est active
    
    /** 
     * Liste représentant les plaques actuellement sur la table de jeu.
     * Les résultats des calculs s'ajoutent à cette liste au fur et à mesure.
     */
    private ArrayList<Plaque> tirage;
    
    /** 
     * Pile mémorisant chronologiquement les opérations mathématiques validées.
     * Cette structure (Stack) est idéale pour implémenter un bouton "Annuler" (dépiler).
     */
    private Stack<Operation> historique;

    /**
     * Crée une nouvelle Partie prête à être jouée en se basant sur une configuration.
     * 
     * @param config Les paramètres (nombre de plaques et limites de cibles) choisis par le joueur.
     */
    public Partie(Config config) {
        this.config = config;
        this.historique = new Stack<>();
        initialiserTirage();
    }

    /**
     * Crée une nouvelle Partie en restaurant des données existantes (pour la sauvegarde).
     */
    public Partie(Config config, int valeurCible, java.util.List<Integer> plaquesVal) {
        this.config = config;
        this.valeurCible = valeurCible;
        this.historique = new Stack<>();
        this.nbEtapes = 0;
        this.tirage = new java.util.ArrayList<>();
        for (int v : plaquesVal) {
            this.tirage.add(new Plaque(v));
        }
    }

    /**
     * @return Les paramètres de configuration en vigueur pour cette partie.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * @return La liste des objets Plaque actuellement utilisables ou en jeu.
     */
    public ArrayList<Plaque> getTirage() {
        return tirage;
    }

    /**
     * @return L'historique sous forme de pile (LIFO) de toutes les opérations jouées.
     */
    public Stack<Operation> getHistorique() {
        return historique;
    }

    /**
     * @return Le nombre exact que le joueur doit recomposer avec ses plaques.
     */
    public int getValeurCible() {
        return valeurCible;
    }

    /**
     * @return Le nombre d'opérations exécutées jusqu'à présent.
     */
    public int getNbEtapes() {
        return nbEtapes;
    }

    /**
     * Génère le plateau de départ : tire aléatoirement les plaques et définit une cible.
     * Fait appel par magie au Solveur pour certifier dès le début que les conditions
     * tirées ont bien au moins une solution mathématique possible. Tant qu'il n'y a pas de
     * solution, le tirage est refait silencieusement dans une boucle.
     */
    private void initialiserTirage() {
        // Jeu de plaques de base, en évitant de repiocher trop de grandes valeurs.
        int[] tuilesPossibles = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 25, 50, 75, 100 };
        java.util.Random random = new java.util.Random();

        boolean solvable = false;

        do {
            this.tirage = new java.util.ArrayList<>();

            // On prépare une liste de tuiles disponibles pour ce tirage temporaire
            java.util.List<Integer> tuilesDispo = new java.util.ArrayList<>();
            for (int val : tuilesPossibles) {
                tuilesDispo.add(val);
            }

            // 1. Générer des plaques de manière aléatoire selon les régles (Config)
            for (int i = 0; i < config.getNbPlaque(); i++) {
                int index = random.nextInt(tuilesDispo.size());
                int val = tuilesDispo.remove(index);
                this.tirage.add(new Plaque(val));
            }

            // 2. Générer une valeur cible dans les limites voulues
            int min = config.getPlageCibleMin();
            int max = config.getPlageCibleMax();
            this.valeurCible = random.nextInt((max - min) + 1) + min;

            // 3. Vérifier rigoureusement si cette configuration est bien résolvable
            solvable = com.comptebon.util.Solveur.resoudre(this);

        } while (!solvable);

        this.nbEtapes = 0;
    }

    /**
     * Tente de réaliser une opération mathématique complète entre deux plaques 
     * selon un opérateur spécifique choisi par le joueur.
     *
     * Le comportement gère de multiples conséquences métiers (vérification de la division par 0,
     * création de la nouvelle plaque, mise dans l'historique et indisponibilité des anciennes plaques).
     * 
     * @param p1 La première plaque sélectionnée.
     * @param op L'opérateur voulu (ex: Operateur.ADDITION).
     * @param p2 La deuxième plaque sélectionnée.
     */
    public void effectuerOperation(Plaque p1, Operateur op, Plaque p2) {
        try {

            // 1. On laisse l'opérateur vérifier si le coup est légal ou mathématiquement valable
            op.valider(p1.getValeur(), p2.getValeur());

            // 2. On laisse Operation s'occuper de calculer le résultat
            // 3. On verrouille (indisponibles) les deux plaques sources qui viennent d'être consommées
            p1.setDisponible(false);
            p2.setDisponible(false);

            // On injecte le nouveau produit de l'opération dans le jeu courant (et l'historique)
            Operation nouvelleOp = new Operation(p1, p2, op);
            tirage.add(nouvelleOp.getResultat());
            historique.push(nouvelleOp);
            nbEtapes++;

        } catch (ArithmeticException e) {
            // 4. Gestion propre de l'erreur mathématique (exprimée sans crasher l'application)
            System.err.println(e.getMessage());
        }
    }

    /**
     * Dépile la toute dernière opération réalisée par le joueur pour annuler ses effets.
     * 
     * Cette méthode restaure l'accessibilité des deux plaques mères utilisées
     * lors du calcul et détruit purement et simplement la plaque résultat qui en a découlé.
     */
    public void annulerDerniereOperation() {
        if (!historique.isEmpty()) {
            Operation op = historique.pop();

            // Rendre à nouveau sélectionnables les deux plaques sources de l'opération
            op.getP1().setDisponible(true);
            op.getP2().setDisponible(true);

            // Supprimer définitivement du "plateau de jeu" la tuile qui portait le résultat de l'erreur 
            op.getResultat().setDisponible(false);
            tirage.remove(op.getResultat());
            nbEtapes++; // L'annulation compte généralement comme une "action" d'étape dans nos décomptes
        }
    }

}
