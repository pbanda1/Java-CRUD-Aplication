package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.App;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Organisation;
import hr.algebra.humanitarnaorganizacija.repo.OrganisationRepo;
import hr.algebra.humanitarnaorganizacija.util.AlertUtility;
import hr.algebra.humanitarnaorganizacija.util.SceneUtility;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminController {


    /// 4 STAGE RETRIEVAL /////
    @FXML
    private BorderPane rootBorderPane;

    private Stage stage() {
        return (Stage) rootBorderPane.getScene().getWindow();
    }

    /// VARIABLE  //
    @FXML
    private TableView<Organisation> adminOrganisationTable;
    @FXML
    private TableColumn<Organisation, String> titleColumn;
    @FXML
    private TableColumn<Organisation, Integer> yearColumn;
    @FXML
    private TableColumn<Organisation, Integer> employeesColumn;
    @FXML
    private TableColumn<Organisation, Double> budgetColumn;
    @FXML
    private TableColumn<Organisation, String> endGoal;
    @FXML
    private TableColumn<Organisation, String> countryColumn;
    @FXML
    private TableColumn<Organisation, String> missionColumn;
    @FXML
    private TableColumn<Organisation, String> volunteerColumn;
    @FXML
    private TableColumn<Organisation, String> sponsorColumn;
    @FXML
    private TableColumn<Organisation, String> campaignColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;


    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearEstablishment"));
        employeesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfEmployees"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("yearlyBudget"));
        endGoal.setCellValueFactory(new PropertyValueFactory<>("endGoal"));

        countryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry().getStateName()));
        missionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMission().getMissionTitle()));
        volunteerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVolunteer().getName()));
        sponsorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSponsor().getName()));
        campaignColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCampaign().getCampaignTitle()));

        loadData();
    }

    private void loadData() {
        ObservableList<Organisation> data = FXCollections.observableArrayList(OrganisationRepo.getInstance().findAll());
        adminOrganisationTable.setItems(data);
    }

    @FXML
    private void onAdd(ActionEvent actionEvent) {
        SceneUtility.loadSceneWithLoader(App.class.getResource("view/manage-view.fxml"), stage(), "Manage UI");
    }

    @FXML
    private void onEdit(ActionEvent actionEvent) {
        Organisation selectedOrg = adminOrganisationTable.getSelectionModel().getSelectedItem();
        if (selectedOrg == null) {
            AlertUtility.showError("No selection", "Please select an Organisation");
            return;
        }
        FXMLLoader loader = SceneUtility.loadSceneWithLoader(App.class.getResource("view/manage-view.fxml"), stage(), "Manage UI");
        ManageController controller = loader.getController();
        controller.setOrganisationToEdit(selectedOrg);
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        Organisation selectedOrg = adminOrganisationTable.getSelectionModel().getSelectedItem();
        if (selectedOrg == null) {
            AlertUtility.showError("No selection", "Please select an valid organisation");
            return;
        }
        try {
            OrganisationRepo.getInstance().deleteById(selectedOrg.getID());
            AlertUtility.showInfo("Organisation", selectedOrg.toString() + " deleted");
            //refresh tablice
            loadData();
        } catch (RepoException e) {
            AlertUtility.showError("Error whilst trying to delete Org", e.getMessage());
        }
    }
}


