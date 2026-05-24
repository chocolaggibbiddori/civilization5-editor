package chocola.civilizationfiveeditor.v2.model.civilization.dlc;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Inca extends AbstractCivilization {

    public static final Path DLC_02_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/DLC_02/Gameplay/XML");
    public static final Path DLC_02_TEXT_PATH = DLC_02_GAME_PATH.resolve("Text/KO_KR");
    public static final Path EXPANSION2_DLC_02_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/DLC/DLC_02/Gameplay/XML");

    public static final Path DLC_02_CIVILIZATION_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations_DLC_02.xml");
    public static final Path DLC_02_LEADER_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders_DLC_02.xml");
    public static final Path DLC_02_TRAIT_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_DLC_02.xml");
    public static final Path DLC_02_UNIT_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Units_DLC_02.xml");
    public static final Path INCA_CIVILOPEDIA_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("Civ5CivlopediaDLC_Inca.xml");
    public static final Path DLC_02_IMPROVEMENT_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Objects_DLC_02.xml");

    public static final Path DLC_02_TRAIT_FILE_PATH = DLC_02_GAME_PATH.resolve("CIV5Traits_DLC_02.xml");

    public static final Path EXPANSION2_DLC_02_CIVILIZATION_FILE_PATH = EXPANSION2_DLC_02_GAME_PATH.resolve("CIV5Civilization_DLC_02.xml");
    public static final Path EXPANSION2_DLC_02_UNIT_FILE_PATH = EXPANSION2_DLC_02_GAME_PATH.resolve("CIV5Units_DLC_02.xml");
    public static final Path EXPANSION2_DLC_02_IMPROVEMENT_FILE_PATH = EXPANSION2_DLC_02_GAME_PATH.resolve("CIV5Improvements_DLC_02.xml");

    @Override
    public List<Path> requiredPathList() {
        return List.of(
                leaderPath,
                DLC_02_CIVILIZATION_TEXT_FILE_PATH,
                DLC_02_LEADER_TEXT_FILE_PATH,
                DLC_02_TRAIT_TEXT_FILE_PATH,
                DLC_02_UNIT_TEXT_FILE_PATH,
                INCA_CIVILOPEDIA_TEXT_FILE_PATH,
                DLC_02_IMPROVEMENT_TEXT_FILE_PATH,
                DLC_02_TRAIT_FILE_PATH,
                EXPANSION2_DLC_02_CIVILIZATION_FILE_PATH,
                EXPANSION2_DLC_02_UNIT_FILE_PATH,
                EXPANSION2_DLC_02_IMPROVEMENT_FILE_PATH
        );
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Pachacuti";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return DLC_02_GAME_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_DLC_02_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return DLC_02_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return DLC_02_LEADER_TEXT_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new IncaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[] {new Slinger()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[] {new TerraceFarm()};
    }

    private class IncaTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return DLC_02_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return DLC_02_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("ImprovementMaintenanceModifier");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Slinger extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_DLC_02_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return DLC_02_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return INCA_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class TerraceFarm extends AbstractUniqueImprovement {

        @Override
        protected Path getImprovementFilePath() {
            return EXPANSION2_DLC_02_IMPROVEMENT_FILE_PATH;
        }

        @Override
        protected Path getImprovementTextFilePath() {
            return DLC_02_IMPROVEMENT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Improvement_AdjacentMountainYieldChanges/Row[ImprovementType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Food)"));
            variableList.add(new NodeVariable(node2, "Yield(Food, Mountain)"));
        }
    }
}
