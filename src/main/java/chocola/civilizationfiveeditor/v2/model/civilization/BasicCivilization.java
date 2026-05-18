package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.loader.GameDataLoader.gameData;

import chocola.civilizationfiveeditor.v2.model.GameData.Type;
import chocola.civilizationfiveeditor.v2.model.GameData.TypedFile;
import chocola.civilizationfiveeditor.v2.util.PathUtils;
import chocola.civilizationfiveeditor.v2.util.TextUtils;
import java.nio.file.Path;
import java.util.List;
import lombok.EqualsAndHashCode;

public abstract class BasicCivilization implements Civilization {

    private static final Path DEFAULT_TEXT_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/NewText/KO_KR");
    private static final Path DEFAULT_CIVILIZATION_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Civilizations");
    private static final Path DEFAULT_LEADER_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Leaders");

    private static final Path CIVILIZATION_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations.xml");
    private static final Path LEADER_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders.xml");
    private static final Path CIVILIZATION_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Civilizations.xml");
    private static final Path TRAIT_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Traits.xml");
    private static final Path TRAIT_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Jon.xml");

    private final Path leaderPath = DEFAULT_LEADER_PATH.resolve("CIV5Leader_%s.xml".formatted(getLeaderEnglishName()));

    private String name;
    private String englishName;
    private String leaderName;

    @Override
    public List<TypedFile> requiredFileList() {
        return List.of(
                new TypedFile(Type.TEXT, CIVILIZATION_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, LEADER_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.CIVILIZATION, CIVILIZATION_FILE_PATH.toUri()),
                new TypedFile(Type.LEADER, leaderPath.toUri()),
                new TypedFile(Type.TRAIT, TRAIT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, TRAIT_TEXT_FILE_PATH.toUri())
        );
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }

        String shortDescriptionTag = gameData
                .getDocument(Type.CIVILIZATION, CIVILIZATION_FILE_PATH.toString())
                .selectSingleNode("/GameData/Civilizations/Row[Type='CIVILIZATION_%s']/ShortDescription"
                        .formatted(getEnglishName().toUpperCase()))
                .getText();
        String name = gameData
                .getDocument(Type.TEXT, CIVILIZATION_TEXT_FILE_PATH.toString())
                .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text"
                        .formatted(shortDescriptionTag))
                .getText();

        this.name = TextUtils.stripInnerTags(name);
        return this.name;
    }

    protected String getEnglishName() {
        if (englishName != null) {
            return englishName;
        }

        englishName = getClass().getSimpleName();
        return englishName;
    }

    @Override
    public String getLeaderName() {
        if (leaderName != null) {
            return leaderName;
        }

        leaderName = gameData
                .getDocument(Type.TEXT, LEADER_TEXT_FILE_PATH.toString())
                .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='TXT_KEY_LEADER_%s']/Text"
                        .formatted(getLeaderEnglishName().toUpperCase()))
                .getText();
        return leaderName;
    }

    protected abstract String getLeaderEnglishName();

    public abstract class BasicTrait implements Trait {

        private String description;

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String traitType = gameData
                    .getDocument(Type.LEADER, leaderPath.toString())
                    .selectSingleNode("/GameData/Leader_Traits/Row/TraitType")
                    .getText();

            String descriptionKey = gameData
                    .getDocument(Type.TRAIT, TRAIT_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Traits/Row[Type='%s']/Description".formatted(traitType))
                    .getText();

            String description = gameData
                    .getDocument(Type.TEXT, TRAIT_TEXT_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = TextUtils.stripInnerTags(description);
            return this.description;
        }
    }

    @EqualsAndHashCode
    public static class BasicTraitVariable implements TraitVariable {

        private final String key;
        private final int originValue;

        @EqualsAndHashCode.Exclude
        private int value;

        public BasicTraitVariable(String key, int value) {
            this.key = key;
            this.originValue = value;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public void setValue(String value) {
            if (value == null) {
                this.value = 0;
                return;
            }

            setValue(Integer.parseInt(value));
        }

        @Override
        public boolean isChanged() {
            return originValue != value;
        }
    }
}
