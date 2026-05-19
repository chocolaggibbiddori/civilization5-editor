package chocola.civilizationfiveeditor.v2;

import chocola.civilizationfiveeditor.v2.config.CivilizationConfiguration;
import chocola.civilizationfiveeditor.v2.loader.GameDataLoader;
import chocola.civilizationfiveeditor.v2.model.*;
import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private Button saveButton;
    @FXML
    private Button restoreButton;
    @FXML
    private Label statusLabel;
    @FXML
    private ListView<Civilization> civListView;
    @FXML
    private StackPane detailPane;
    @FXML
    private Label placeholderLabel;
    @FXML
    private VBox detailContent;
    @FXML
    private Label civNameLabel;
    @FXML
    private Label leaderLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private VBox traitBox;
    @FXML
    private VBox unitsBox;
    @FXML
    private VBox buildingsBox;
    @FXML
    private VBox improvementsBox;

    private final ObservableSet<Variable> changedChecker = FXCollections.observableSet();
    private Civilization currentCivilization;

    @FXML
    public void initialize() {
        GameDataLoader.load();

        CivilizationConfiguration
                .getCivilizationList()
                .forEach(civ -> civListView.getItems().add(civ));
        civListView.setCellFactory(listView -> new ListCell<>() {

            @Override
            protected void updateItem(Civilization civilization, boolean empty) {
                super.updateItem(civilization, empty);
                setText(empty || civilization == null ? null : civilization.getName());
            }
        });

        saveButton.disableProperty().bind(Bindings.isEmpty(changedChecker));
    }

    @FXML
    public void onSave(ActionEvent actionEvent) {
    }

    @FXML
    public void onRestore(ActionEvent actionEvent) {
    }

    @FXML
    public void onCivSelected(MouseEvent mouseEvent) {
        Civilization civilization = civListView.getSelectionModel().getSelectedItem();

        if (civilization == null || currentCivilization == civilization) {
            return;
        }

        currentCivilization = civilization;
        civNameLabel.setText(civilization.getName());
        leaderLabel.setText("지도자: " + civilization.getLeaderName());

        buildTraitsPanel(civilization);
        buildUnitsPanel(civilization);
        buildBuildingsPanel(civilization);
//        buildImprovementsPanel(civilization);

        placeholderLabel.setVisible(false);
        detailContent.setVisible(true);
        restoreButton.setDisable(false);
    }

    private void buildTraitsPanel(Civilization civilization) {
        ObservableList<Node> traitBoxChildren = clearAndReturn(traitBox);
        Trait trait = civilization.getTrait();

        VBox vBox = card();
        vBox.getChildren().add(sectionHeader(trait));
        vBox.getChildren().add(sectionBody(trait));

        traitBoxChildren.add(vBox);
    }

    private void buildUnitsPanel(Civilization civilization) {
        ObservableList<Node> unitsBoxChildren = clearAndReturn(unitsBox);
        UniqueUnit[] uniqueUnits = civilization.getUniqueUnits();

        if (uniqueUnits.length == 0) {
            unitsBoxChildren.add(new Label("고유 유닛 없음"));
            return;
        }

        for (UniqueUnit uniqueUnit : uniqueUnits) {
            VBox card = card();
            card.getChildren().add(sectionHeader(uniqueUnit));
            card.getChildren().add(sectionBody(uniqueUnit));

            unitsBoxChildren.add(card);
        }
    }

    private void buildBuildingsPanel(Civilization civilization) {
        ObservableList<Node> buildingsBoxChildren = clearAndReturn(buildingsBox);
        UniqueBuilding[] uniqueBuildings = civilization.getUniqueBuildings();

        if (uniqueBuildings.length == 0) {
            buildingsBoxChildren.add(new Label("고유 건물 없음"));
            return;
        }

        for (UniqueBuilding uniqueBuilding : uniqueBuildings) {
            VBox card = card();
            card.getChildren().add(sectionHeader(uniqueBuilding));
            card.getChildren().add(sectionBody(uniqueBuilding));

            buildingsBoxChildren.add(card);
        }
    }

    private ObservableList<Node> clearAndReturn(VBox vBox) {
        ObservableList<Node> children = vBox.getChildren();
        children.clear();

        return children;
    }

    private VBox card() {
        VBox box = new VBox(8);
        box.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 4; -fx-background-color: #fafafa; -fx-background-radius: 4; -fx-padding: 12;");

        return box;
    }

    private Label sectionHeader(Describable describable) {
        String text = describable.getDescription();

        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        return label;
    }

    private GridPane sectionBody(VariableAccessor variableAccessor) {
        List<Variable> traitVariableList = variableAccessor.getVariableList();
        GridPane grid = fieldGrid();
        int col = 0, row = 0;

        for (Variable variable : traitVariableList) {
            String key = variable.getKey();
            int value = variable.getValue();

            Label label = new Label(key + ":");
            label.setStyle("-fx-font-size: 12;");

            int width = 3;
            GridPane.setConstraints(label, col * width, row);

            TextField textField = numberField(value);
            textField.textProperty().addListener((obs, o, n) -> {
                variable.setValue(n);

                if (variable.isChanged()) {
                    changedChecker.add(variable);
                } else {
                    changedChecker.remove(variable);
                }
            });

            GridPane.setConstraints(textField, col * width + 1, row);
            grid.getChildren().addAll(label, textField);

            if (++col >= width) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private GridPane fieldGrid() {
        GridPane grid = new GridPane(8.0, 8.0);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints label = new ColumnConstraints(200);
            ColumnConstraints field = new ColumnConstraints(80);
            ColumnConstraints spacer = new ColumnConstraints(20);

            grid.getColumnConstraints().addAll(label, field, spacer);
        }

        return grid;
    }

    private TextField numberField(int val) {
        TextField tf = new TextField(String.valueOf(val));
        tf.setPrefWidth(80);
        tf.setStyle("-fx-font-size: 12;");
        tf.textProperty().addListener((obs, o, n) -> {
            if (!n.matches("-?\\d*")) {
                tf.setText(o);
            }
        });

        return tf;
    }
}
