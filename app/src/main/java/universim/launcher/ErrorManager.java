package universim.launcher;

import javafx.scene.control.Alert;

public class ErrorManager {
    public static void errorMessage(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(errorMsg);
        alert.setContentText("Consultez les logs pour plus de details");
        alert.showAndWait();
    }
}
