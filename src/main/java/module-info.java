module dentalica {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.postgresql.jdbc;
    requires org.slf4j;
    requires java.sql;
    requires java.naming;

    opens dentalica.models to javafx.base;

    exports dentalica;
    exports dentalica.controllers.intervention;
    opens dentalica.controllers.intervention to javafx.fxml;
    exports dentalica.controllers.patient;
    opens dentalica.controllers.patient to javafx.fxml;
    exports dentalica.controllers.dashboard;
    opens dentalica.controllers.dashboard to javafx.fxml;
    exports dentalica.controllers.fees;
    opens dentalica.controllers.fees to javafx.fxml;
}
