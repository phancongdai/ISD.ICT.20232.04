package views.screen.authentication;

import javafx.scene.control.Alert;

/**
 *
 * @author LENOVO
 */
public class alertMessage {

    private Alert alert;

    public void errorMessage(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void successMessage(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
