module com.comptebon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;

    opens com.comptebon to javafx.fxml; // Permet au FXML de parler à ton code
    opens com.comptebon.controller to javafx.fxml;
    exports com.comptebon.dto to com.fasterxml.jackson.databind;

    exports com.comptebon;
}