package hr.algebra.humanitarnaorganizacija.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneUtility {
    private SceneUtility () {};

    public static FXMLLoader loadSceneWithLoader(URL fxmlUrl, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            return loader;
        } catch (IOException e) {
            throw new RuntimeException("Can not load FXML" + fxmlUrl, e);
        }
    }
}
