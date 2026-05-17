package chocola.civilizationfiveeditor.v2.loader;

import chocola.civilizationfiveeditor.v2.config.CivilizationConfiguration;
import chocola.civilizationfiveeditor.v2.model.GameData;
import chocola.civilizationfiveeditor.v2.model.GameData.DataType;
import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import java.util.List;
import java.util.Objects;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class GameDataLoader {

    public static GameData gameData = null;

    public static void load() {
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

                    return new DataType(file.getType(), document);
                })
                .filter(Objects::nonNull)
                .toList();

        gameData = new GameData(dataTypeList);
    }
}
