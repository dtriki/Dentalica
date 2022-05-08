package dentalica.controllers.intervention;

import dentalica.util.DBUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class AddInterventionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TextField typeFld;
    @FXML
    private TextField teethFld;
    @FXML
    private DatePicker intervenedAtFld;
    @FXML
    private TextField priceFld;
    @FXML
    private CheckBox payedCheckBox;

    private Integer patientId;

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public void initData(Integer patientId) {
        setPatientId(patientId);
    }

    @FXML
    private void saveIntervention(MouseEvent event) {
        Alert alert = null;
        var type = typeFld.getText();
        var intervenedAt = intervenedAtFld.getValue();
        if (type != null && !type.isEmpty() && intervenedAt != null) {
            insert();
            cleanFields();
        } else {
            alert = showSaveInterventionAlert(alert);
        }
        if (alert == null) {
            closeView();
        }
    }

    @FXML
    private void cleanFields() {
        typeFld.setText(null);
        teethFld.setText(null);
        intervenedAtFld.setValue(null);
        priceFld.setText(null);
        payedCheckBox.setSelected(false);
    }

    private void insert() {
        var connection = DBUtil.connect();
        var query = "INSERT INTO application.intervention (patient_id, type, teeth, price, intervened_at, payed, created_at) VALUES (?,?,?,?,?,?,?)";
        try {
            var preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setString(2, typeFld.getText());
            preparedStatement.setString(3, teethFld.getText());
            preparedStatement.setInt(4, preparePrice(priceFld.getText()));
            preparedStatement.setObject(5, intervenedAtFld.getValue());
            preparedStatement.setBoolean(6, payedCheckBox.isSelected());
            preparedStatement.setObject(7, Timestamp.from(Instant.now()));
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to insert intervention ", e);
            throw new RuntimeException();
        }
    }

    private Integer preparePrice(String price) {
        return price.isEmpty() ? 0 : Integer.valueOf(priceFld.getText());
    }

    private Alert showSaveInterventionAlert(Alert alert) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Polja tip i datum su obavezna");
        alert.showAndWait();
        return alert;
    }

    private void closeView() {
        var thisStage = (Stage) typeFld.getScene().getWindow();
        thisStage.close();
    }

}
