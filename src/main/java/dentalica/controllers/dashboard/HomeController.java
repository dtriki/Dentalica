package dentalica.controllers.dashboard;

import dentalica.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DASHBOARD_FXML_URL = "/dashboard.fxml";

    public void switchToDashboard(MouseEvent mouseEvent) {
        var parent = loadFxml(Constants.BASE_FXML_URL + DASHBOARD_FXML_URL);
        var scene = new Scene(parent);
        var stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private Parent loadFxml(String viewPath) {
        try {
            return FXMLLoader.load(getClass().getResource(viewPath));
        } catch (IOException e) {
            logger.error("Unable to load fxml view, path: " + viewPath, e);
            throw new RuntimeException();
        }
    }

}
