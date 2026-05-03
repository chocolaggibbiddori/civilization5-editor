module chocola.civilizationfiveeditor {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.xml;

    opens chocola.civilizationfiveeditor to javafx.fxml;
    exports chocola.civilizationfiveeditor;
}
