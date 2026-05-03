module chocola.civilizationfiveeditor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens chocola.civilizationfiveeditor to javafx.fxml;
    exports chocola.civilizationfiveeditor;
}