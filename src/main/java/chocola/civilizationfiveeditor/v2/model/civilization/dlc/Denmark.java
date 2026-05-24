package chocola.civilizationfiveeditor.v2.model.civilization.dlc;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Element;

public class Denmark extends AbstractCivilization {

    public static final Path DLC_04_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/DLC_04/Gameplay/XML");
    public static final Path DLC_04_TEXT_PATH = DLC_04_GAME_PATH.resolve("Text/KO_KR");
    public static final Path EXPANSION2_DLC_04_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/DLC/DLC_04/Gameplay/XML");

    public static final Path DENMARK_CIVILIZATION_TEXT_FILE_PATH = DLC_04_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations_DLC_Denmark.xml");
    public static final Path DENMARK_LEADER_TEXT_FILE_PATH = DLC_04_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders_Harald.xml");
    public static final Path DENMARK_TRAIT_TEXT_FILE_PATH = DLC_04_TEXT_PATH.resolve("CIV5GameTextInfos_Denmark.xml");
    public static final Path DENMARK_UNIT_TEXT_FILE_PATH = DLC_04_TEXT_PATH.resolve("CIV5GameTextInfos_Units_Denmark.xml");
    public static final Path DENMARK_CIVILOPEDIA_TEXT_FILE_PATH = DLC_04_TEXT_PATH.resolve("Civ5CivlopediaDLC_Denmark.xml");

    public static final Path DENMARK_TRAIT_FILE_PATH = DLC_04_GAME_PATH.resolve("CIV5Traits_Denmark.xml");

    public static final Path EXPANSION2_DENMARK_CIVILIZATION_FILE_PATH = EXPANSION2_DLC_04_GAME_PATH.resolve("CIV5Civilization_DLC_Denmark.xml");
    public static final Path EXPANSION2_DENMARK_UNIT_FILE_PATH = EXPANSION2_DLC_04_GAME_PATH.resolve("CIV5Units_Denmark.xml");

    @Override
    public List<Path> requiredPathList() {
        return List.of(
                leaderPath,
                DENMARK_CIVILIZATION_TEXT_FILE_PATH,
                DENMARK_LEADER_TEXT_FILE_PATH,
                DENMARK_TRAIT_TEXT_FILE_PATH,
                DENMARK_UNIT_TEXT_FILE_PATH,
                DENMARK_CIVILOPEDIA_TEXT_FILE_PATH,
                DENMARK_TRAIT_FILE_PATH,
                EXPANSION2_DENMARK_CIVILIZATION_FILE_PATH,
                EXPANSION2_DENMARK_UNIT_FILE_PATH
        );
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Harald";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return DLC_04_GAME_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_DENMARK_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return DENMARK_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return DENMARK_LEADER_TEXT_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new DenmarkTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[] {new Berserker(), new SkiInfantry()};
    }

    private class DenmarkTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return DENMARK_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return DENMARK_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("ExtraEmbarkMoves");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Berserker extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_DENMARK_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return DENMARK_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return DENMARK_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class SkiInfantry extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_DENMARK_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return DENMARK_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return DENMARK_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }
}
