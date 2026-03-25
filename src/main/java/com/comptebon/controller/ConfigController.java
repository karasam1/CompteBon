package com.comptebon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * Contrôleur responsable de l'écran de configuration avant le début de la
 * partie.
 * 
 * Cette classe gère les interactions définies dans le fichier 'parametres.fxml',
 * telles que la modification des paramètres (nombre de plaques, valeurs cibles)
 * et la navigation vers l'écran de jeu via les boutons.
 */
public class ConfigController {

    @FXML private Slider sliderPlaques;
    @FXML private Label lblNbPlaques;
    @FXML private TextField txtCibleMin;
    @FXML private TextField txtCibleMax;
    @FXML private ToggleButton btnChrono;

    @FXML
    public void initialize() {
        if (sliderPlaques != null && lblNbPlaques != null) {
            sliderPlaques.valueProperty().addListener((observable, oldValue, newValue) -> {
                lblNbPlaques.setText(String.valueOf(newValue.intValue()));
            });
            lblNbPlaques.setText(String.valueOf((int) sliderPlaques.getValue()));
        }
    }

    @FXML
    private void handleChrono(ActionEvent event) {
        if (btnChrono != null) {
            if (btnChrono.isSelected()) {
                btnChrono.setText("ON");
                btnChrono.setStyle("-fx-background-radius: 20; -fx-padding: 5 15; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            } else {
                btnChrono.setText("OFF");
                btnChrono.setStyle("-fx-background-radius: 20; -fx-padding: 5 15; -fx-font-weight: bold; -fx-background-color: #f44336; -fx-text-fill: white;");
            }
        }
    }

    @FXML
    public void retourAccueil(ActionEvent event) {
        try {
            com.comptebon.App.setRoot("accueil"); // Retourne virtuellement sur un menu principal
        } catch (IOException e) {
            System.err.println("Aucun accueil.fxml trouvé ou erreur de chargement.");
        }
    }

    /**
     * Action déclenchée par l'utilisateur lorsqu'il clique sur "Demarrer" (bouton lancerJeu).
     *
     * Valide les paramètres, crée la Configuration puis le modèle Partie,
     * et injecte ce dernier dans le contrôleur de jeu en chargeant la bonne vue.
     */
    @FXML
    public void lancerJeu(ActionEvent event) {
        try {
            // 1. Lire les paramètres depuis l'interface
            int nbPlaques = (int) sliderPlaques.getValue();
            int min = Integer.parseInt(txtCibleMin.getText());
            int max = Integer.parseInt(txtCibleMax.getText());
            
            // 2. Créer l'objet métier Configuration et initialiser la Partie
            com.comptebon.model.Config config = new com.comptebon.model.Config(nbPlaques, min, max);
            com.comptebon.model.Partie partie = new com.comptebon.model.Partie(config);
            
            System.out.println("Paramètres sauvegardés, changement de vue vers 'jeu.fxml'");
            
            // 3. Charger le FXML du plateau de jeu
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(com.comptebon.App.class.getResource("jeu.fxml"));
            javafx.scene.Parent root = loader.load();
            
            // 4. Récupérer le contrôleur instancié par JavaFX et lui injecter la partie
            PartieController controller = loader.getController();
            controller.setPartie(partie);
            
            // 5. Remplacer la racine de la scène actuelle par la vue du jeu
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);

        } catch (NumberFormatException e) {
            System.err.println("Erreur : La cible minimum ou maximum entrée n'est pas un nombre valide.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
