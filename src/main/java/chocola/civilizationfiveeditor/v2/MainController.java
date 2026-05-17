package chocola.civilizationfiveeditor.v2;

import chocola.civilizationfiveeditor.v2.config.CivilizationConfiguration;
import chocola.civilizationfiveeditor.v2.loader.GameDataLoader;
import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import chocola.civilizationfiveeditor.v2.model.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private VBox traitsBox;
    @FXML
    private VBox unitsBox;
    @FXML
    private VBox buildingsBox;
    @FXML
    private VBox improvementsBox;

    private GameData gameData;
    private Civilization currentCivilization;

    @FXML
    public void initialize() {
        gameData = GameDataLoader.load();

        CivilizationConfiguration
                .getCivilizationList()
                .forEach(civ -> civListView.getItems().add(civ));
        civListView.setCellFactory(listView -> new ListCell<>() {

            @Override
            protected void updateItem(Civilization civilization, boolean empty) {
                super.updateItem(civilization, empty);
                setText(empty || civilization == null ? null : civilization.getKoreanName(gameData));
            }
        });
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
        civNameLabel.setText(civilization.getKoreanName(gameData));
        leaderLabel.setText("지도자: " + civilization.getLeaderKoreanName(gameData));

//        buildTraitsPanel(civilization);
//        buildUnitsPanel(civilization);
//        buildBuildingsPanel(civilization);
//        buildImprovementsPanel(civilization);

        placeholderLabel.setVisible(false);
        detailContent.setVisible(true);
        saveButton.setDisable(true);
        restoreButton.setDisable(false);
    }

}
