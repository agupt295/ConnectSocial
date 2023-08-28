module Phase3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml;
    requires java.desktop;
    opens com.example.phase3 to javafx.fxml;
    exports com.example.phase3;
}