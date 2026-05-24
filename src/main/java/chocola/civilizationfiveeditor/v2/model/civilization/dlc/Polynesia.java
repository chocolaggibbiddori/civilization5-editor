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

public class Polynesia extends AbstractCivilization {

    private static final Path DLC_03_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/DLC_03/Gameplay/XML");
    private static final Path DLC_03_TEXT_PATH = DLC_03_GAME_PATH.resolve("Text/KO_KR");
    private static final Path EXPANSION2_DLC_03_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/DLC/DLC_03/Gameplay/XML");

    private static final Path DLC_03_CIVILIZATION_TEXT_FILE_PATH = DLC_03_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations_DLC_Polynesia.xml");
    private static final Path DLC_03_LEADER_TEXT_FILE_PATH = DLC_03_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders_Kamehameha.xml");
    private static final Path DLC_03_TRAIT_TEXT_FILE_PATH = DLC_03_TEXT_PATH.resolve("CIV5GameTextInfos_Polynesia.xml");
    private static final Path DLC_03_UNIT_TEXT_FILE_PATH = DLC_03_TEXT_PATH.resolve("CIV5GameTextInfos_Units_Polynesia.xml");
    private static final Path DLC_03_CIVILOPEDIA_TEXT_FILE_PATH = DLC_03_TEXT_PATH.resolve("Civ5CivlopediaDLC_Polynesia.xml");

    private static final Path DLC_03_TRAIT_FILE_PATH = DLC_03_GAME_PATH.resolve("CIV5Traits_Polynesia.xml");

    private static final Path EXPANSION2_DLC_03_CIVILIZATION_FILE_PATH = EXPANSION2_DLC_03_GAME_PATH.resolve("CIV5Civilization_DLC_Polynesia.xml");
    private static final Path EXPANSION2_DLC_03_UNIT_FILE_PATH = EXPANSION2_DLC_03_GAME_PATH.resolve("CIV5Units_Polynesia.xml");
    private static final Path EXPANSION2_DLC_03_IMPROVEMENT_FILE_PATH = EXPANSION2_DLC_03_GAME_PATH.resolve("CIV5Improvements_Polynesia.xml");

    @Override
    public List<Path> requiredPathList() {
        return List.of(
                leaderPath,
                DLC_03_CIVILIZATION_TEXT_FILE_PATH,
                DLC_03_LEADER_TEXT_FILE_PATH,
                DLC_03_TRAIT_TEXT_FILE_PATH,
                DLC_03_UNIT_TEXT_FILE_PATH,
                DLC_03_CIVILOPEDIA_TEXT_FILE_PATH,
                DLC_03_TRAIT_FILE_PATH,
                EXPANSION2_DLC_03_CIVILIZATION_FILE_PATH,
                EXPANSION2_DLC_03_UNIT_FILE_PATH,
                EXPANSION2_DLC_03_IMPROVEMENT_FILE_PATH
        );
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Kamehameha";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return DLC_03_GAME_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_DLC_03_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return DLC_03_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return DLC_03_LEADER_TEXT_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new PolynesiaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[] {new MaoriWarrior()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[] {new Moai()};
    }

    private class PolynesiaTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return DLC_03_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return DLC_03_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element element = getElement();
            Element node1 = element.element("NearbyImprovementCombatBonus");
            Element node2 = element.element("NearbyImprovementBonusRange");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }
    }

    private class MaoriWarrior extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_DLC_03_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return DLC_03_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return DLC_03_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class Moai extends AbstractUniqueImprovement {

        @Override
        protected Path getImprovementFilePath() {
            return EXPANSION2_DLC_03_IMPROVEMENT_FILE_PATH;
        }

        @Override
        protected Path getImprovementTextFilePath() {
            return DLC_03_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Improvement_TechYieldChanges/Row[ImprovementType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Culture)"));
            variableList.add(new NodeVariable(node2, "Yield(Gold, Tech)"));
        }
    }
}
