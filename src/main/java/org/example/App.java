package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Charge le fichier FXML situé dans le même dossier
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Le Compte est Bon - Test");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}