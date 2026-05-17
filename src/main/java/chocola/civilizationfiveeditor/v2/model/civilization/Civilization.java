package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.model.GameData;
import java.io.File;
import java.util.List;

public interface Civilization {

    List<File> requiredFileList();

    String getKoreanName(GameData gameData);

    String getEnglishName(GameData gameData);

    String getLeaderKoreanName(GameData gameData);

    String getLeaderEnglishName(GameData gameData);
}
