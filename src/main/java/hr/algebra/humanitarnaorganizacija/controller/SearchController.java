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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.List;

public class SearchController {

    @FXML
    private TableView<Organisation> organisationTable;
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
    private Button searchButton;
    @FXML
    private TextField searchFieldText;

    //search
    private List<Organisation> organisations;


    @FXML
    private void initialize() {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearEstablishment"));
        employeesColumn.setCellValueFactory(new PropertyValueFactory<>("numOfEmployees"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("yearlyBudget"));
        endGoal.setCellValueFactory(new PropertyValueFactory<>("endGoal"));


        //vraćam cijeli Country objekt pa idem sa SimpleStringProperty jer jedino tako mogu doci do GetStateName u Country!

        countryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry().getStateName()));
        missionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMission().getMissionTitle()));
        volunteerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVolunteer().getName()));
        sponsorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSponsor().getName()));
        campaignColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCampaign().getCampaignTitle()));

        loadData();
    }

    private void loadData() {
        organisations = OrganisationRepo.getInstance().findAll(); // postavljam listu tj punim
        organisationTable.setItems(FXCollections.observableArrayList(organisations)); // wrapper oko te liste
    }

    public void onSearchButtonClick(ActionEvent actionEvent) {
        String searchTerm = searchFieldText.getText().trim().toLowerCase();
        //search reset
        if (searchTerm.isBlank()) {
            organisationTable.setItems(FXCollections.observableArrayList(organisations));
            return;
        }
        List<Organisation> filtered =
                organisations.stream().filter(
                                o -> o.getTitle().toLowerCase().contains(searchTerm)
                                        || o.getEndGoal().toLowerCase().contains(searchTerm) || o.getCountry().getStateName().toLowerCase().contains(searchTerm)).toList();

        organisationTable.setItems(FXCollections.observableArrayList(filtered));
    }
}
