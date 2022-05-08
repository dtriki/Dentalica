package dentalica.controllers.patient;

import dentalica.models.Patient;
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

public class EditPatientController {

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

    private Patient patient;

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void initData(Patient patient) {
        setPatient(patient);
        nameFld.setText(patient.getName());
        surnameFld.setText(patient.getSurname());
        birthFld.setValue(patient.getBirth());
        numberFld.setText(patient.getNumber());
        emailFld.setText(patient.getEmail());
        addressFld.setText(patient.getAddress());
    }

    @FXML
    private void savePatient(MouseEvent event) {
        var name = nameFld.getText();
        var surname = surnameFld.getText();
        var number = numberFld.getText();
        if (name != null && !name.isEmpty() && surname != null && !surname.isEmpty() && number != null && !number.isEmpty()) {
            update();
            cleanFields();
        } else {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Ime, prezime i telefon su obavezni");
            alert.showAndWait();
        }
        var thisStage = (Stage) nameFld.getScene().getWindow();
        thisStage.close();
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

    private void update() {
        var connection = DBUtil.connect();
        var query = "UPDATE application.patient SET " +
                "name = ?, " +
                "surname = ?, " +
                "birth = ?, " +
                "number = ?, " +
                "email = ?, " +
                "address = ? " +
                "WHERE id = " + patient.getId();
        try {
            var preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFld.getText());
            preparedStatement.setString(2, surnameFld.getText());
            preparedStatement.setDate(3, Date.valueOf(birthFld.getValue()));
            preparedStatement.setString(4, numberFld.getText());
            preparedStatement.setString(5, emailFld.getText());
            preparedStatement.setString(6, addressFld.getText());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to edit patient ", e);
            throw new RuntimeException();
        }
    }

}
