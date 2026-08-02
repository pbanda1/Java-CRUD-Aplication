package hr.algebra.humanitarnaorganizacija.util;

import javafx.scene.control.Alert;

public class AlertUtility {
    private AlertUtility() {};

    public static void showInfo(String title, String message)    { show(Alert.AlertType.INFORMATION, title, message); }
    public static void showError(String title, String message)   { show(Alert.AlertType.ERROR, title, message); }
    public static void showWarning(String title, String message) { show(Alert.AlertType.WARNING, title, message); }

    private static void show (Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait(); //blokira dok korisnik ne potvrdi
    }
    public static Alert showInfoNonBlocking (String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        return alert;
    }
}
