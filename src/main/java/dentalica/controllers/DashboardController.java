package dentalica.controllers;

import dentalica.Constants;
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

    @FXML
    private AnchorPane mainAnchorPane;

    public void patientsOnClick(MouseEvent mouseEvent) {
        var parent = loadFxml(Constants.BASE_FXML_URL + PATIENTS_FXML_URL);
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

}
