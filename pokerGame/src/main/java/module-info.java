module com.aastu.poker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.aastu.poker to javafx.fxml;
    exports com.aastu.poker;
}