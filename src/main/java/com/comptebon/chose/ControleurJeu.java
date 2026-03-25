package com.comptebon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.stage.FileChooser;
import java.io.File;

public class ControleurJeu {

    @FXML
    private Label labelCible;
    @FXML
    private HBox conteneurPlaques;
    @FXML
    private VBox listeCalculs;
    @FXML
    private MenuItem itemSolution;
    @FXML
    private MenuItem itemIndice;

    private MoteurJeu moteur = new MoteurJeu();
    private Stack<EtatJeu> historique = new Stack<>();
    private List<Integer> plaquesInitiales;
    private Button premierBouton = null;
    private String operateurSelectionne = "";
    private Integer premierNombre = null;

    private record EtatJeu(List<Integer> plaques, List<String> calculs) implements Serializable {
    }

    public void initialiserPartie(int nbPlaques, int min, int max) {
        MoteurJeu.Tirage tirageValide = moteur.initialiserNouveauTirage(nbPlaques, min, max);
        labelCible.setText(String.valueOf(tirageValide.cible()));
        this.plaquesInitiales = new ArrayList<>(tirageValide.plaques());

        itemSolution.setOnAction(event -> afficherSolutionDuMoteur());
        itemIndice.setOnAction(event -> afficherIndiceAdaptatif());

        rechargerInterface(this.plaquesInitiales, new ArrayList<>());
    }

    // --- SÉRIALISATION (SAUVEGARDE / CHARGEMENT) ---

    @FXML
    private void sauvegarderPartie() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la partie");
        fileChooser.setInitialFileName("partie_compte_bon.ser");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Fichiers de sauvegarde (*.ser)", "*.ser"));

        // Récupérer la fenêtre actuelle
        Stage stage = (Stage) labelCible.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            SauvegardePartie data = new SauvegardePartie(
                    Integer.parseInt(labelCible.getText()),
                    this.plaquesInitiales,
                    recupererNombresActuels(),
                    recupererCalculsActuels());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(data);
                afficherAlerte("Sauvegarde", "Partie enregistrée sous : " + file.getName(),
                        Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible de sauvegarder le fichier.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void chargerPartie() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une sauvegarde");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Fichiers de sauvegarde (*.ser)", "*.ser"));

        Stage stage = (Stage) labelCible.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null && file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                SauvegardePartie data = (SauvegardePartie) ois.readObject();

                this.plaquesInitiales = data.getPlaquesInitiales();
                labelCible.setText(String.valueOf(data.getCible()));

                historique.clear();
                rechargerInterface(data.getPlaquesActuelles(), data.getHistoriqueCalculs());
                resetSelection();

                afficherAlerte("Chargement", "Partie chargée avec succès !", Alert.AlertType.INFORMATION);
            } catch (IOException | ClassNotFoundException e) {
                afficherAlerte("Erreur", "Fichier corrompu ou illisible.", Alert.AlertType.ERROR);
            }
        }
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- LOGIQUE DE JEU ---

    private void afficherIndiceAdaptatif() {
        String msg = moteur.obtenirIndiceDynamique(recupererNombresActuels(), Integer.parseInt(labelCible.getText()));
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-font-style: italic; -fx-text-fill: #d35400; -fx-background-color: #fff3e0; -fx-padding: 5;");
        listeCalculs.getChildren().add(0, lbl);
    }

    private void handlePlaqueClick(Button boutonClique, Integer valeur) {
        if (premierNombre == null) {
            premierNombre = valeur;
            premierBouton = boutonClique;
            premierBouton.setStyle("-fx-border-color: #ffcc00; -fx-border-width: 3px; -fx-background-color: #f0f0f0;");
        } else if (!operateurSelectionne.isEmpty() && boutonClique != premierBouton) {
            Integer resultat = calculer(premierNombre, valeur, operateurSelectionne);
            if (resultat != null) {
                sauvegarderEtatDansHistorique();
                List<Integer> nouveaux = recupererNombresActuels();
                nouveaux.remove(premierNombre);
                nouveaux.remove((Integer) valeur);
                nouveaux.add(resultat);

                List<String> calculs = recupererCalculsActuels();
                calculs.add(premierNombre + " " + operateurSelectionne + " " + valeur + " = " + resultat);

                rechargerInterface(nouveaux, calculs);
                verifierVictoire(resultat);
            }
            resetSelection();
        }
    }

    @FXML
    private void annulerDerniereAction() {
        if (!historique.isEmpty()) {
            EtatJeu precedent = historique.pop();
            rechargerInterface(precedent.plaques, precedent.calculs);
            resetSelection();
        }
    }

    @FXML
    private void reinitialiserPartie() {
        historique.clear();
        rechargerInterface(plaquesInitiales, new ArrayList<>());
        resetSelection();
    }

    private void rechargerInterface(List<Integer> plaques, List<String> calculs) {
        conteneurPlaques.getChildren().clear();
        for (Integer v : plaques) {
            Button btn = new Button(String.valueOf(v));
            btn.getStyleClass().add("plate-button");
            btn.setOnAction(e -> handlePlaqueClick(btn, v));
            conteneurPlaques.getChildren().add(btn);
        }
        listeCalculs.getChildren().clear();
        for (String c : calculs) {
            Label lbl = new Label(c);
            lbl.setStyle("-fx-font-size: 18px; -fx-text-fill: #2e1a4f;");
            listeCalculs.getChildren().add(lbl);
        }
    }

    private void afficherSolutionDuMoteur() {
        moteur.obtenirIndiceDynamique(plaquesInitiales, Integer.parseInt(labelCible.getText()));
        listeCalculs.getChildren().clear();
        Label detail = new Label(moteur.getDerniereSolution());
        detail.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18px;");
        listeCalculs.getChildren().add(detail);
    }

    @FXML
    private void handleOperateurClick(ActionEvent event) {
        if (premierNombre != null)
            operateurSelectionne = ((Button) event.getSource()).getText();
    }

    private Integer calculer(int n1, int n2, String op) {
        return switch (op) {
            case "+" -> n1 + n2;
            case "-" -> (n1 - n2 > 0) ? n1 - n2 : null;
            case "×" -> n1 * n2;
            case "÷" -> (n2 != 0 && n1 % n2 == 0) ? n1 / n2 : null;
            default -> null;
        };
    }

    private void resetSelection() {
        if (premierBouton != null)
            premierBouton.setStyle("");
        premierNombre = null;
        premierBouton = null;
        operateurSelectionne = "";
    }

    private void verifierVictoire(int res) {
        if (res == Integer.parseInt(labelCible.getText())) {
            labelCible.setStyle(
                    "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 48px; -fx-padding: 10 30;");
        }
    }

    private List<Integer> recupererNombresActuels() {
        List<Integer> liste = new ArrayList<>();
        for (Node node : conteneurPlaques.getChildren())
            if (node instanceof Button b)
                liste.add(Integer.parseInt(b.getText()));
        return liste;
    }

    private List<String> recupererCalculsActuels() {
        List<String> liste = new ArrayList<>();
        for (Node node : listeCalculs.getChildren())
            if (node instanceof Label l)
                liste.add(l.getText());
        return liste;
    }

    private void sauvegarderEtatDansHistorique() {
        historique.push(new EtatJeu(recupererNombresActuels(), recupererCalculsActuels()));
    }

    @FXML
    public void retourAccueil(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("accueil.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 700));
    }
}