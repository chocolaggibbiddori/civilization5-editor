package chocola.civilizationfiveeditor;

import chocola.civilizationfiveeditor.model.BuildingData;
import chocola.civilizationfiveeditor.model.CivEntry;
import chocola.civilizationfiveeditor.model.GameData;
import chocola.civilizationfiveeditor.model.ImprovementData;
import chocola.civilizationfiveeditor.model.TraitData;
import chocola.civilizationfiveeditor.model.UnitData;
import chocola.civilizationfiveeditor.service.XmlDataLoader;
import chocola.civilizationfiveeditor.service.XmlDataSaver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.IntConsumer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class MainController {

    private static final Path DEFAULT_GAME_PATH = Path.of("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Sid Meier's Civilization V");

    private final ObservableList<CivEntry> allCivs = FXCollections.observableArrayList();
    private final ObservableList<CivEntry> filteredCivs = FXCollections.observableArrayList();
    private final Set<Path> dirtyFiles = new HashSet<>();

    private XmlDataSaver saver;

    @FXML
    private Button loadButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button restoreButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<CivEntry> civListView;
    @FXML
    private Label placeholderLabel;
    @FXML
    private VBox detailContent;
    @FXML
    private Label civNameLabel;
    @FXML
    private Label leaderLabel;
    @FXML
    private VBox traitsBox;
    @FXML
    private VBox unitsBox;
    @FXML
    private VBox buildingsBox;
    @FXML
    private VBox improvementsBox;
    private GameData gameData;
    private CivEntry currentCiv;
    private Path currentRoot;

    @FXML
    public void initialize() {
        civListView.setItems(filteredCivs);
        civListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CivEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : civKoreanName(item));
            }
        });
        if (Files.exists(DEFAULT_GAME_PATH)) {
            loadFrom(DEFAULT_GAME_PATH);
        }
    }

    // ── Event handlers ─────────────────────────────────────────────────────

    @FXML
    private void onLoad() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Civilization V 게임 폴더 선택");
        if (Files.exists(DEFAULT_GAME_PATH)) chooser.setInitialDirectory(DEFAULT_GAME_PATH.toFile());
        File selected = chooser.showDialog(loadButton.getScene().getWindow());
        if (selected == null) {
            return;
        }
        loadFrom(selected.toPath());
    }

    private void loadFrom(Path root) {
        loadFrom(root, null);
    }

    private void loadFrom(Path root, String selectCivType) {
        currentRoot = root;
        saver = new XmlDataSaver(root);
        statusLabel.setText("로딩 중...");
        loadButton.setDisable(true);

        new Thread(() -> {
            try {
                GameData data = new XmlDataLoader().load(root);
                Platform.runLater(() -> {
                    gameData = data;
                    currentCiv = null;
                    allCivs.setAll(data.getCivilizations());
                    filteredCivs.setAll(allCivs);
                    statusLabel.setText("문명 " + data.getCivilizations().size() + "개 로드 완료");
                    loadButton.setDisable(false);

                    if (selectCivType != null) {
                        filteredCivs.stream()
                                .filter(c -> c.getType().equals(selectCivType))
                                .findFirst()
                                .ifPresent(civ -> {
                                    civListView.getSelectionModel().select(civ);
                                    currentCiv = civ;
                                    showCiv(civ);
                                });
                    } else {
                        detailContent.setVisible(false);
                        placeholderLabel.setVisible(true);
                        restoreButton.setDisable(true);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("로드 실패: " + e.getMessage());
                    loadButton.setDisable(false);
                    showError("로드 오류", e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void onSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isBlank()) {
            filteredCivs.setAll(allCivs);
        } else {
            filteredCivs.setAll(allCivs.stream()
                    .filter(c -> civKoreanName(c).toLowerCase().contains(query)
                              || c.getDisplayName().toLowerCase().contains(query))
                    .toList());
        }
    }

    @FXML
    private void onCivSelected() {
        CivEntry selected = civListView.getSelectionModel().getSelectedItem();
        if (selected == null || selected == currentCiv) {
            return;
        }
        currentCiv = selected;
        showCiv(selected);
    }

    @FXML
    private void onSave() {
        if (gameData == null || currentCiv == null) {
            return;
        }
        try {
            saveCurrentCiv();
            statusLabel.setText("저장 완료");
            saveButton.setDisable(true);
            dirtyFiles.clear();
        } catch (Exception e) {
            showError("저장 오류", e.getMessage());
        }
    }

    @FXML
    private void onRestore() {
        if (currentCiv == null || gameData == null) {
            return;
        }

        Set<Path> sourceFiles = collectCivSourceFiles();
        boolean anyBackup = sourceFiles.stream().anyMatch(saver::hasBackup);
        if (!anyBackup) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("복원 불가");
            info.setHeaderText(null);
            info.setContentText("저장된 백업 파일이 없습니다.\n한 번 이상 저장해야 복원이 가능합니다.");
            info.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("원본 복원");
        confirm.setHeaderText(null);
        confirm.setContentText("저장된 변경 내용이 모두 삭제됩니다. 원본으로 복원하시겠습니까?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        for (Path file : sourceFiles) {
            try {
                saver.restoreBackup(file);
            } catch (Exception e) {
                showError("복원 오류", e.getMessage());
                return;
            }
        }

        String civType = currentCiv.getType();
        loadFrom(currentRoot, civType);
    }

    // ── Display ────────────────────────────────────────────────────────────

    private void showCiv(CivEntry civ) {
        civNameLabel.setText(civKoreanName(civ));
        leaderLabel.setText(formatLeader(civ.getLeaderType()));

        buildTraitsPanel(civ);
        buildUnitsPanel(civ);
        buildBuildingsPanel(civ);
        buildImprovementsPanel(civ);

        placeholderLabel.setVisible(false);
        detailContent.setVisible(true);
        dirtyFiles.clear();
        saveButton.setDisable(true);
        restoreButton.setDisable(false);
    }

    private void buildTraitsPanel(CivEntry civ) {
        traitsBox.getChildren().clear();

        if (civ.getTraitTypes().isEmpty()) {
            traitsBox.getChildren().add(new Label("특성 정보 없음"));
            return;
        }

        for (String traitType : civ.getTraitTypes()) {
            TraitData trait = gameData.getTrait(traitType);
            if (trait == null) {
                continue;
            }

            VBox card = card();
            card.getChildren().add(sectionHeader(koreanText(trait.getDescription())));

            if (trait.getValues().isEmpty()) {
                card.getChildren().add(new Label("수치 데이터 없음"));
            } else {
                GridPane grid = fieldGrid();
                int col = 0, row = 0;
                for (Map.Entry<String, String> entry : trait.getValues().entrySet()) {
                    String key = entry.getKey();
                    String val = entry.getValue();
                    boolean isBool = "true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val);

                    Label label = new Label(key + ":");
                    label.setStyle("-fx-font-size: 12;");
                    GridPane.setConstraints(label, col * 3, row);

                    if (isBool) {
                        CheckBox cb = new CheckBox();
                        cb.setSelected("true".equalsIgnoreCase(val));
                        cb.selectedProperty().addListener((obs, o, n) -> {
                            trait.setValue(key, n.toString());
                            markDirty(trait.getSourceFile());
                        });
                        GridPane.setConstraints(cb, col * 3 + 1, row);
                        grid.getChildren().addAll(label, cb);
                    } else {
                        TextField tf = numField(val);
                        tf.textProperty().addListener((obs, o, n) -> {
                            trait.setValue(key, n);
                            markDirty(trait.getSourceFile());
                        });
                        GridPane.setConstraints(tf, col * 3 + 1, row);
                        grid.getChildren().addAll(label, tf);
                    }

                    col++;
                    if (col >= 3) {
                        col = 0;
                        row++;
                    }
                }
                card.getChildren().add(grid);
            }
            traitsBox.getChildren().add(card);
        }
    }

    private void buildUnitsPanel(CivEntry civ) {
        unitsBox.getChildren().clear();

        if (civ.getUniqueUnitTypes().isEmpty()) {
            unitsBox.getChildren().add(new Label("고유 유닛 없음"));
            return;
        }

        for (String unitType : civ.getUniqueUnitTypes()) {
            UnitData unit = gameData.getUnit(unitType);
            if (unit == null) {
                continue;
            }

            VBox card = card();
            card.getChildren().add(sectionHeader(koreanText(unit.getDescription())));

            GridPane grid = fieldGrid();
            addIntField(grid, "전투력 (Combat)",       0, 0, unit.getCombat(),       unit::setCombat,       unit.getSourceFile());
            addIntField(grid, "원거리 전투력 (Ranged)", 1, 0, unit.getRangedCombat(), unit::setRangedCombat, unit.getSourceFile());
            addIntField(grid, "생산 비용 (Cost)",       2, 0, unit.getCost(),         unit::setCost,         unit.getSourceFile());
            addIntField(grid, "이동력 (Moves)",         0, 1, unit.getMoves(),        unit::setMoves,        unit.getSourceFile());
            addIntField(grid, "사거리 (Range)",         1, 1, unit.getRange(),        unit::setRange,        unit.getSourceFile());
            card.getChildren().add(grid);
            unitsBox.getChildren().add(card);
        }
    }

    private void buildBuildingsPanel(CivEntry civ) {
        buildingsBox.getChildren().clear();

        if (civ.getUniqueBuildingTypes().isEmpty()) {
            buildingsBox.getChildren().add(new Label("고유 건물 없음"));
            return;
        }

        for (String buildingType : civ.getUniqueBuildingTypes()) {
            BuildingData building = gameData.getBuilding(buildingType);
            if (building == null) {
                continue;
            }

            VBox card = card();
            card.getChildren().add(sectionHeader(koreanText(building.getDescription())));

            GridPane grid = fieldGrid();
            addIntField(grid, "생산 비용 (Cost)",   0, 0, building.getCost(),            building::setCost,            building.getSourceFile());
            addIntField(grid, "문화 (Culture)",     1, 0, building.getCulture(),         building::setCulture,         building.getSourceFile());
            addIntField(grid, "골드 (Gold)",        2, 0, building.getGold(),            building::setGold,            building.getSourceFile());
            addIntField(grid, "행복 (Happiness)",   0, 1, building.getHappiness(),       building::setHappiness,       building.getSourceFile());
            addIntField(grid, "방어 (Defense)",     1, 1, building.getDefense(),         building::setDefense,         building.getSourceFile());
            addIntField(grid, "유지비 (Maintenance)", 2, 1, building.getGoldMaintenance(), building::setGoldMaintenance, building.getSourceFile());
            card.getChildren().add(grid);
            buildingsBox.getChildren().add(card);
        }
    }

    private void buildImprovementsPanel(CivEntry civ) {
        improvementsBox.getChildren().clear();

        if (civ.getUniqueImprovementTypes().isEmpty()) {
            improvementsBox.getChildren().add(new Label("고유 구조물 없음"));
            return;
        }

        for (String impType : civ.getUniqueImprovementTypes()) {
            ImprovementData imp = gameData.getImprovement(impType);
            if (imp == null) {
                continue;
            }

            VBox card = card();
            card.getChildren().add(sectionHeader(koreanText(imp.getDescription())));

            GridPane grid = fieldGrid();
            addIntField(grid, "식량 (Food)",       0, 0, imp.getFood(),       imp::setFood,       imp.getSourceFile());
            addIntField(grid, "생산 (Production)", 1, 0, imp.getProduction(), imp::setProduction, imp.getSourceFile());
            addIntField(grid, "골드 (Gold)",        2, 0, imp.getGold(),       imp::setGold,       imp.getSourceFile());
            addIntField(grid, "과학 (Science)",     0, 1, imp.getScience(),    imp::setScience,    imp.getSourceFile());
            addIntField(grid, "문화 (Culture)",     1, 1, imp.getCulture(),    imp::setCulture,    imp.getSourceFile());
            addIntField(grid, "신앙 (Faith)",       2, 1, imp.getFaith(),      imp::setFaith,      imp.getSourceFile());
            card.getChildren().add(grid);
            improvementsBox.getChildren().add(card);
        }
    }

    // ── Save ───────────────────────────────────────────────────────────────

    private void saveCurrentCiv() throws Exception {
        if (currentCiv == null) {
            return;
        }

        for (String traitType : currentCiv.getTraitTypes()) {
            TraitData trait = gameData.getTrait(traitType);
            if (trait != null) {
                saver.saveTrait(trait);
            }
        }
        for (String unitType : currentCiv.getUniqueUnitTypes()) {
            UnitData unit = gameData.getUnit(unitType);
            if (unit != null) {
                saver.saveUnit(unit);
            }
        }
        for (String buildingType : currentCiv.getUniqueBuildingTypes()) {
            BuildingData building = gameData.getBuilding(buildingType);
            if (building != null) {
                saver.saveBuilding(building);
            }
        }
        for (String impType : currentCiv.getUniqueImprovementTypes()) {
            ImprovementData imp = gameData.getImprovement(impType);
            if (imp != null) {
                saver.saveImprovement(imp);
            }
        }
    }

    private Set<Path> collectCivSourceFiles() {
        Set<Path> files = new HashSet<>();
        for (String traitType : currentCiv.getTraitTypes()) {
            TraitData trait = gameData.getTrait(traitType);
            if (trait != null && trait.getSourceFile() != null) {
                files.add(trait.getSourceFile());
            }
        }
        for (String unitType : currentCiv.getUniqueUnitTypes()) {
            UnitData unit = gameData.getUnit(unitType);
            if (unit != null && unit.getSourceFile() != null) {
                files.add(unit.getSourceFile());
            }
        }
        for (String buildingType : currentCiv.getUniqueBuildingTypes()) {
            BuildingData building = gameData.getBuilding(buildingType);
            if (building != null && building.getSourceFile() != null) {
                files.add(building.getSourceFile());
            }
        }
        for (String impType : currentCiv.getUniqueImprovementTypes()) {
            ImprovementData imp = gameData.getImprovement(impType);
            if (imp != null && imp.getSourceFile() != null) {
                files.add(imp.getSourceFile());
            }
        }
        return files;
    }

    // ── UI helpers ─────────────────────────────────────────────────────────

    private void addIntField(GridPane grid, String label,
                             int col, int row,
                             int currentValue, IntConsumer setter,
                             Path sourceFile) {
        Label lbl = new Label(label + ":");
        lbl.setStyle("-fx-font-size: 12;");
        TextField tf = numField(String.valueOf(currentValue));
        tf.textProperty().addListener((obs, o, n) -> {
            try {
                setter.accept(Integer.parseInt(n));
                markDirty(sourceFile);
            } catch (NumberFormatException ignored) {}
        });
        GridPane.setConstraints(lbl, col * 3, row);
        GridPane.setConstraints(tf, col * 3 + 1, row);
        grid.getChildren().addAll(lbl, tf);
    }

    private GridPane fieldGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints label = new ColumnConstraints(160);
            ColumnConstraints field = new ColumnConstraints(80);
            ColumnConstraints spacer = new ColumnConstraints(20);
            grid.getColumnConstraints().addAll(label, field, spacer);
        }
        return grid;
    }

    private TextField numField(String val) {
        TextField tf = new TextField(val);
        tf.setPrefWidth(80);
        tf.setStyle("-fx-font-size: 12;");
        tf.textProperty().addListener((obs, o, n) -> {
            if (!n.matches("-?\\d*")) {
                tf.setText(o);
            }
        });
        return tf;
    }

    private VBox card() {
        VBox box = new VBox(8);
        box.setStyle(
                "-fx-border-color: #dddddd; -fx-border-radius: 4; -fx-background-color: #fafafa; -fx-background-radius: 4; -fx-padding: 12;");
        return box;
    }

    private Label sectionHeader(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        return lbl;
    }

    private void markDirty(Path file) {
        if (file != null) {
            dirtyFiles.add(file);
        }
        saveButton.setDisable(false);
    }

    private String civKoreanName(CivEntry civ) {
        if (gameData == null) return civ.getDisplayName();
        String key = "TXT_KEY_CIV_" + civ.getType().replace("CIVILIZATION_", "") + "_SHORT_DESC";
        String text = gameData.getText(key);
        return text.equals(key) ? civ.getDisplayName() : text;
    }

    private String koreanText(String txtKey) {
        if (gameData == null || txtKey == null) return txtKey != null ? txtKey : "";
        String text = gameData.getText(txtKey);
        return text.equals(txtKey) ? txtKey : text;
    }

    private String formatLeader(String leaderType) {
        if (leaderType == null) return "";
        String key = "TXT_KEY_" + leaderType;
        String text = koreanText(key);
        return "지도자: " + (text.equals(key) ? leaderType.replace("LEADER_", "") : text);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
