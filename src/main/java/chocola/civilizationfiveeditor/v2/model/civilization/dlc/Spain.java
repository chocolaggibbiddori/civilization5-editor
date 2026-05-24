package chocola.civilizationfiveeditor.v2.model.civilization.dlc;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION_UNIT_TEXT_FILE_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.Node;

public class Spain extends AbstractCivilization {

    private static final Path DLC_02_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/DLC_02/Gameplay/XML");
    private static final Path DLC_02_TEXT_PATH = DLC_02_GAME_PATH.resolve("Text/KO_KR");
    private static final Path EXPANSION2_DLC_02_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/DLC/DLC_02/Gameplay/XML");

    private static final Path DLC_02_CIVILIZATION_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations_DLC_02.xml");
    private static final Path DLC_02_LEADER_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders_DLC_02.xml");
    private static final Path DLC_02_TRAIT_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_DLC_02.xml");
    private static final Path DLC_02_UNIT_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Units_DLC_02.xml");
    private static final Path SPAIN_CIVILOPEDIA_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("Civ5CivlopediaDLC_Spain.xml");
    private static final Path DLC_02_IMPROVEMENT_TEXT_FILE_PATH = DLC_02_TEXT_PATH.resolve("CIV5GameTextInfos_Objects_DLC_02.xml");

    private static final Path DLC_02_TRAIT_FILE_PATH = DLC_02_GAME_PATH.resolve("CIV5Traits_DLC_02.xml");

    private static final Path EXPANSION2_DLC_02_CIVILIZATION_FILE_PATH = EXPANSION2_DLC_02_GAME_PATH.resolve("CIV5Civilization_DLC_02.xml");
    private static final Path EXPANSION2_DLC_02_UNIT_FILE_PATH = EXPANSION2_DLC_02_GAME_PATH.resolve("CIV5Units_DLC_02.xml");
    private static final Path EXPANSION2_DLC_02_IMPROVEMENT_FILE_PATH = EXPANSION2_DLC_02_GAME_PATH.resolve("CIV5Improvements_DLC_02.xml");

    @Override
    public List<Path> requiredPathList() {
        return List.of(
                leaderPath,
                DLC_02_CIVILIZATION_TEXT_FILE_PATH,
                DLC_02_LEADER_TEXT_FILE_PATH,
                DLC_02_TRAIT_TEXT_FILE_PATH,
                DLC_02_UNIT_TEXT_FILE_PATH,
                SPAIN_CIVILOPEDIA_TEXT_FILE_PATH,
                DLC_02_IMPROVEMENT_TEXT_FILE_PATH,
                DLC_02_TRAIT_FILE_PATH,
                EXPANSION2_DLC_02_CIVILIZATION_FILE_PATH,
                EXPANSION2_DLC_02_UNIT_FILE_PATH,
                EXPANSION2_DLC_02_IMPROVEMENT_FILE_PATH
        );
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Isabella";
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
        return new SpainTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Tercio(), new Conquistador()};
    }

    private class SpainTrait extends AbstractTrait {

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
            Element element = getElement();
            Node node1 = element.element("NaturalWonderFirstFinderGold");
            Node node2 = element.element("NaturalWonderSubsequentFinderGold");
            Node node3 = element.element("NaturalWonderYieldModifier");
            Node node4 = element.element("NaturalWonderHappinessModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
            variableList.add(new NodeVariable(node3));
            variableList.add(new NodeVariable(node4));
        }
    }

    private class Tercio extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_DLC_02_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return EXPANSION_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return SPAIN_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class Conquistador extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_DLC_02_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return EXPANSION_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return SPAIN_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }
}
