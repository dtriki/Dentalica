package dentalica.controllers;

import dentalica.Constants;
import dentalica.models.Patient;
import dentalica.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PatientsController implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ADD_PATIENT_FXML_URL = "/addPatient.fxml";

    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, String> colName;
    @FXML
    private TableColumn<Patient, String> colSurname;
    @FXML
    private TableColumn<Patient, String> colNumber;

    ObservableList<Patient> patientList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }

    @FXML
    private void getAddView(MouseEvent event) {
        var parent = loadFxml(Constants.BASE_FXML_URL + ADD_PATIENT_FXML_URL);
        var scene = new Scene(parent);
        var stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
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
    private void searchTable(MouseEvent event) {
    }

    private void loadData() {
        refreshTable();
    }

    @FXML
    private void refreshTable() {
        patientList.clear();
        var connection = DBUtil.connect();
        var query = "SELECT * FROM application.patient";
        try {
            var preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();
            populatePatientsTable(resultSet);
        } catch (SQLException ex) {
            logger.error("Unable to connect to database");
        }
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
    }

    private void populatePatientsTable(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            patientList.add(new Patient(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("number")));
            patientsTable.setItems(patientList);
        }
    }

}
