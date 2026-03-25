package com.comptebon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class AccueilController {
    
    @FXML
    public void allerVersParametres(ActionEvent event) {
        try {
            com.comptebon.App.setRoot("parametres");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void chargerPartieAccueil(ActionEvent event) {
        com.comptebon.model.Partie p = com.comptebon.util.SaveManager.load();
        if (p != null) {
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(com.comptebon.App.class.getResource("jeu.fxml"));
                javafx.scene.Parent root = loader.load();
                PartieController controller = loader.getController();
                controller.setPartie(p);
                ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void quitterApp(ActionEvent event) {
        System.exit(0);
    }
}
