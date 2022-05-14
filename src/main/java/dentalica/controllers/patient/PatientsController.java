package dentalica.controllers.patient;

import dentalica.Constants;
import dentalica.controllers.intervention.InterventionsController;
import dentalica.models.Patient;
import dentalica.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PatientsController implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ADD_PATIENT_FXML_URL = "/addPatient.fxml";
    private static final String EDIT_PATIENT_FXML_URL = "/editPatient.fxml";

    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, String> colName;
    @FXML
    private TableColumn<Patient, String> colSurname;
    @FXML
    private TableColumn<Patient, String> colNumber;
    @FXML
    private TextField searchFld;

    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshTable();
        filterData();
    }

    @FXML
    public void refreshTable() {
        patientList.clear();
        var connection = DBUtil.connect();
        var query = "SELECT * FROM application.patient";
        try {
            var preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();
            populatePatientsTable(resultSet);
        } catch (SQLException ex) {
            logger.error("Unable to connect to database, cause: ", ex);
        }
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
    }

    private void populatePatientsTable(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            patientList.add(new Patient(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getObject("birth", LocalDate.class),
                    resultSet.getString("number"),
                    resultSet.getString("email"),
                    resultSet.getString("address")));
        }
        patientsTable.setItems(patientList);
    }

    private void filterData() {
        var filteredData = new FilteredList<>(patientList, b -> true);
        searchFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                var text = searchFld.getText().toLowerCase();
                if (patient.getName().toLowerCase().indexOf(text) > -1) {
                    return true;
                } else if (patient.getSurname().toLowerCase().indexOf(text) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        var sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(patientsTable.comparatorProperty());
        patientsTable.setItems(sortedData);
    }

    @FXML
    private void getAddPatientView(MouseEvent event) {
        var parent = loadFxml(Constants.BASE_FXML_URL + ADD_PATIENT_FXML_URL);
        var scene = new Scene(parent);
        var stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
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
    private void getInterventionsView(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            var patient = patientsTable.getSelectionModel().getSelectedItem();
            var viewPath = Constants.BASE_FXML_URL + Constants.INTERVENTIONS_FXML_URL;
            var loader = new FXMLLoader(getClass().getResource(viewPath));
            var stage = new Stage(StageStyle.DECORATED);
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            InterventionsController controller = loader.getController();
            controller.initData(patient.getId());
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    private void getEditPatientView(MouseEvent event) {
        var patient = patientsTable.getSelectionModel().getSelectedItem();
        var viewPath = Constants.BASE_FXML_URL + EDIT_PATIENT_FXML_URL;
        var loader = new FXMLLoader(getClass().getResource(viewPath));
        var stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EditPatientController controller = loader.getController();
        controller.initData(patient);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void deletePatient(MouseEvent event) {
        var confirmButton = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
        var rejectButton = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);
        var contentText = "Da li ste sigurni da želite da izbrišete pacijenta i sve njegove intervencije ?";
        var alert = new Alert(Alert.AlertType.CONFIRMATION, contentText, confirmButton, rejectButton);
        alert.setHeaderText(null);
        alert.setTitle("Potvrda");
        var result = alert.showAndWait();
        if (result.get() == confirmButton) {
            var patient = patientsTable.getSelectionModel().getSelectedItem();
            delete(patient.getId());
        }
    }

    private void delete(Integer patientId) {
        var connection = DBUtil.connect();
        var interventionsQuery = "DELETE FROM application.intervention WHERE patient_id = " + patientId;
        var patientQuery = "DELETE FROM application.patient WHERE id = " + patientId;
        try {
            var preparedStatement = connection.prepareStatement(interventionsQuery);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(patientQuery);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to delete patient ", e);
            throw new RuntimeException();
        }
        refreshTable();
    }

}
