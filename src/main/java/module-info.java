module chocola.civilizationfiveeditor {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.xml;
    requires org.dom4j;
    requires java.logging;

    opens chocola.civilizationfiveeditor to javafx.fxml;
    opens chocola.civilizationfiveeditor.v2 to javafx.fxml;

    exports chocola.civilizationfiveeditor;
    exports chocola.civilizationfiveeditor.v2;
}
