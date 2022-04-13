module dentalica {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.postgresql.jdbc;
    requires org.slf4j;
    requires java.sql;
    requires java.naming;

    opens dentalica.controllers to javafx.fxml;
    opens dentalica.models to javafx.base;

    exports dentalica;
    exports dentalica.controllers;
}
