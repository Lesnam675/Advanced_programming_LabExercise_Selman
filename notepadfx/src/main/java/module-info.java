module com.aastu.notepadfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.aastu.notepadfx to javafx.fxml;
    exports com.aastu.notepadfx;
}