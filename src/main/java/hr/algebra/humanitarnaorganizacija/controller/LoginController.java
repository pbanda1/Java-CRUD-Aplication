package hr.algebra.humanitarnaorganizacija.controller;

import hr.algebra.humanitarnaorganizacija.App;
import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.model.AppUser;
import hr.algebra.humanitarnaorganizacija.repo.AppUserRepo;
import hr.algebra.humanitarnaorganizacija.util.AlertUtility;
import hr.algebra.humanitarnaorganizacija.util.RoleUtility;
import hr.algebra.humanitarnaorganizacija.util.SceneUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    //enkapsuliram varijable, a klasa je public kako bih je vidio u FXML-u

    @FXML
    private void handleRegister(ActionEvent actionEvent) {
        String usernameInput = usernameField.getText().trim();
        String passwordInput = passwordField.getText();

        try {
            AppUserRepo.getInstance().save(new AppUser(usernameInput, passwordInput));
            AlertUtility.showInfo("Registration", "User " + usernameInput + " is registered");
        } catch (AppException e) {
            AlertUtility.showError("Error whilst trying to register", e.getMessage());
        }
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        String usernameInput = usernameField.getText().trim();
        String passwordInput = passwordField.getText();

                                                           /*pozivam metodu iz AppUserRepo!*/
        Optional<AppUser> appUser = AppUserRepo.getInstance().findByUserName(usernameInput);

        /*Optional je tip podatka zato.get.getPass..*/
        if (appUser.isPresent() && appUser.get().getPassWord().equals(passwordInput)) {
            RoleUtility.setCurrentUser(appUser.get());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            SceneUtility.loadSceneWithLoader(App.class.getResource("view/welcome-view.fxml"), stage, "human rights organisation");
        } else {
            AlertUtility.showError("Authentification failed", "Wrong username or password");
        }
    }
}
