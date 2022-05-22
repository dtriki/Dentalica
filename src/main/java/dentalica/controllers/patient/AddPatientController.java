package dentalica.controllers.patient;

import dentalica.models.Patient;
import dentalica.util.DBUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

public class AddPatientController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TextField nameFld;
    @FXML
    private TextField surnameFld;
    @FXML
    private DatePicker birthFld;
    @FXML
    private TextField numberFld;
    @FXML
    private TextField emailFld;
    @FXML
    private TextField addressFld;

    private ObservableList<Patient> patientList;

    public void setPatientList(ObservableList<Patient> patientList) {
        this.patientList = patientList;
    }

    @FXML
    private void savePatient(MouseEvent event) {
        Alert alert = null;
        var name = nameFld.getText();
        var surname = surnameFld.getText();
        var number = numberFld.getText();
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty() && number != null && !number.isEmpty()) {
            insert();
            cleanFields();
        } else {
            alert = showSavePatientAlert(alert);
        }
        if (alert == null) {
            closeView(event);
        }
    }

    private void insert() {
        Integer patientId = null;
        var connection = DBUtil.connect();
        var insertQuery = "INSERT INTO application.patient (name, surname, birth, number, email, address, created_at) VALUES (?,?,?,?,?,?,?)";
        var selectQuery = "SELECT * FROM application.patient p ORDER BY p.created_at desc LIMIT 1";
        try {
            var preparedInsertStatement = connection.prepareStatement(insertQuery);
            preparedInsertStatement.setString(1, nameFld.getText());
            preparedInsertStatement.setString(2, surnameFld.getText());
            preparedInsertStatement.setDate(3, parseBirthDate(birthFld.getValue()));
            preparedInsertStatement.setString(4, numberFld.getText());
            preparedInsertStatement.setString(5, emailFld.getText());
            preparedInsertStatement.setString(6, addressFld.getText());
            preparedInsertStatement.setObject(7, Timestamp.from(Instant.now()));
            preparedInsertStatement.execute();
            var preparedSelectStatement = connection.prepareStatement(selectQuery);
            var resultSet = preparedSelectStatement.executeQuery();
            if (resultSet.next()) {
                patientId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.error("Unable to insert patient ", e);
            throw new RuntimeException();
        }
        patientList.add(new Patient(patientId, nameFld.getText(), surnameFld.getText(), birthFld.getValue(), numberFld.getText(), emailFld.getText(), addressFld.getText()));
    }

    private Date parseBirthDate(LocalDate birth) {
        return birth != null ? Date.valueOf(birth) : null;
    }

    @FXML
    private void cleanFields() {
        nameFld.setText(null);
        surnameFld.setText(null);
        birthFld.setValue(null);
        numberFld.setText(null);
        emailFld.setText(null);
        addressFld.setText(null);
    }

    private Alert showSavePatientAlert(Alert alert) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Ime, prezime i telefon su obavezni");
        alert.showAndWait();
        return alert;
    }

    private void closeView(MouseEvent event) {
        var node = (Node) event.getSource();
        var stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
