package dentalica.controllers.fees;

import dentalica.Constants;
import dentalica.controllers.intervention.InterventionsController;
import dentalica.models.Fee;
import dentalica.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class FeesController implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TableView<Fee> feesTable;
    @FXML
    private TableColumn<Fee, String> colPatientName;
    @FXML
    private TableColumn<Fee, String> colPatientSurname;
    @FXML
    private TableColumn<Fee, String> colPatientNumber;
    @FXML
    private TableColumn<Fee, Integer> colDebt;
    @FXML
    private TextField searchFld;

    private ObservableList<Fee> feeList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
        filterData();
    }

    private void loadData() {
        feeList.clear();
        var connection = DBUtil.connect();
        var query = "SELECT p.id, p.name, p.surname, p.number, SUM(i.price) AS debt FROM application.patient p " +
                "LEFT JOIN application.intervention i ON p.id = i.patient_id WHERE i.payed IS FALSE AND i.price != 0 GROUP BY p.id ORDER BY debt DESC";
        try {
            var preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();
            populateFeeTable(resultSet);
        } catch (SQLException ex) {
            logger.error("Unable to connect to database, cause: ", ex);
        }
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colPatientSurname.setCellValueFactory(new PropertyValueFactory<>("patientSurname"));
        colPatientNumber.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
        colDebt.setCellValueFactory(new PropertyValueFactory<>("debt"));
    }

    private void populateFeeTable(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            feeList.add(new Fee(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("number"),
                    resultSet.getInt("debt")));
        }
        feesTable.setItems(feeList);
    }

    private void filterData() {
        var filteredData = new FilteredList<>(feeList, b -> true);
        searchFld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(fee -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                var text = searchFld.getText().toLowerCase();
                if (fee.getPatientName().toLowerCase().indexOf(text) > -1) {
                    return true;
                } else if (fee.getPatientSurname().toLowerCase().indexOf(text) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        var sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(feesTable.comparatorProperty());
        feesTable.setItems(sortedData);
    }

    @FXML
    private void getInterventionsView(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() != 2) {
            return;
        }
        var fee = feesTable.getSelectionModel().getSelectedItem();
        var viewPath = Constants.BASE_FXML_URL + Constants.INTERVENTIONS_FXML_URL;
        var loader = new FXMLLoader(getClass().getResource(viewPath));
        var stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InterventionsController controller = loader.getController();
        controller.initData(fee.getPatientId());
        stage.setResizable(false);
        stage.show();
    }

}
