package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.App;
import hr.algebra.humanitarnaorganizacija.service.ServiceLoadStates;
import hr.algebra.humanitarnaorganizacija.util.AlertUtility;
import hr.algebra.humanitarnaorganizacija.util.RoleUtility;
import hr.algebra.humanitarnaorganizacija.util.SceneUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;


public class MenuController {

@FXML private AnchorPane rootAnchorPane;
@FXML private MenuItem adminMenuItem;
@FXML private MenuItem loadStatesMenuItem;

//load service for loadingStates
private static final ServiceLoadStates loadStatesService = new ServiceLoadStates();

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
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/admin-view.fxml"), stage(), "Admin UI");
    }
    @FXML
    private void onLogout() {
        RoleUtility.clearCurrentUser();
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/login-view.fxml"), stage(), "Authentification");
    }


    @FXML private void AboutUs(ActionEvent actionEvent) {
        AlertUtility.showInfo("Human Rights Organisation", "Our Mission is to give voice to every Human");
    }

    @FXML private void onLoadStates(ActionEvent actionEvent) {
            if(loadStatesService.isRunning()) {
                return;
            } //if the service is running return
            loadStatesMenuItem.setDisable(true); //disable menu while import is running
            Alert progressAlert = AlertUtility.showInfoNonBlocking("API import", "Loading States...");
            progressAlert.contentTextProperty().bind(loadStatesService.messageProperty());
            //binding alert text with background task msg

            //if success
            loadStatesService.setOnSucceeded(event -> {
                loadStatesMenuItem.setDisable(false);
                Integer num = loadStatesService.getValue(); //states pulled from API
                progressAlert.contentTextProperty().unbind();
                progressAlert.setContentText("Import finished, new states count= " + num);
        });

            //if fail
            loadStatesService.setOnFailed(event -> {
                loadStatesMenuItem.setDisable(false);
                progressAlert.contentTextProperty().unbind();
                progressAlert.setContentText("Error " + loadStatesService.getException().getMessage());
            });

            loadStatesService.restart();

    }
}
