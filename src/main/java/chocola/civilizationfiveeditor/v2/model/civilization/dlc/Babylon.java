package chocola.civilizationfiveeditor.v2.model.civilization.dlc;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Element;

public class Babylon extends AbstractCivilization {

    private static final Path DLC_DELUXE_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/DLC_Deluxe/Gameplay/XML");
    private static final Path DLC_DELUXE_TEXT_PATH = DLC_DELUXE_GAME_PATH.resolve("Text/KO_KR");
    private static final Path EXPANSION2_DLC_DELUXE_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/DLC/DLC_Deluxe/Gameplay/XML");

    private static final Path BABYLON_CIVILIZATION_TEXT_FILE_PATH = DLC_DELUXE_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations_Babylon.xml");
    private static final Path BABYLON_LEADER_TEXT_FILE_PATH = DLC_DELUXE_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders_Nebuchadnezzar.xml");
    private static final Path BABYLON_TRAIT_TEXT_FILE_PATH = DLC_DELUXE_TEXT_PATH.resolve("CIV5GameTextInfos_Babylon.xml");
    private static final Path BABYLON_UNIT_TEXT_FILE_PATH = DLC_DELUXE_TEXT_PATH.resolve("CIV5GameTextInfos_Units_Babylon.xml");
    private static final Path BABYLON_CIVILOPEDIA_TEXT_FILE_PATH = DLC_DELUXE_TEXT_PATH.resolve("Civ5CivlopediaDLC_Babylon.xml");
    private static final Path BABYLON_BUILDING_TEXT_FILE_PATH = DLC_DELUXE_TEXT_PATH.resolve("CIV5GameTextInfos_Buildings_Babylon.xml");

    private static final Path BABYLON_TRAIT_FILE_PATH = DLC_DELUXE_GAME_PATH.resolve("CIV5Traits_Babylon.xml");

    private static final Path EXPANSION2_BABYLON_CIVILIZATION_FILE_PATH = EXPANSION2_DLC_DELUXE_GAME_PATH.resolve("CIV5Civilization_Babylon.xml");
    private static final Path EXPANSION2_BABYLON_UNIT_FILE_PATH = EXPANSION2_DLC_DELUXE_GAME_PATH.resolve("CIV5Units_Babylon.xml");
    private static final Path EXPANSION2_BABYLON_BUILDING_FILE_PATH = EXPANSION2_DLC_DELUXE_GAME_PATH.resolve("CIV5Buildings_Babylon.xml");

    @Override
    public List<Path> requiredPathList() {
        return List.of(
                leaderPath,
                BABYLON_CIVILIZATION_TEXT_FILE_PATH,
                BABYLON_LEADER_TEXT_FILE_PATH,
                BABYLON_TRAIT_TEXT_FILE_PATH,
                BABYLON_UNIT_TEXT_FILE_PATH,
                BABYLON_CIVILOPEDIA_TEXT_FILE_PATH,
                BABYLON_BUILDING_TEXT_FILE_PATH,
                BABYLON_TRAIT_FILE_PATH,
                EXPANSION2_BABYLON_CIVILIZATION_FILE_PATH,
                EXPANSION2_BABYLON_UNIT_FILE_PATH,
                EXPANSION2_BABYLON_BUILDING_FILE_PATH
        );
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Nebuchadnezzar";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return DLC_DELUXE_GAME_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_BABYLON_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return BABYLON_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return BABYLON_LEADER_TEXT_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new BabylonTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[] {new Bowman()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[] {new WallsOfBabylon()};
    }

    private class BabylonTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return BABYLON_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return BABYLON_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("GreatScientistRateModifier");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Bowman extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BABYLON_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return BABYLON_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return BABYLON_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class WallsOfBabylon extends AbstractUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BABYLON_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return BABYLON_BUILDING_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }
}
