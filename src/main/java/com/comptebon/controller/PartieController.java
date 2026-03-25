package com.comptebon.controller;

import com.comptebon.model.Operateur;
import com.comptebon.model.Operation;
import com.comptebon.model.Partie;
import com.comptebon.model.Plaque;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javafx.scene.layout.HBox;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.GaussianBlur;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Contrôleur responsable de l'écran principal de jeu "Le Compte est Bon".
 * 
 * Cette classe gère les interactions définies dans le fichier 'jeu.fxml',
 * permet l'enchaînement des calculs par l'utilisateur, l'affichage du tirage
 * et de la cible, ainsi que la validation de la victoire ou l'affichage d'indices.
 */
public class PartieController {

    /**
     * L'instance du modèle de Partie en cours qui contient toute la logique
     * métier (historique, plaques restantes, vérifications mathématiques).
     */
    private Partie partie;

    // --- VARIABLES D'ÉTAT DE L'IHM ---
    private Button premierBouton = null;
    private Plaque premierePlaque = null;
    private Operateur operateurSelectionne = null;
    private String messageAide = null;
    private boolean victoireAtteinte = false;

    // --- LIAISON AVEC LA VUE (Fichier jeu.fxml) ---

    @FXML
    private Label labelCible;

    @FXML
    private FlowPane conteneurPlaques;

    @FXML
    private VBox listeCalculs;

    @FXML
    private MenuItem itemSolution;

    @FXML
    private MenuItem itemIndice;

    @FXML private HBox boiteOperateurs;
    @FXML private MenuButton menuAide;
    @FXML private Button btnAnnuler;
    @FXML private Button btnReinitialiser;
    @FXML private VBox mainContainer;
    @FXML private VBox panelNouvellePartie;

    // --- MÉTHODES APPELÉES PAR LA VUE (Définies dans onAction) ---

    /**
     * Action déclenchée lorsque le joueur clique sur l'un des quatre
     * boutons d'opérations mathématiques (+, -, ×, ÷).
     */
    @FXML
    public void handleOperateurClick(ActionEvent event) {
        if (premierePlaque != null) {
            String texteBouton = ((Button) event.getSource()).getText();
            switch (texteBouton) {
                case "+":
                    operateurSelectionne = Operateur.ADDITION;
                    break;
                case "-":
                    operateurSelectionne = Operateur.SOUSTRACTION;
                    break;
                case "×":
                    operateurSelectionne = Operateur.MULTIPLICATION;
                    break;
                case "÷":
                    operateurSelectionne = Operateur.DIVISION;
                    break;
            }
        }
    }

    /**
     * Gère les clics dynamiques sur les plaques générées.
     */
    private void handlePlaqueClick(Plaque plaque, Button boutonClique) {
        if (premierePlaque == null) {
            // Premier nombre sélectionné
            premierePlaque = plaque;
            premierBouton = boutonClique;
            premierBouton.setStyle("-fx-border-color: #ffcc00; -fx-border-width: 3px; -fx-background-color: #f0f0f0;");
        } else if (operateurSelectionne != null && boutonClique != premierBouton) {
            // Deuxième nombre sélectionné : On tente le calcul métier
            Plaque p1 = premierePlaque;
            Plaque p2 = plaque;

            // L'utilisateur gère lui-même le sens de la soustraction et de la division
            // Le modèle bloquera via une ArithmeticException si <=0 ou div par 0
            
            int etapesAvant = partie.getNbEtapes();
            partie.effectuerOperation(p1, operateurSelectionne, p2);

            if (partie.getNbEtapes() > etapesAvant) {
                // Le coup était valide d'après le modèle
                messageAide = null;
                resetSelection();
                updateUI();
            } else {
                // Coup invalide (division à reste, faux bouton)
                resetSelection();
            }
        }
    }

    /**
     * Action associée au bouton "Annuler".
     */
    @FXML
    public void annulerDerniereAction() {
        if (partie != null) {
            partie.annulerDerniereOperation();
            messageAide = null;
            resetSelection();
            updateUI();
        }
    }

    /**
     * Action associée au bouton "Réinitialiser".
     */
    @FXML
    public void reinitialiserPartie() {
        if (partie != null) {
            while (!partie.getHistorique().isEmpty()) {
                partie.annulerDerniereOperation();
            }
            if (transitionVictoire != null) {
                transitionVictoire.stop();
            }
            messageAide = null;
            victoireAtteinte = false;

            if (panelNouvellePartie != null) panelNouvellePartie.setVisible(false);
            if (mainContainer != null) mainContainer.setEffect(null);
            if (boiteOperateurs != null) boiteOperateurs.setDisable(false);
            if (menuAide != null) menuAide.setDisable(false);
            if (btnAnnuler != null) btnAnnuler.setDisable(false);
            if (conteneurPlaques != null) conteneurPlaques.setDisable(false);
            messageAide = null;
            resetSelection();
            updateUI();
        }
    }

    @FXML
    public void sauvegarderPartie() {
        if (partie != null) {
            com.comptebon.util.SaveManager.save(partie);
        }
    }

    @FXML
    public void chargerPartie() {
        Partie p = com.comptebon.util.SaveManager.load();
        if (p != null) {
            victoireAtteinte = false;
            messageAide = null;
            if (transitionVictoire != null) transitionVictoire.stop();
            if (panelNouvellePartie != null) panelNouvellePartie.setVisible(false);
            if (mainContainer != null) mainContainer.setEffect(null);
            
            if (boiteOperateurs != null) boiteOperateurs.setDisable(false);
            if (menuAide != null) menuAide.setDisable(false);
            if (btnAnnuler != null) btnAnnuler.setDisable(false);
            if (btnReinitialiser != null) btnReinitialiser.setDisable(false);
            if (conteneurPlaques != null) conteneurPlaques.setDisable(false);
            
            setPartie(p);
        }
    }

    @FXML
    public void donnerIndice(ActionEvent event) {
        if (partie != null) {
            String indice = com.comptebon.util.Solveur.donnerIndice(partie);
            messageAide = "Indice : " + indice;
            updateUI();
        }
    }

    @FXML
    public void afficherSolution(ActionEvent event) {
        if (partie != null) {
            boolean possible = com.comptebon.util.Solveur.resoudreDynamique(partie.getTirage(), partie.getValeurCible());
            if (possible) {
                messageAide = "Solution :\n" + String.join("\n", com.comptebon.util.Solveur.getEtapesSolution());
                
                // Désactiver les pqaques et les boutons d'aide/annulation selon requête
                if (boiteOperateurs != null) boiteOperateurs.setDisable(true);
                if (menuAide != null) menuAide.setDisable(true);
                if (btnAnnuler != null) btnAnnuler.setDisable(true);
                if (conteneurPlaques != null) conteneurPlaques.setDisable(true);
                
            } else {
                messageAide = "Aucune solution possible avec vos plaques actuelles !";
            }
            updateUI();
        }
    }

    @FXML
    public void retourAccueil(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    public void lancerNouvellePartie(ActionEvent event) {
        if (partie != null && partie.getConfig() != null) {
            Partie nouvellePartie = new Partie(partie.getConfig());
            
            victoireAtteinte = false;
            messageAide = null;
            if (transitionVictoire != null) {
                transitionVictoire.stop();
            }
            if (panelNouvellePartie != null) panelNouvellePartie.setVisible(false);
            if (mainContainer != null) mainContainer.setEffect(null);
            
            if (boiteOperateurs != null) boiteOperateurs.setDisable(false);
            if (menuAide != null) menuAide.setDisable(false);
            if (btnAnnuler != null) btnAnnuler.setDisable(false);
            if (btnReinitialiser != null) btnReinitialiser.setDisable(false);
            if (conteneurPlaques != null) conteneurPlaques.setDisable(false);
            
            setPartie(nouvellePartie);
        } else {
            retourAccueil(event);
        }
    }

    // --- MÉTHODES MÉTIER UTILES ---

    private void resetSelection() {
        if (premierBouton != null) {
            premierBouton.setStyle("");
        }
        premierePlaque = null;
        premierBouton = null;
        operateurSelectionne = null;
    }

    private javafx.animation.PauseTransition transitionVictoire;

    private void verifierVictoire() {
        if (victoireAtteinte) return;

        boolean victory = false;
        for (Plaque p : partie.getTirage()) {
            if (p.isDisponible() && p.getValeur() == partie.getValeurCible()) {
                victory = true;
                break;
            }
        }

        if (victory) {
            victoireAtteinte = true;
            labelCible.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 48px; -fx-padding: 10 30;");
            
            if (boiteOperateurs != null) boiteOperateurs.setDisable(true);
            if (menuAide != null) menuAide.setDisable(true);
            if (btnAnnuler != null) btnAnnuler.setDisable(true);
            // On DOIT laisser btnReinitialiser activé selon la demande
            
            if (conteneurPlaques != null) conteneurPlaques.setDisable(true);
            
            transitionVictoire = new PauseTransition(Duration.seconds(10));
            transitionVictoire.setOnFinished(e -> {
                if (mainContainer != null) mainContainer.setEffect(new GaussianBlur(10));
                if (panelNouvellePartie != null) panelNouvellePartie.setVisible(true);
            });
            transitionVictoire.play();
        } else {
            labelCible.setStyle("-fx-background-color: #d7cfe8; -fx-text-fill: black; -fx-font-size: 48px; -fx-padding: 10 30;");
        }
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
        updateUI();
    }

    /**
     * Synchronise intégralement l'interface graphique FXML avec
     * l'état actuel de l'objet Partie.
     */
    public void updateUI() {
        if (partie == null)
            return;

        // 1. Mise à jour de la cible
        labelCible.setText(String.valueOf(partie.getValeurCible()));

        // 2. Affichage dynamique des plaques
        conteneurPlaques.getChildren().clear();
        for (Plaque plaque : partie.getTirage()) {
            if (plaque.isDisponible()) {
                Button btn = new Button(String.valueOf(plaque.getValeur()));
                btn.getStyleClass().add("plate-button");
                btn.setOnAction(e -> handlePlaqueClick(plaque, btn));
                conteneurPlaques.getChildren().add(btn);
            }
        }

        // 3. Affichage de l'historique de jeu
        listeCalculs.getChildren().clear();
        for (Operation op : partie.getHistorique()) {
            Label lbl = new Label(op.toString());
            lbl.setStyle("-fx-font-size: 18px; -fx-text-fill: #2e1a4f;");
            listeCalculs.getChildren().add(lbl);
        }

        if (messageAide != null) {
            Label lblAide = new Label(messageAide);
            lblAide.setStyle("-fx-font-size: 16px; -fx-text-fill: #f57c00; -fx-background-color: #ffe0b2; -fx-padding: 5 15; -fx-background-radius: 5;");
            listeCalculs.getChildren().add(lblAide);
        }

        // 4. Vérification d'une potentielle victoire au dernier coup
        verifierVictoire();
    }
}
