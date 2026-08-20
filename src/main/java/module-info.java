module hr.algebra.humanitarnaorganizacija {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jdi;
    requires java.management;
    requires java.sql;
    requires jdk.dynalink;
    requires java.xml.crypto;
    requires jdk.jshell;
    requires java.desktop;
    requires com.h2database;
    requires org.slf4j;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens hr.algebra.humanitarnaorganizacija to javafx.fxml;
    opens hr.algebra.humanitarnaorganizacija.controller to javafx.fxml;
    opens hr.algebra.humanitarnaorganizacija.model to javafx.base;
    exports hr.algebra.humanitarnaorganizacija;
}