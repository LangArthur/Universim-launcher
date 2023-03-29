package universim.launcher;

import javafx.scene.control.Alert;

/* expose static utility methods to handle error in pop-up */
public class ErrorManager {
    public static void errorMessage(Exception error) {
        // log the error
        Launcher.logger.err(error.getMessage());
        // display error in pop-up
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(error.getMessage());
        alert.setContentText("Consultez les logs pour plus de details");
        alert.showAndWait();
    }

    public static void errorMessage(String errorMsg) {
        // display error in pop-up
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(errorMsg);
        alert.setContentText("Consultez les logs pour plus de details");
        alert.showAndWait();
    }
}
