package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.loader.GameDataLoader.gameData;

import chocola.civilizationfiveeditor.v2.model.GameData.Type;
import chocola.civilizationfiveeditor.v2.model.GameData.TypedFile;
import chocola.civilizationfiveeditor.v2.model.*;
import chocola.civilizationfiveeditor.v2.model.civilization.BasicCivilization.BasicUniqueUnit.BasicUniqueUnitBuilder;
import chocola.civilizationfiveeditor.v2.util.PathUtils;
import chocola.civilizationfiveeditor.v2.util.TextUtils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Element;
import org.dom4j.Node;

public abstract class BasicCivilization implements Civilization {

    private static final Path DEFAULT_TEXT_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/NewText/KO_KR");
    private static final Path DEFAULT_CIVILIZATION_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Civilizations");
    private static final Path DEFAULT_LEADER_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Leaders");
    private static final Path DEFAULT_UNIT_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Units");
    private static final Path DEFAULT_BUILDING_PATH = PathUtils.DEFAULT_GAME_PATH.resolve("Gameplay/XML/Buildings");

    private static final Path CIVILIZATION_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations.xml");
    private static final Path LEADER_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders.xml");
    private static final Path TRAIT_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Jon.xml");
    private static final Path UNIT_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Units.xml");
    private static final Path CIVILOPEDIA_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Civilopedia.xml");
    private static final Path BUILDING_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Buildings.xml");

    private static final Path CIVILIZATION_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Civilizations.xml");
    private static final Path TRAIT_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Traits.xml");
    private static final Path UNIT_FILE_PATH = DEFAULT_UNIT_PATH.resolve("CIV5Units.xml");
    private static final Path BUILDING_FILE_PATH = DEFAULT_BUILDING_PATH.resolve("CIV5Buildings.xml");

    private final Path leaderPath = DEFAULT_LEADER_PATH.resolve("CIV5Leader_%s.xml".formatted(getLeaderEnglishName()));

    private String name;
    private String englishName;
    private String leaderName;
    private UniqueUnit[] uniqueUnits;
    private UniqueBuilding[] uniqueBuildings;

    @Override
    public List<TypedFile> requiredFileList() {
        return List.of(
                new TypedFile(Type.TEXT, CIVILIZATION_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, LEADER_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, TRAIT_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, UNIT_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, CIVILOPEDIA_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.TEXT, BUILDING_TEXT_FILE_PATH.toUri()),
                new TypedFile(Type.CIVILIZATION, CIVILIZATION_FILE_PATH.toUri()),
                new TypedFile(Type.LEADER, leaderPath.toUri()),
                new TypedFile(Type.TRAIT, TRAIT_FILE_PATH.toUri()),
                new TypedFile(Type.UNIT, UNIT_FILE_PATH.toUri()),
                new TypedFile(Type.BUILDING, BUILDING_FILE_PATH.toUri())
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

    @Override
    public UniqueUnit[] getUniqueUnits() {
        if (uniqueUnits != null) {
            return uniqueUnits;
        }

        String[] uniqueUnitTypes = getUniqueUnitTypes();
        if (uniqueUnitTypes.length == 0) {
            uniqueUnits = new UniqueUnit[0];
            return uniqueUnits;
        }

        UniqueUnit[] uniqueUnits = new UniqueUnit[uniqueUnitTypes.length];
        for (int i = 0; i < uniqueUnitTypes.length; i++) {
            String uniqueUnitType = uniqueUnitTypes[i];
            Element row = (Element) gameData
                    .getDocument(Type.UNIT, UNIT_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Units/Row[Type='UNIT_%s']".formatted(uniqueUnitType));

            String combat = row.elementText("Combat");
            String rangedCombat = row.elementText("RangedCombat");
            String cost = row.elementText("Cost");
            String moves = row.elementText("Moves");
            String range = row.elementText("Range");

            BasicUniqueUnitBuilder unitBuilder = BasicUniqueUnit
                    .builder()
                    .type(uniqueUnitType);
            if (combat != null) unitBuilder.combat(Integer.parseInt(combat));
            if (rangedCombat != null) unitBuilder.rangedCombat(Integer.parseInt(rangedCombat));
            if (cost != null) unitBuilder.cost(Integer.parseInt(cost));
            if (moves != null) unitBuilder.moves(Integer.parseInt(moves));
            if (range != null) unitBuilder.range(Integer.parseInt(range));

            uniqueUnits[i] = unitBuilder.build();
        }

        this.uniqueUnits = uniqueUnits;
        return uniqueUnits;
    }

    protected abstract String[] getUniqueUnitTypes();

    @Override
    public UniqueBuilding[] getUniqueBuildings() {
        if (uniqueBuildings != null) {
            return uniqueBuildings;
        }

        UniqueBuilding[] uniqueBuildings = createUniqueBuildings();

        this.uniqueBuildings = uniqueBuildings;
        return uniqueBuildings;
    }

    protected abstract UniqueBuilding[] createUniqueBuildings();

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

    @Builder
    public static class BasicUniqueUnit implements UniqueUnit {

        private final String type;
        private String description;

        @Getter @Setter
        private Integer combat;

        @Getter @Setter
        private Integer rangedCombat;

        @Getter @Setter
        private Integer cost;

        @Getter @Setter
        private Integer moves;

        @Getter @Setter
        private Integer range;

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = gameData
                    .getDocument(Type.UNIT, UNIT_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Units/Row[Type='UNIT_%s']/Description".formatted(type))
                    .getText();

            Node descriptionNode = gameData
                    .getDocument(Type.TEXT, UNIT_TEXT_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey));

            if (descriptionNode == null) {
                descriptionNode = gameData
                        .getDocument(Type.TEXT, CIVILOPEDIA_TEXT_FILE_PATH.toString())
                        .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey));
            }

            description = descriptionNode.getText();
            return description;
        }

        @Override
        public List<Variable> getVariableList() {
            List<Variable> variableList = new ArrayList<>();
            if (combat != null) variableList.add(new KeyValueVariable("전투력(Combat)", combat));
            if (rangedCombat != null) variableList.add(new KeyValueVariable("원거리 전투력(Ranged Combat)", rangedCombat));
            if (cost != null) variableList.add(new KeyValueVariable("생산 비용(Cost)", cost));
            if (moves != null) variableList.add(new KeyValueVariable("이동력(Moves)", moves));
            if (range != null) variableList.add(new KeyValueVariable("사거리(Range)", range));

            return variableList;
        }
    }

    public static abstract class BasicUniqueBuilding implements UniqueBuilding {

        private final String type;
        private String description;

        public BasicUniqueBuilding(String type) {
            this.type = type;
        }

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = gameData
                    .getDocument(Type.BUILDING, BUILDING_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Building/Row[Type='BUILDING_%s']/Description".formatted(type))
                    .getText();

            String description = gameData
                    .getDocument(Type.TEXT, BUILDING_TEXT_FILE_PATH.toString())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = description;
            return description;
        }
    }
}
