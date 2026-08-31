package hr.algebra.humanitarnaorganizacija;

import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void init() throws Exception {
        var conn = DatabaseUtil.getConnection();
        if (!DatabaseUtil.schemaExists(conn)) {
            DatabaseUtil.initSchema(conn);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Humanitarian Organisation");
        stage.setScene(scene);
        stage.show();

    }
}
