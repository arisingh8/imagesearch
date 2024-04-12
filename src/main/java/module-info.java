module edu.guilford {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.logging;
    requires com.google.gson;
    requires javafx.graphics;

    opens edu.guilford to javafx.fxml, com.google.gson;

    exports edu.guilford;
}
