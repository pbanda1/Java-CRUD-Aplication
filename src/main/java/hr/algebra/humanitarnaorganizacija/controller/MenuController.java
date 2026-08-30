package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.App;
import hr.algebra.humanitarnaorganizacija.service.ServiceExportOrganisations;
import hr.algebra.humanitarnaorganizacija.service.ServiceLoadOrganisations;
import hr.algebra.humanitarnaorganizacija.service.ServiceLoadStates;
import hr.algebra.humanitarnaorganizacija.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.util.Optional;


public class MenuController {

    @FXML
    private MenuItem assignVolunteersMenuItem;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private MenuItem adminMenuItem;
    @FXML
    private MenuItem loadStatesMenuItem;
    @FXML
    private MenuItem loadOrganisationsXMLMenuItem;
    @FXML
    private MenuItem exportOrganisationsXMLMenuItem;

    //load service for loadingStates
    private static final ServiceLoadStates loadStatesService = new ServiceLoadStates();
    //load service for LoadingOrganisations
    private static final ServiceLoadOrganisations loadOrganisationsXMLService = new ServiceLoadOrganisations();
    //export service for XML export
    private static final ServiceExportOrganisations exportOrganisationXMLService = new ServiceExportOrganisations();

    @FXML
    private void initialize() {
        /*automatski se pokrece kad se FXML ekran ucita u memoriju
         * sustav poziva RoleUtility.isAdmin() i provjerava je li trenutni korisnik admin
         * ako trenutni korisnik nije admin i adminMenuItem je disable-an
         * */
        if (!RoleUtility.isAdmin()) {
            adminMenuItem.setDisable(true);
            loadStatesMenuItem.setDisable(true);
            loadOrganisationsXMLMenuItem.setDisable(true);
            exportOrganisationsXMLMenuItem.setDisable(true);
            assignVolunteersMenuItem.setDisable(true);
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
        String USER = RoleUtility.getCurrentUser().getUserName();
        RoleUtility.clearCurrentUser();
        UserActionLoggerUtility.log(USER, "LOGOUT",  " user logged out");
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/login-view.fxml"), stage(), "Authentification");
    }


    @FXML
    private void AboutUs(ActionEvent actionEvent) {
        AlertUtility.showInfo("Human Rights Organisation", "Our Mission is to give voice to every Human");
    }

    @FXML
    private void onLoadStates(ActionEvent actionEvent) {
        if (loadStatesService.isRunning()) {
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
            UserActionLoggerUtility.log(RoleUtility.getCurrentUser().getUserName(), "IMPORT_STATES_XML", num + " states imported");
        });

        //if fail
        loadStatesService.setOnFailed(event -> {
            loadStatesMenuItem.setDisable(false);
            progressAlert.contentTextProperty().unbind();
            progressAlert.setContentText("Error " + loadStatesService.getException().getMessage());
        });
        loadStatesService.restart();
    }

    @FXML
    private void onLoadOrganisationsXML(ActionEvent actionEvent) {
        if (loadOrganisationsXMLService.isRunning()) {
            return;
        }
        loadOrganisationsXMLMenuItem.setDisable(true);
        Alert progressAlert = AlertUtility.showInfoNonBlocking("XML import", "Loading Organisations...");
        progressAlert.contentTextProperty().bind(loadOrganisationsXMLService.messageProperty());

        //if success
        loadOrganisationsXMLService.setOnSucceeded(event -> {
            loadOrganisationsXMLMenuItem.setDisable(false);
            Integer num = loadOrganisationsXMLService.getValue(); //numOfNewOrganisations
            progressAlert.contentTextProperty().unbind();
            progressAlert.setContentText("Import finished, new Organisations count= " + num);
            UserActionLoggerUtility.log(RoleUtility.getCurrentUser().getUserName(), "IMPORT_ORGANISATIONS_XML", num + " organisations imported");

        });

        //if fail
        loadOrganisationsXMLService.setOnFailed(event -> {
            loadOrganisationsXMLMenuItem.setDisable(false);
            progressAlert.contentTextProperty().unbind();
            progressAlert.setContentText("Error " + loadOrganisationsXMLService.getException().getMessage());
        });

        loadOrganisationsXMLService.restart();

    }

    @FXML
    private void onExportOrganisationsXML(ActionEvent actionEvent) {
        if (exportOrganisationXMLService.isRunning()) {
            return;
        }
        //postavi FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Organisations");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        fileChooser.setInitialFileName("organisations-export.xml");

        File file = fileChooser.showSaveDialog(stage());
        if (file == null) {
            return;
        }
        exportOrganisationXMLService.setPath(file.getAbsolutePath());
        exportOrganisationsXMLMenuItem.setDisable(true);
        Alert progressAlert = AlertUtility.showInfoNonBlocking("XML export", "Exporting Organisations...");
        progressAlert.contentTextProperty().bind(exportOrganisationXMLService.messageProperty());

        exportOrganisationXMLService.setOnSucceeded(event -> {
            exportOrganisationsXMLMenuItem.setDisable(false);
            Integer num = exportOrganisationXMLService.getValue();
            progressAlert.contentTextProperty().unbind();
            progressAlert.setContentText("Export finished, " + num + " organisations saved");
            UserActionLoggerUtility.log(RoleUtility.getCurrentUser().getUserName(), "EXPORT_ORGANISATIONS_XML", num + " organisations exported");
        });

        exportOrganisationXMLService.setOnFailed(event -> {
            exportOrganisationsXMLMenuItem.setDisable(false);
            progressAlert.contentTextProperty().unbind();
            progressAlert.setContentText("Error " + exportOrganisationXMLService.getException().getMessage());
        });
        exportOrganisationXMLService.restart();
    }

    @FXML private void onAssignVolunteers(ActionEvent actionEvent) {
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/assign-view.fxml"), stage(), "Assign Volunteers");
    }

   @FXML private void OnResetDatabase(ActionEvent actionEvent) {
       Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete all Countries, Missions, Volunteers, Sponsors, Campaigns and Organisations. Are you sure?", ButtonType.YES, ButtonType.NO);
       confirmAlert.setTitle("Reset Database");
       Optional<ButtonType> result = confirmAlert.showAndWait();
       if (result.isPresent() && result.get() == ButtonType.YES) {
           DatabaseUtil.resetDatabase(DatabaseUtil.getConnection());
           UserActionLoggerUtility.log(RoleUtility.getCurrentUser().getUserName(), "RESET_DATABASE", "Database cleared");
           AlertUtility.showInfo("Database", "Database has been cleared");
       }
    }
}
