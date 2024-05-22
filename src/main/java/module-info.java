module com.empresa.javafx_mongo {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires java.sql;

    opens com.empresa.javafx_mongo to javafx.fxml;
    opens com.empresa.javafx_mongo.login to javafx.fxml;
    exports com.empresa.javafx_mongo;
}