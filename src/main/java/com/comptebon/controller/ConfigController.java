package com.comptebon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * Contrôleur responsable de l'écran de configuration avant le début de la partie.
 * 
 * Cette classe gère les interactions définies dans le fichier 'config.fxml', telles que 
 * la modification des paramètres (nombre de plaques, valeurs cibles, chronomètre) 
 * et la navigation vers l'écran de jeu via les boutons.
 */
public class ConfigController {

    /**
     * Composant Slider permettant à l'utilisateur de glisser pour choisir 
     * le nombre de plaques avec lesquelles il va jouer (liées au fichier FXML).
     */
    @FXML
    private Slider sliderNbPlaques;

    /**
     * Champ texte permettant d'afficher numériquement la valeur sélectionnée 
     * par le Slider pour le nombre de plaques.
     */
    @FXML
    private TextField txtNbPlaques;

    /**
     * Champ texte utilisé pour saisir ou afficher la borne minimale 
     * de la cible que le joueur devra atteindre.
     */
    @FXML
    private TextField txtPlageCibleMin;

    /**
     * Champ texte utilisé pour saisir ou afficher la borne maximale 
     * de la cible que le joueur devra atteindre.
     */
    @FXML
    private TextField txtPlageCibleMax;

    /**
     * Case à cocher permettant à l'utilisateur d'activer 
     * ou de désactiver le mode chronométré pour la partie.
     */
    @FXML
    private CheckBox chkChrono;

    /**
     * Méthode d'initialisation appelée automatiquement par JavaFX.
     * 
     * Dès que l'interface FXML est chargée, cette méthode est exécutée.
     * Elle sert ici à créer un "Binding" (lien) entre la valeur pointée par le curseur (Slider) 
     * et l'affichage textuel de cette même valeur dans le champ (TextField) à sa droite.
     */
    @FXML
    public void initialize() {
        if (sliderNbPlaques != null && txtNbPlaques != null) {
            // On ajoute un "listener" qui écoute les changements de valeur du slider
            sliderNbPlaques.valueProperty().addListener((observable, oldValue, newValue) -> {
                // On met à jour le texte à chaque fois que le curseur bouge
                txtNbPlaques.setText(String.valueOf(newValue.intValue()));
            });
            
            // On définit la valeur par défaut au démarrage
            txtNbPlaques.setText(String.valueOf((int) sliderNbPlaques.getValue()));
        }
    }

    /**
     * Action déclenchée par l'utilisateur lorsqu'il clique sur le bouton "Annuler".
     *
     * Permet pour l'instant d'afficher un log. Elle servira typiquement 
     * à ramener l'utilisateur vers le menu d'accueil ou a quitter la page de configuration.
     * 
     * @param event L'événement d'action (le clic sur le bouton) propagé par JavaFX.
     */
    @FXML
    public void onAnnuler(ActionEvent event) {
        System.out.println("Action Annuler déclenchée depuis le bouton FXML");
        // TODO: Implémenter le retour vers l'écran titre ou fermer l'application
    }

    /**
     * Action déclenchée par l'utilisateur lorsqu'il clique sur "Lancer la partie".
     *
     * Valide virtuellement les paramètres (dans de futures évolutions, la logique de validation 
     * s'ajoutera ici), puis délègue le changement de scène vers "partie.fxml".
     * 
     * @param event L'événement d'action (le clic sur le bouton) propagé par JavaFX.
     */
    @FXML
    public void onLancerPartie(ActionEvent event) {
        try {
            System.out.println("Paramètres sauvegardés, changement de vue vers 'partie.fxml'");
            com.comptebon.App.setRoot("partie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
