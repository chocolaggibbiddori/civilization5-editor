package chocola.civilizationfiveeditor.v2.service;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.config.CivilizationConfiguration;
import chocola.civilizationfiveeditor.v2.model.GameData;
import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class GameDataLoader {

    public static GameData gameData = null;

    public static void load() {
        SAXReader saxReader = SAXReader.createDefault();
        Map<String, Document> documentMap = Stream.concat(
                        Stream.of(defaultPathList()),
                        CivilizationConfiguration
                                .getCivilizationList()
                                .stream()
                                .map(Civilization::requiredPathList))
                .flatMap(List::stream)
                .distinct()
                .map(path -> {
                    try {
                        return saxReader.read(path.toFile());
                    } catch (DocumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Document::getName, Function.identity()));

        gameData = new GameData(documentMap);
    }

    private static List<Path> defaultPathList() {
        return List.of(
                CIVILIZATION_TEXT_FILE_PATH,
                LEADER_TEXT_FILE_PATH,
                TRAIT_TEXT_FILE_PATH,
                UNIT_TEXT_FILE_PATH,
                CIVILOPEDIA_TEXT_FILE_PATH,
                BUILDING_TEXT_FILE_PATH,
                OBJECT_TEXT_FILE_PATH,
                CIVILIZATION_FILE_PATH,
                TRAIT_FILE_PATH,
                UNIT_FILE_PATH,
                BUILDING_FILE_PATH,
                EXPANSION_CIVILIZATION_TEXT_FILE_PATH,
                EXPANSION_TRAIT_TEXT_FILE_PATH,
                EXPANSION_LEADER_TEXT_FILE_PATH,
                EXPANSION_UNIT_TEXT_FILE_PATH,
                EXPANSION_CIVILOPEDIA_TEXT_FILE_PATH,
                EXPANSION_BUILDING_TEXT_FILE_PATH,
                EXPANSION_OBJECT_TEXT_FILE_PATH,
                EXPANSION_CIVILIZATION_FILE_PATH,
                EXPANSION_TRAIT_FILE_PATH,
                EXPANSION_DEFAULT_LEADER_PATH,
                EXPANSION_UNIT_FILE_PATH,
                EXPANSION_BUILDING_FILE_PATH,
                EXPANSION_IMPROVEMENT_FILE_PATH,
                EXPANSION2_TRAIT_TEXT_FILE_PATH,
                EXPANSION2_INHERITED_TRAIT_TEXT_FILE_PATH,
                EXPANSION2_JON_INHERITED_TEXT_FILE_PATH,
                EXPANSION2_OBJECT_TEXT_FILE_PATH,
                EXPANSION2_BUILDING_TEXT_FILE_PATH,
                EXPANSION2_CIVILIZATION_FILE_PATH,
                EXPANSION2_TRAIT_FILE_PATH,
                EXPANSION2_UNIT_FILE_PATH,
                EXPANSION2_INHERITED_UNIT_TEXT_FILE_PATH,
                EXPANSION2_BASIC_BUILDING_FILE_PATH,
                EXPANSION2_BUILDING_FILE_PATH,
                EXPANSION2_IMPROVEMENT_FILE_PATH
        );
    }
}
