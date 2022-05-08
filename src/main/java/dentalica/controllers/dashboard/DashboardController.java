package dentalica.controllers.dashboard;

import dentalica.Constants;
import dentalica.controllers.intervention.InterventionsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PATIENTS_FXML_URL = "/patients.fxml";
    private static final String FEES_FXML_URL = "/fees.fxml";
    private static final String INTERVENTIONS_FXML_URL = "/interventions.fxml";

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    public void patientsOnClick(MouseEvent mouseEvent) {
        var parent = loadFxml(Constants.BASE_FXML_URL + PATIENTS_FXML_URL);
        mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(parent);
    }

    private Parent loadFxml(String viewPath) {
        try {
            return FXMLLoader.load(getClass().getResource(viewPath));
        } catch (IOException e) {
            logger.error("Unable to load fxml view, path: " + viewPath, e);
            throw new RuntimeException();
        }
    }

    @FXML
    public void feesOnClick(MouseEvent mouseEvent) {
        var parent = loadFxml(Constants.BASE_FXML_URL + FEES_FXML_URL);
        mainAnchorPane.getChildren().clear();
        mainAnchorPane.getChildren().add(parent);
    }

    @FXML
    public void interventionsOnClick(MouseEvent mouseEvent) {
        var viewPath = Constants.BASE_FXML_URL + Constants.INTERVENTIONS_FXML_URL;
        var loader = new FXMLLoader(getClass().getResource(viewPath));
        try {
            mainAnchorPane.getChildren().clear();
            mainAnchorPane.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InterventionsController controller = loader.getController();
        controller.initData(null);
    }

}
