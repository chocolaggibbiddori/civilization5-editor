package chocola.civilizationfiveeditor;

import java.io.IOException;

import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parameters parameters = getParameters();
        List<String> unnamedParameterList = parameters.getUnnamed();

        String version = unnamedParameterList.isEmpty() ? "v1" : unnamedParameterList.getFirst();
        String mainResourceName = Objects.equals(version, "v2") ? "main-view2.fxml" : "main-view.fxml";

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(mainResourceName));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Civilization V Editor");
        stage.setScene(scene);
        stage.show();
    }
}
