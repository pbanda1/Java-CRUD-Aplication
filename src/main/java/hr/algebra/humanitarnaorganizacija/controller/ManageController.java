package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.App;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.*;
import hr.algebra.humanitarnaorganizacija.repo.*;
import hr.algebra.humanitarnaorganizacija.util.AlertUtility;
import hr.algebra.humanitarnaorganizacija.util.RoleUtility;
import hr.algebra.humanitarnaorganizacija.util.SceneUtility;
import hr.algebra.humanitarnaorganizacija.util.UserActionLoggerUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ManageController {

    @FXML
    private BorderPane rootPane4stage;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TextField titleField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField employeesField;
    @FXML
    private TextField budgetField;
    @FXML
    private TextField endGoalField;
    @FXML
    private TextField logoField;

    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Mission> missionComboBox;
    @FXML
    private ComboBox<Volunteer> volunteerComboBox;
    @FXML
    private ComboBox<Sponsor> sponsorComboBox;
    @FXML
    private ComboBox<Campaign> campaignComboBox;


    private Organisation editingOrganisation;


    private Stage stage() {
        /*Brzo dohvaćanje trenutnog prozora*/
        return (Stage) rootPane4stage.getScene().getWindow();
    }

    @FXML
    private void initialize() {

        /// COMBO BOX  DATA SETUP
        ObservableList<Country> countries = FXCollections.observableArrayList(CountryRepo.getInstance().findAll());
        countryComboBox.setItems(countries);

        ObservableList<Mission> missions = FXCollections.observableArrayList(MissionRepo.getInstance().findAll());
        missionComboBox.setItems(missions);

        ObservableList<Volunteer> volunteers = FXCollections.observableArrayList(VolunteerRepo.getInstance().findAll());
        volunteerComboBox.setItems(volunteers);

        ObservableList<Sponsor> sponsors = FXCollections.observableArrayList(SponsorRepo.getInstance().findAll());
        sponsorComboBox.setItems(sponsors);

        ObservableList<Campaign> campaigns = FXCollections.observableArrayList(CampaignRepo.getInstance().findAll());
        campaignComboBox.setItems(campaigns);


    }


    public void setOrganisationToEdit(Organisation org) {
        this.editingOrganisation = org;

        titleField.setText(org.getTitle());
        yearField.setText(String.valueOf(org.getYearEstablishment()));
        employeesField.setText(String.valueOf(org.getNumOfEmployees()));
        budgetField.setText(String.valueOf(org.getYearlyBudget()));
        endGoalField.setText(org.getEndGoal());
        logoField.setText(org.getLogo());

        countryComboBox.setValue(org.getCountry());
        missionComboBox.setValue(org.getMission());
        volunteerComboBox.setValue(org.getVolunteer());
        sponsorComboBox.setValue(org.getSponsor());
        campaignComboBox.setValue(org.getCampaign());
    }

    @FXML
    private void onSave(ActionEvent actionEvent) {


        String titleFieldText = titleField.getText();

        String yearFieldText = yearField.getText();
        String employeesFieldText = employeesField.getText();
        String budgetFieldText = budgetField.getText();
        String endGoalFieldText = endGoalField.getText();
        String logoFieldText = logoField.getText();

        int yearEstablishment;
        int numOfEmployees;
        double yearlyBudget;

        //CHECK FOR INTEGERS BEGIN  //////
        try {
            yearEstablishment = Integer.parseInt(yearFieldText.trim());
            numOfEmployees = Integer.parseInt(employeesFieldText.trim());
            yearlyBudget = Double.parseDouble(budgetFieldText.trim());
        } catch (NumberFormatException e) {
            AlertUtility.showError("Invalid input", "Establishment Year and Num of Employees must be whole numbers, Yearly Budget must be a number.");
            return;
        }
        if (yearEstablishment < 1863) {
            AlertUtility.showError("Invalid Input", "Establishment year must be 1863 or younger");
            return;
        }
        if (numOfEmployees <= 0) {
            AlertUtility.showError("Invalid Input", "NumOfEmployees must be greater than 0!");
            return;
        }
        if (yearlyBudget <= 10000) {
            AlertUtility.showError("Invalid Input", "Yearly Budget must be greater than 10000");
            return;
        }
        //CHECK FOR INTEGERS END  //////


        //povlačenje vrijednosti iz combosa
        Country selectedCountry = countryComboBox.getValue();
        Mission selectedMission = missionComboBox.getValue();
        Volunteer selectedVolunteer = volunteerComboBox.getValue();
        Sponsor selectedSponsor = sponsorComboBox.getValue();
        Campaign selectedCampaign = campaignComboBox.getValue();

        if (selectedCountry == null || selectedMission == null || selectedVolunteer == null
                || selectedSponsor == null || selectedCampaign == null) {
            AlertUtility.showError("Invalid input", "You must select a Country, Mission, Volunteer, Sponsor and Campaign.");
            return;
        }
        // definiram Organizaciju i spremam je
        Organisation org = new Organisation();
        org.setTitle(titleFieldText);
        org.setYearEstablishment(yearEstablishment);
        org.setNumOfEmployees(numOfEmployees);
        org.setYearlyBudget(yearlyBudget);
        org.setEndGoal(endGoalFieldText);
        org.setLogo(logoFieldText);
        org.setCountry(selectedCountry);
        org.setMission(selectedMission);
        org.setVolunteer(selectedVolunteer);
        org.setSponsor(selectedSponsor);
        org.setCampaign(selectedCampaign);


        try {
            if (editingOrganisation == null) {
                OrganisationRepo.getInstance().save(org);
                UserActionLoggerUtility.log(RoleUtility.getCurrentUser().getUserName(), "EDITING_ORGANISATION", "edited: " + org.getTitle());
                AlertUtility.showInfo("Success", "Organisation saved successfully");

            } else {
                org.setID(editingOrganisation.getID());
                OrganisationRepo.getInstance().update(org);
                AlertUtility.showInfo("Success", "Organisation updated successfully");
            }
            SceneUtility.loadSceneWithLoader(
                    App.class.getResource("view/admin-view.fxml"), stage(), "Admin UI");
        } catch (RepoException e) {
            AlertUtility.showError("Error while saving Organisation by input!", e.getMessage());
        }
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        SceneUtility.loadSceneWithLoader(
                App.class.getResource("view/admin-view.fxml"), stage(), "Admin UI");
    }
}
