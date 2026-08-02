package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.App;
import hr.algebra.humanitarnaorganizacija.util.AlertUtility;
import hr.algebra.humanitarnaorganizacija.util.RoleUtility;
import hr.algebra.humanitarnaorganizacija.util.SceneUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;


public class MenuController {

@FXML private AnchorPane rootAnchorPane;
@FXML private MenuItem adminMenuItem;
    @FXML
    private void initialize() {
        /*automatski se pokrece kad se FXML ekran ucita u memoriju
        * sustav poziva RoleUtility.isAdmin() i provjerava je li trenutni korisnik admin
        * ako trenutni korisnik nije admin i adminMenuItem je disable-an
        * */
        if (!RoleUtility.isAdmin()) {
            adminMenuItem.setDisable(true);
        }
    }

    private Stage stage() {
        /*Brzo dohvaćanje trenutnog prozora*/
        return (Stage) rootAnchorPane.getScene().getWindow();
    }

    @FXML
    private void onStartingPage() {
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/welcome-view.fxml"), stage(), "Starting Page");
    }


    @FXML
    private void onTables() {
       SceneUtility.loadSceneWithLoader(
               App.class.getResource("view/search-view.fxml"), stage(), "Organisations");

    }

    @FXML
    private void onAdmin() {
        // admin-view još ne postoji (Checkpoint 6) — privremeni placeholder
        AlertUtility.showInfo("Uskoro", "Admin panel dolazi kasnije.");
    }
    @FXML
    private void onLogout() {
        RoleUtility.clearCurrentUser();
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/login-view.fxml"), stage(), "Authentification");
    }

    public void AboutUs(ActionEvent actionEvent) {
        AlertUtility.showInfo("Human Rights Organisation", "Our Mission is to give voice to every Human");
    }
}
