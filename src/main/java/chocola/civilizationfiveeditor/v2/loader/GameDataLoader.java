package chocola.civilizationfiveeditor.v2.loader;

import chocola.civilizationfiveeditor.v2.config.CivilizationConfiguration;
import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import chocola.civilizationfiveeditor.v2.model.GameData;
import chocola.civilizationfiveeditor.v2.model.GameData.DataType;
import chocola.civilizationfiveeditor.v2.model.GameData.Type;
import java.util.List;
import java.util.Objects;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class GameDataLoader {

    public static GameData load() {
        SAXReader saxReader = SAXReader.createDefault();
        List<DataType> dataTypeList = CivilizationConfiguration
                .getCivilizationList()
                .stream()
                .map(Civilization::requiredFileList)
                .flatMap(List::stream)
                .distinct()
                .map(file -> {
                    Document document;

                    try {
                        document = saxReader.read(file);
                    } catch (DocumentException e) {
                        return null;
                    }

                    return switch (file) {
                        case TextFile ignored -> new DataType(Type.TEXT, document);
                        case CivilizationFile ignored -> new DataType(Type.CIVILIZATION, document);
                        default -> null;
                    };
                })
                .filter(Objects::nonNull)
                .toList();

        return new GameData(dataTypeList);
    }
}
