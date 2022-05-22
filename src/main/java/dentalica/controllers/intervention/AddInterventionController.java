package dentalica.controllers.intervention;

import dentalica.models.Intervention;
import dentalica.util.DBUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private TextField descriptionFld;
    @FXML
    private TextField teethFld;
    @FXML
    private DatePicker intervenedAtFld;
    @FXML
    private TextField priceFld;
    @FXML
    private CheckBox payedCheckBox;

    private Integer patientId;

    private ObservableList<Intervention> interventionList;

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public void setInterventionList(ObservableList<Intervention> interventionList) {
        this.interventionList = interventionList;
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
            closeView(event);
        }
    }

    @FXML
    private void cleanFields() {
        typeFld.setText(null);
        descriptionFld.setText(null);
        teethFld.setText(null);
        intervenedAtFld.setValue(null);
        priceFld.setText(null);
        payedCheckBox.setSelected(false);
    }

    private void insert() {
        Integer interventionId = null;
        var connection = DBUtil.connect();
        var insertQuery = "INSERT INTO application.intervention (patient_id, type, description, teeth, price, intervened_at, payed, created_at) VALUES (?,?,?,?,?,?,?,?)";
        var selectQuery = "SELECT * FROM application.intervention i ORDER BY i.created_at desc LIMIT 1";
        try {
            var preparedInsertStatement = connection.prepareStatement(insertQuery);
            preparedInsertStatement.setInt(1, patientId);
            preparedInsertStatement.setString(2, typeFld.getText());
            preparedInsertStatement.setString(3, descriptionFld.getText());
            preparedInsertStatement.setString(4, teethFld.getText());
            preparedInsertStatement.setInt(5, preparePrice(priceFld.getText()));
            preparedInsertStatement.setObject(6, intervenedAtFld.getValue());
            preparedInsertStatement.setBoolean(7, payedCheckBox.isSelected());
            preparedInsertStatement.setObject(8, Timestamp.from(Instant.now()));
            preparedInsertStatement.execute();
            var preparedSelectStatement = connection.prepareStatement(selectQuery);
            var resultSet = preparedSelectStatement.executeQuery();
            if (resultSet.next()) {
                interventionId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            logger.error("Unable to insert intervention ", e);
            throw new RuntimeException();
        }
        interventionList.add(new Intervention(interventionId, typeFld.getText(), descriptionFld.getText(), teethFld.getText(), preparePrice(priceFld.getText()), intervenedAtFld.getValue(), payedCheckBox.getText()));
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

    private void closeView(MouseEvent event) {
        var node = (Node) event.getSource();
        var stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
