package com.comptebon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de l'accueil
        FXMLLoader loader = new FXMLLoader(getClass().getResource("accueil.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Le Compte est Bon - Accueil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}