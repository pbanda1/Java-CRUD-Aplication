package hr.algebra.humanitarnaorganizacija.controller;
import hr.algebra.humanitarnaorganizacija.model.Organisation;
import hr.algebra.humanitarnaorganizacija.repo.OrganisationRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class AdminController {

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
        ObservableList<Organisation> data  = FXCollections.observableArrayList(OrganisationRepo.getInstance().findAll());
        adminOrganisationTable.setItems(data);
    }

   @FXML private void onAdd(ActionEvent actionEvent) {
    }

    @FXML private void onEdit(ActionEvent actionEvent) {
    }

    @FXML private void onDelete(ActionEvent actionEvent) {
    }
}
