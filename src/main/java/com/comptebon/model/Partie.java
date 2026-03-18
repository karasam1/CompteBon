package com.comptebon.model;

import java.util.ArrayList;
import java.util.Stack;

import com.comptebon.model.Operation;
import com.comptebon.model.Plaque;

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
        int[] tuilesPossibles = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 25, 50, 75, 100 };
        java.util.Random random = new java.util.Random();

        int nbTotal = tuilesPossibles.length;
        java.util.List<Integer> tuilesList = new java.util.ArrayList<>();
        for (int i = 0; i < config.getNbPlaque(); i++) {
            int index = random.nextInt(nbTotal);
            int val = tuilesPossibles[index];
            tuilesList.add(val);
            tirage.add(new Plaque(val));
            tuilesPossibles[index] = tuilesPossibles[nbTotal - 1]; // replace with last element
            nbTotal--;
        }

        // generer une cible atteignable si possible
        java.util.List<Integer> valeursCourantes = new java.util.ArrayList<>(tuilesList);
        int nbOperations = random.nextInt(config.getNbPlaque());
        Operateur[] operateurs = Operateur.values();

        for (int i = 0; i < nbOperations; i++) {
            if (valeursCourantes.size() < 2)
                break;
            int idx1 = random.nextInt(valeursCourantes.size());
            int val1 = valeursCourantes.remove(idx1);
            int idx2 = random.nextInt(valeursCourantes.size());
            int val2 = valeursCourantes.remove(idx2);

            // Permutation pour que val1 >= val2
            if (val2 > val1) {
                int temp = val1;
                val1 = val2;
                val2 = temp;
            }

            Operateur op = operateurs[random.nextInt(operateurs.length)];
            try {
                op.valider(val1, val2);
                valeursCourantes.add(op.calculer(val1, val2));
            } catch (ArithmeticException e) {
                valeursCourantes.add(val1);
                valeursCourantes.add(val2);
            }
        }

        int cibleProposee = valeursCourantes.get(random.nextInt(valeursCourantes.size()));
        if (cibleProposee >= config.getPlageCibleMin() && cibleProposee <= config.getPlageCibleMax()) {
            this.valeurCible = cibleProposee;
        } else {
            this.valeurCible = config.getPlageCibleMin()
                    + random.nextInt(config.getPlageCibleMax() - config.getPlageCibleMin() + 1);
        }

        this.nbEtapes = 0;
    }

    public void effectuerOperation(Plaque p1, Operateur op, Plaque p2) {
        try {

            // 1. On laisse l'opérateur vérifier si le coup est légal (géré dans operation)
            op.valider(p1.getValeur(), p2.getValeur());

            // 2. On calcule le résultat
            int resValeur = op.calculer(p1.getValeur(), p2.getValeur());

            // 3. On crée la nouvelle plaque et on met à jour l'historique...
            p1.setDisponible(false);
            p2.setDisponible(false);

            Plaque pRes = new Plaque(resValeur);
            tirage.add(pRes);
            historique.push(new Operation(p1, p2, op));
            nbEtapes++;

        } catch (ArithmeticException e) {
            // 4. Gestion propre de l'erreur pour l'IHM
            afficherAlerte(e.getMessage());
        }
    }

    public void annulerDerniereOperation() {
        // si non vide alors
        if (!historique.isEmpty()) {
            Operation op = historique.pop();

            // on remet les plaques d'origine à disponible
            op.getP1().setDisponible(true);
            op.getP2().setDisponible(true);

            // on retire la plaque resultat
            op.getResultat().setDisponible(false);
            tirage.remove(op.getResultat());
            nbEtapes++;
        }
    }

    private void afficherAlerte(String message) {
        // message d'erreur pour l'utilisateur dans l'interface
    }
}
