module com.mycompany.calcreflex {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.calcreflex to javafx.fxml;
    exports com.mycompany.calcreflex;
}
