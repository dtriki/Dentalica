package dentalica.controllers.intervention;

import dentalica.Constants;
import dentalica.models.Intervention;
import dentalica.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InterventionsController implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ADD_INTERVENTION_FXML_URL = "/addIntervention.fxml";
    private static final String EDIT_INTERVENTION_FXML_URL = "/editIntervention.fxml";

    @FXML
    private TableView<Intervention> interventionsTable;
    @FXML
    private TableColumn<Intervention, String> colType;
    @FXML
    private TableColumn<Intervention, String> colTeeth;
    @FXML
    private TableColumn<Intervention, String> colPrice;
    @FXML
    private TableColumn<Intervention, Timestamp> colIntervenedAt;
    @FXML
    private TableColumn<Intervention, Boolean> colPayed;
    @FXML
    private TextField searchFld;

    private ObservableList<Intervention> interventionList = FXCollections.observableArrayList();
    private Integer patientId;

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initData(Integer patientId) {
        setPatientId(patientId);
        loadData();
        filterData();
    }

    private void loadData() {
        interventionList.clear();
        var connection = DBUtil.connect();
        var query = "SELECT * FROM application.intervention ";
        if (patientId != null) {
            query = query.concat("WHERE patient_id = " + patientId);
        }
        query = query.concat(" ORDER BY intervened_at DESC");
        try {
            var preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();
            populateInterventionsTable(resultSet);
        } catch (SQLException ex) {
            logger.error("Unable to connect to database");
        }
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTeeth.setCellValueFactory(new PropertyValueFactory<>("teeth"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colIntervenedAt.setCellValueFactory(new PropertyValueFactory<>("intervenedAt"));
        colPayed.setCellValueFactory(new PropertyValueFactory<>("payed"));
    }

    private void populateInterventionsTable(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            interventionList.add(new Intervention(
                    resultSet.getInt("id"),
                    resultSet.getString("type"),
                    resultSet.getString("teeth"),
                    resultSet.getInt("price"),
                    resultSet.getObject("intervened_at", LocalDate.class),
                    mapPayedToString(resultSet.getBoolean("payed"))));
        }
        interventionsTable.setItems(interventionList);
    }

    private String mapPayedToString(Boolean payed) {
        return payed ? "Da" : "Ne";
    }

    private void filterData() {
        var filteredData = new FilteredList<>(interventionList, b -> true);
        searchFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(intervention -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                var text = searchFld.getText().toLowerCase();
                if (intervention.getType().toLowerCase().indexOf(text) > -1) {
                    return true;
                } else if (intervention.getTeeth().toLowerCase().indexOf(text) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        var sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(interventionsTable.comparatorProperty());
        interventionsTable.setItems(sortedData);
    }

    @FXML
    private void getAddInterventionView(MouseEvent event) {
        var viewPath = Constants.BASE_FXML_URL + ADD_INTERVENTION_FXML_URL;
        var loader = new FXMLLoader(getClass().getResource(viewPath));
        var stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AddInterventionController controller = loader.getController();
        controller.initData(patientId);
        stage.show();
    }

    @FXML
    private void refreshTable() {
        loadData();
    }

    @FXML
    private void getEditInterventionView(MouseEvent event) {
        var intervention = interventionsTable.getSelectionModel().getSelectedItem();
        var viewPath = Constants.BASE_FXML_URL + EDIT_INTERVENTION_FXML_URL;
        var loader = new FXMLLoader(getClass().getResource(viewPath));
        var stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EditInterventionController controller = loader.getController();
        controller.initData(intervention);
        stage.show();
    }

    @FXML
    private void deleteIntervention(MouseEvent event) {
        var confirmButton = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
        var rejectButton = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);
        var contentText = "Da li ste sigurni da želite da izbrišete intervenciju ?";
        var alert = new Alert(Alert.AlertType.CONFIRMATION, contentText, confirmButton, rejectButton);
        alert.setHeaderText(null);
        alert.setTitle("Potvrda");
        var result = alert.showAndWait();
        if (result.get() == confirmButton) {
            var intervention = interventionsTable.getSelectionModel().getSelectedItem();
            delete(intervention.getId());
        }
    }

    private void delete(Integer interventionId) {
        var connection = DBUtil.connect();
        var interventionsQuery = "DELETE FROM application.intervention WHERE id = " + interventionId;
        try {
            var preparedStatement = connection.prepareStatement(interventionsQuery);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to delete intervention ", e);
            throw new RuntimeException();
        }
        refreshTable();
    }

}
