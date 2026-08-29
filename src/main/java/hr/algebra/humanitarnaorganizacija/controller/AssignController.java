package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.model.Organisation;
import hr.algebra.humanitarnaorganizacija.model.Volunteer;
import hr.algebra.humanitarnaorganizacija.repo.OrganisationRepo;
import hr.algebra.humanitarnaorganizacija.repo.VolunteerRepo;
import hr.algebra.humanitarnaorganizacija.util.AlertUtility;
import hr.algebra.humanitarnaorganizacija.util.RoleUtility;
import hr.algebra.humanitarnaorganizacija.util.UserActionLoggerUtility;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;


public class AssignController {


    @FXML
    private TableView<Organisation> organisationTable;
    @FXML
    private TableView<Volunteer> volunteerTable;
    @FXML
    private TableColumn<Volunteer, String> volunteerNameColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerSurnameColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerSpecialisationColumn;
    @FXML
    private TableColumn<Volunteer, Integer> volunteerHoursWorkedColumn;
    @FXML
    private TableColumn<Volunteer, String> volunteerStatusColumn;

    @FXML
    private TableColumn<Organisation, String> organisationTitle;
    @FXML
    private TableColumn<Organisation, String> assignedVolunteer;

    @FXML
    private void initialize() {
        volunteerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        volunteerSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surName"));
        volunteerSpecialisationColumn.setCellValueFactory(new PropertyValueFactory<>("specialisation"));
        volunteerHoursWorkedColumn.setCellValueFactory(new PropertyValueFactory<>("hoursNum"));
        volunteerStatusColumn.setCellValueFactory(new PropertyValueFactory<>("volunteerStatus"));
        organisationTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        assignedVolunteer.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVolunteer().getName()));

        loadData();
        setupDragSource();
        setDropTarget();
    }


    private void setupDragSource() {
        volunteerTable.setRowFactory(volunteerTableView -> {
            TableRow<Volunteer> row = new TableRow<>();
            row.setOnDragDetected(event -> {
                if (row.isEmpty()) {
                    return;
                }
                Volunteer volunteer = row.getItem();
                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(volunteer.getID()));
                db.setContent(content);
                event.consume();
            });
            return row;
        });
    }

    private void setDropTarget() {
        organisationTable.setRowFactory(organisationTableView -> {
            TableRow<Organisation> row = new TableRow<>();
            row.setOnDragOver(dragEvent -> {
                if (dragEvent.getGestureSource() != row && dragEvent.getDragboard().hasString() && !row.isEmpty()) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                }
                dragEvent.consume();
            });

            row.setOnDragDropped(dragEvent -> {
                Dragboard db = dragEvent.getDragboard();
                boolean success = false;
                if (db.hasString() && !row.isEmpty()) {
                    int volunteerID = Integer.parseInt(db.getString());
                    Volunteer volunteer = VolunteerRepo.getInstance().findById(volunteerID).orElseThrow();
                    Organisation organisation = row.getItem();
                    organisation.setVolunteer(volunteer);
                    OrganisationRepo.getInstance().update(organisation);
                    success = true;
                    AlertUtility.showInfo("Volunteer: ",
                            volunteer.getName() + " " + volunteer.getSurName() + " assigned to " + organisation.getTitle());
                    UserActionLoggerUtility.log(RoleUtility.getCurrentUser().getUserName(), "ASSIGN VOLUNTEER", volunteer.getName() + " " + volunteer.getSurName() + " -> " + organisation.getTitle());
                }
                dragEvent.setDropCompleted(success);
                dragEvent.consume();
                loadData();
            });
            return row;
        });
    }

    private void loadData() {
        volunteerTable.setItems(FXCollections.observableArrayList(VolunteerRepo.getInstance().findAll()));
        organisationTable.setItems(FXCollections.observableArrayList(OrganisationRepo.getInstance().findAll()));
    }


}
