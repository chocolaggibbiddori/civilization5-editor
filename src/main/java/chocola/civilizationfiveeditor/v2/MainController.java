package chocola.civilizationfiveeditor.v2;

import chocola.civilizationfiveeditor.v2.config.CivilizationConfiguration;
import chocola.civilizationfiveeditor.v2.loader.GameDataLoader;
import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import chocola.civilizationfiveeditor.v2.model.civilization.Trait;
import chocola.civilizationfiveeditor.v2.model.civilization.TraitVariable;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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
    private TextField searchField;
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

    private Civilization currentCivilization;
    private ObservableSet<TraitVariable> changedChecker = FXCollections.observableSet();

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
    public void onSearch(KeyEvent keyEvent) {
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
//        buildUnitsPanel(civilization);
//        buildBuildingsPanel(civilization);
//        buildImprovementsPanel(civilization);

        placeholderLabel.setVisible(false);
        detailContent.setVisible(true);
        saveButton.setDisable(true);
        restoreButton.setDisable(false);
    }

    private void buildTraitsPanel(Civilization civilization) {
        ObservableList<Node> traitBoxChildren = traitBox.getChildren();
        traitBoxChildren.clear();
        Trait trait = civilization.getTrait();

        VBox vBox = card();
        vBox.getChildren().add(sectionHeader(trait.getDescription()));

        GridPane grid = fieldGrid();
        List<TraitVariable> traitVariableList = trait.getVariableList();

        int col = 0, row = 0;
        for (TraitVariable variable : traitVariableList) {
            String key = variable.getKey();
            int value = variable.getValue();

            Label label = new Label(key + ":");
            label.setStyle("-fx-font-size: 12;");
            GridPane.setConstraints(label, col * 3, row);

            TextField textField = numberField(value);
            textField.textProperty().addListener((obs, o, n) -> {
                variable.setValue(n);

                if (variable.isChanged()) {
                    changedChecker.add(variable);
                } else {
                    changedChecker.remove(variable);
                }
            });

            GridPane.setConstraints(textField, col * 3 + 1, row);
            grid.getChildren().addAll(label, textField);

            if (++col >= 3) {
                col = 0;
                row++;
            }
        }

        vBox.getChildren().add(grid);
        traitBoxChildren.add(vBox);
    }

    private VBox card() {
        VBox box = new VBox(8);
        box.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 4; -fx-background-color: #fafafa; -fx-background-radius: 4; -fx-padding: 12;");

        return box;
    }

    private Label sectionHeader(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        return label;
    }

    private GridPane fieldGrid() {
        GridPane grid = new GridPane(8.0, 8.0);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints label = new ColumnConstraints(160);
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
