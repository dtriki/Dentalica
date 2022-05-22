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

public class EditInterventionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PAYED_VALUE = "Da";

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

    private Intervention intervention;

    private ObservableList<Intervention> interventionList;

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public void setInterventionList(ObservableList<Intervention> interventionList) {
        this.interventionList = interventionList;
    }

    public void initData(Intervention intervention) {
        setIntervention(intervention);
        typeFld.setText(intervention.getType());
        descriptionFld.setText(intervention.getDescription());
        teethFld.setText(intervention.getTeeth());
        intervenedAtFld.setValue(intervention.getIntervenedAt());
        priceFld.setText(intervention.getPrice().toString());
        payedCheckBox.setSelected(mapPayedToBoolean(intervention.getPayed()));
    }

    private Boolean mapPayedToBoolean(String payed) {
        return PAYED_VALUE.equals(payed) ? true : false;
    }

    @FXML
    private void saveIntervention(MouseEvent event) {
        Alert alert = null;
        var type = typeFld.getText();
        var intervenedAt = intervenedAtFld.getValue();
        if (type != null && !type.isEmpty() && intervenedAt != null) {
            update();
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

    private void update() {
        var connection = DBUtil.connect();
        var query = "UPDATE application.intervention SET " +
                "type = ?, " +
                "description = ?, " +
                "teeth = ?, " +
                "price = ?, " +
                "intervened_at = ?, " +
                "payed = ? " +
                "WHERE id = " + intervention.getId();
        var payedd = payedCheckBox.isSelected();
        try {
            var preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, typeFld.getText());
            preparedStatement.setString(2, descriptionFld.getText());
            preparedStatement.setString(3, teethFld.getText());
            preparedStatement.setInt(4, preparePrice(priceFld.getText()));
            preparedStatement.setObject(5, intervenedAtFld.getValue());
            preparedStatement.setBoolean(6, payedd);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to edit intervention ", e);
            throw new RuntimeException();
        }
        var updatedIntervention = new Intervention(intervention.getId(), typeFld.getText(), descriptionFld.getText(), teethFld.getText(), preparePrice(priceFld.getText()), intervenedAtFld.getValue(), mapPayedToString(payedCheckBox.isSelected()));
        updateInterventionTable(updatedIntervention);
    }

    private Integer preparePrice(String price) {
        return price.isEmpty() ? 0 : Integer.valueOf(priceFld.getText());
    }

    private String mapPayedToString(Boolean payed) {
        return payed ? "Da" : "Ne";
    }

    private void updateInterventionTable(Intervention updatedIntervention) {
        for (var i = 0; i < interventionList.size(); i++) {
            if (intervention.getId() == interventionList.get(i).getId()) {
                interventionList.set(i, updatedIntervention);
            }
        }
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
