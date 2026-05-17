package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.loader.CivilizationFile;
import chocola.civilizationfiveeditor.v2.loader.TextFile;
import chocola.civilizationfiveeditor.v2.model.GameData;
import chocola.civilizationfiveeditor.v2.model.GameData.Type;
import chocola.civilizationfiveeditor.v2.util.PathUtils;
import chocola.civilizationfiveeditor.v2.util.TextUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public abstract class BasicCivilization implements Civilization {

    private static final Path DEFAULT_TEXT_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/NewText/KO_KR");
    private static final Path DEFAULT_CIVILIZATION_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Civilizations");

    private static final Path CIVILIZATION_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations.xml");
    private static final Path LEADER_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders.xml");
    private static final Path CIVILIZATION_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Civilizations.xml");

    private String koreanName;
    private String englishName;
    private String leaderKoreanName;
    private String leaderEnglishName;

    @Override
    public List<File> requiredFileList() {
        TextFile civilizationTextFile = new TextFile(CIVILIZATION_TEXT_FILE_PATH.toUri());
        TextFile leaderTextFile = new TextFile(LEADER_TEXT_FILE_PATH.toUri());
        CivilizationFile civilizationFile = new CivilizationFile(CIVILIZATION_FILE_PATH.toUri());

        return List.of(civilizationTextFile, leaderTextFile, civilizationFile);
    }

    @Override
    public String getKoreanName(GameData gameData) {
        if (koreanName != null) {
            return koreanName;
        }

        String shortDescriptionTag = gameData
                .getDocument(Type.CIVILIZATION, CIVILIZATION_FILE_PATH.toString())
                .selectSingleNode("/GameData/Civilizations/Row[Type=CIVILIZATION_%s]/ShortDescription"
                        .formatted(getEnglishName(gameData)))
                .getText();
        String koreanName = gameData
                .getDocument(Type.TEXT, CIVILIZATION_TEXT_FILE_PATH.toString())
                .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text"
                        .formatted(shortDescriptionTag))
                .getText();

        this.koreanName = TextUtils.stripColorTags(koreanName);
        return this.koreanName;
    }

    @Override
    public String getEnglishName(GameData gameData) {
        if (englishName != null) {
            return englishName;
        }

        englishName = getClass().getSimpleName().toUpperCase();
        return englishName;
    }

    @Override
    public String getLeaderKoreanName(GameData gameData) {
        if (leaderKoreanName != null) {
            return leaderKoreanName;
        }

        leaderKoreanName = gameData
                .getDocument(Type.TEXT, LEADER_TEXT_FILE_PATH.toString())
                .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='TXT_KEY_LEADER_%s']/Text"
                        .formatted(getLeaderEnglishName(gameData)))
                .getText();
        return leaderKoreanName;
    }

    @Override
    public String getLeaderEnglishName(GameData gameData) {
        if (leaderEnglishName != null) {
            return leaderEnglishName;
        }

        leaderEnglishName = gameData
                .getDocument(Type.CIVILIZATION, CIVILIZATION_FILE_PATH.toString())
                .selectSingleNode("/GameData/Civilization_Leaders/Row[CivilizationType=CIVILIZATION_%s]/LeaderheadType"
                        .formatted(getEnglishName(gameData)))
                .getText()
                .substring("LEADER_".length());
        return leaderEnglishName;
    }
}
