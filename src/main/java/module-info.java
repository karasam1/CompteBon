module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example to javafx.fxml; // Permet au FXML de parler à ton code
    exports org.example;
}