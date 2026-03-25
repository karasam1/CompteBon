package com.comptebon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import java.io.IOException;

public class ControleurPrincipal {

    @FXML private Slider sliderPlaques;
    @FXML private Label lblNbPlaques;
    @FXML private TextField txtCibleMin;
    @FXML private TextField txtCibleMax;
    @FXML private ToggleButton btnChrono;

    /**
     * Méthode appelée automatiquement au chargement du FXML.
     * Elle met en place les "écouteurs" pour l'interactivité.
     */
    @FXML
    public void initialize() {
        if (sliderPlaques != null && lblNbPlaques != null) {
            // Met à jour le label en temps réel quand on bouge le slider
            sliderPlaques.valueProperty().addListener((observable, oldValue, newValue) -> {
                lblNbPlaques.setText(String.valueOf(newValue.intValue()));
            });
        }
    }

    /**
     * Change l'apparence du bouton Chrono quand on clique dessus.
     */
    @FXML
    private void handleChrono() {
        if (btnChrono.isSelected()) {
            btnChrono.setText("ON");
            btnChrono.setStyle("-fx-background-radius: 20; -fx-padding: 5 15; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        } else {
            btnChrono.setText("OFF");
            btnChrono.setStyle("-fx-background-radius: 20; -fx-padding: 5 15; -fx-font-weight: bold; -fx-background-color: #f44336; -fx-text-fill: white;");
        }
    }

    @FXML
    public void allerVersParametres(ActionEvent event) throws IOException {
        changerScene(event, "parametres.fxml", "Configuration");
    }

    @FXML
    public void retourAccueil(ActionEvent event) throws IOException {
        changerScene(event, "accueil.fxml", "Accueil");
    }

    @FXML
    public void lancerJeu(ActionEvent event) throws IOException {
        int nb = (int) sliderPlaques.getValue();
        int min = Integer.parseInt(txtCibleMin.getText().trim());
        int max = Integer.parseInt(txtCibleMax.getText().trim());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("jeu.fxml"));
        Parent root = loader.load();

        ControleurJeu ctrl = loader.getController();
        ctrl.initialiserPartie(nb, min, max);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 700));
    }

    private void changerScene(ActionEvent event, String fxmlFile, String titre) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 700));
        stage.setTitle(titre);
    }
}