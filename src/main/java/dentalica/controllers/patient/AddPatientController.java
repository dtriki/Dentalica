package dentalica.controllers.patient;

import dentalica.util.DBUtil;
import javafx.fxml.FXML;
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
            closeView();
        }
    }

    private void insert() {
        var connection = DBUtil.connect();
        var query = "INSERT INTO application.patient (name, surname, birth, number, email, address, created_at) VALUES (?,?,?,?,?,?,?)";
        try {
            var preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFld.getText());
            preparedStatement.setString(2, surnameFld.getText());
            preparedStatement.setDate(3, parseBirthDate(birthFld.getValue()));
            preparedStatement.setString(4, numberFld.getText());
            preparedStatement.setString(5, emailFld.getText());
            preparedStatement.setString(6, addressFld.getText());
            preparedStatement.setObject(7, Timestamp.from(Instant.now()));
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to insert patient ", e);
            throw new RuntimeException();
        }
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

    private void closeView() {
        var thisStage = (Stage) nameFld.getScene().getWindow();
        thisStage.close();
    }

}
