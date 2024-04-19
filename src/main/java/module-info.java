module edu.guilford {
    requires javafx.base;
    requires javafx.swing;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.logging;
    requires com.google.gson;
    requires javafx.graphics;
    requires imgscalr.lib;

    opens edu.guilford to javafx.fxml, com.google.gson;

    exports edu.guilford;
}
