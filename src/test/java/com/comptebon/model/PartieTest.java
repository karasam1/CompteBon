package com.comptebon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.comptebon.model.Config;
import com.comptebon.model.Operateur;
import com.comptebon.model.Partie;
import com.comptebon.model.Plaque;

import static org.junit.jupiter.api.Assertions.*;

public class PartieTest {

    private Config config = new Config(6, 100, 999);

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testInitialiserTirage() {
        Partie partie = new Partie(config);

        assertEquals(6, partie.getTirage().size(), "Le tirage doit comporter 6 plaques");
        int valeurCible = partie.getValeurCible();
        assertTrue(valeurCible >= 100 && valeurCible <= 999, "La valeur cible doit être dans la plage configurée");
        assertTrue(partie.getHistorique().isEmpty(), "L'historique doit être vide au départ");
        assertEquals(0, partie.getNbEtapes(), "Le nombre d'étapes doit être initialisé à 0");
    }

    @Test
    public void testEffectuerOperation() {
        Partie partie = new Partie(config);
        Plaque p1 = new Plaque(10);
        Plaque p2 = new Plaque(5);

        // Ajout temporaire pour pouvoir utiliser
        partie.getTirage().add(p1);
        partie.getTirage().add(p2);

        int tailleAvant = partie.getTirage().size();

        partie.effectuerOperation(p1, Operateur.ADDITION, p2);

        assertFalse(p1.isDisponible(), "La plaque p1 ne doit plus être disponible");
        assertFalse(p2.isDisponible(), "La plaque p2 ne doit plus être disponible");

        assertEquals(tailleAvant + 1, partie.getTirage().size(),
                "On ajoute la nouvelle plaque au tirage, donc la taille augmente de 1");

        assertEquals(1, partie.getHistorique().size(), "L'historique doit contenir 1 opération");
        assertEquals(15, partie.getHistorique().peek().getResultat().getValeur(),
                "La valeur du résultat doit être correcte (10 + 5 = 15)");
        assertEquals(1, partie.getNbEtapes(), "Le nombre d'étapes doit passer à 1");
    }

    @Test
    public void testAnnulerDerniereOperation() {
        Partie partie = new Partie(config);
        Plaque p1 = new Plaque(20);
        Plaque p2 = new Plaque(4);

        partie.getTirage().add(p1);
        partie.getTirage().add(p2);

        partie.effectuerOperation(p1, Operateur.DIVISION, p2);

        assertFalse(p1.isDisponible(), "p1 n'est plus disponible");
        Plaque pRes = partie.getHistorique().peek().getResultat();

        int nbEtapesAvant = partie.getNbEtapes();

        partie.annulerDerniereOperation();

        assertTrue(p1.isDisponible(), "p1 redevient disponible");
        assertTrue(p2.isDisponible(), "p2 redevient disponible");
        assertTrue(partie.getHistorique().isEmpty(), "L'historique doit être vide après annulation");
        assertEquals(nbEtapesAvant + 1, partie.getNbEtapes(), "Le nombre d'étapes doit décroître");
    }

    @Test
    public void testOperationInvalide() {
        Partie partie = new Partie(config);
        Plaque p1 = new Plaque(5);
        Plaque p2 = new Plaque(10);

        partie.getTirage().add(p1);
        partie.getTirage().add(p2);

        int tailleHistoriqueAvant = partie.getHistorique().size();

        // Soustraction impossible si résultat négatif, l'effectuerOperation peut
        // parfois les intervertir ou déclencher Exception
        // Le comportement actuel intervertit, donc 10 - 5 = 5 (ça va marcher).
        // Essayons une division non entière 10 / 5 (marche), ou 5 / 10 -> 10 / 5
        // (marche).
        partie.effectuerOperation(new Plaque(10), Operateur.DIVISION, new Plaque(3));

        // Une exception est attrapée en interne, l'historique ne doit pas changer
        assertEquals(tailleHistoriqueAvant, partie.getHistorique().size(),
                "L'historique ne doit pas grandir lors d'une opération invalide");
    }

    @Test
    public void operationvalide() {
        Partie partie = new Partie(config);
        Plaque p1 = new Plaque(10);
        Plaque p2 = new Plaque(5);

        partie.getTirage().add(p1);
        partie.getTirage().add(p2);

        partie.effectuerOperation(p1, Operateur.ADDITION, p2);

        assertFalse(p1.isDisponible(), "p1 n'est plus disponible");
        assertFalse(p2.isDisponible(), "p2 n'est plus disponible");
        assertEquals(1, partie.getHistorique().size(), "L'historique doit contenir 1 opération");
        assertEquals(15, partie.getHistorique().peek().getResultat().getValeur(),
                "La valeur du résultat doit être correcte (10 + 5 = 15)");
        assertEquals(1, partie.getNbEtapes(), "Le nombre d'étapes doit passer à 1");
    }

}
