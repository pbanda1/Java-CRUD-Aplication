package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.model.AppUser;
import hr.algebra.humanitarnaorganizacija.util.RoleUtility;
import javafx.scene.control.Label;
import javafx.fxml.FXML;


public class WelcomeController {
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        AppUser user = RoleUtility.getCurrentUser();
        welcomeLabel.setText("Welcome " + RoleUtility.getCurrentUser().getName());
    }
}

