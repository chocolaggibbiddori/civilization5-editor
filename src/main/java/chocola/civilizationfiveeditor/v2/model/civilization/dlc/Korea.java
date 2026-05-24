package chocola.civilizationfiveeditor.v2.model.civilization.dlc;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;

public class Korea extends AbstractCivilization {

    public static final Path DLC_05_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/DLC_05/Gameplay/XML");
    public static final Path DLC_05_TEXT_PATH = DLC_05_GAME_PATH.resolve("Text/KO_KR");
    public static final Path EXPANSION2_DLC_05_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/DLC/DLC_05/Gameplay/XML");

    public static final Path KOREA_CIVILIZATION_TEXT_FILE_PATH = DLC_05_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations_DLC_Korea.xml");
    public static final Path KOREA_LEADER_TEXT_FILE_PATH = DLC_05_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders_Sejong.xml");
    public static final Path KOREA_TRAIT_TEXT_FILE_PATH = DLC_05_TEXT_PATH.resolve("CIV5GameTextInfos_Korea.xml");
    public static final Path KOREA_UNIT_TEXT_FILE_PATH = DLC_05_TEXT_PATH.resolve("CIV5GameTextInfos_Units_Korea.xml");
    public static final Path KOREA_CIVILOPEDIA_TEXT_FILE_PATH = DLC_05_TEXT_PATH.resolve("Civ5CivlopediaDLC_Korea.xml");

    public static final Path EXPANSION2_KOREA_CIVILIZATION_FILE_PATH = EXPANSION2_DLC_05_GAME_PATH.resolve("CIV5Civilization_DLC_Korea.xml");
    public static final Path EXPANSION2_KOREA_TRAIT_FILE_PATH = EXPANSION2_DLC_05_GAME_PATH.resolve("CIV5Traits_Korea.xml");
    public static final Path EXPANSION2_KOREA_UNIT_FILE_PATH = EXPANSION2_DLC_05_GAME_PATH.resolve("CIV5Units_Korea.xml");

    @Override
    public List<Path> requiredPathList() {
        return List.of(
                leaderPath,
                KOREA_CIVILIZATION_TEXT_FILE_PATH,
                KOREA_LEADER_TEXT_FILE_PATH,
                KOREA_TRAIT_TEXT_FILE_PATH,
                KOREA_UNIT_TEXT_FILE_PATH,
                KOREA_CIVILOPEDIA_TEXT_FILE_PATH,
                EXPANSION2_KOREA_CIVILIZATION_FILE_PATH,
                EXPANSION2_KOREA_TRAIT_FILE_PATH,
                EXPANSION2_KOREA_UNIT_FILE_PATH
        );
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Sejong";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return DLC_05_GAME_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_KOREA_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return KOREA_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return KOREA_LEADER_TEXT_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new KoreaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[] {new TurtleShip(), new Hwacha()};
    }

    private class KoreaTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_KOREA_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return KOREA_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s' and ImprovementType='IMPROVEMENT_ACADEMY']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s' and ImprovementType='IMPROVEMENT_CUSTOMS_HOUSE']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s' and ImprovementType='IMPROVEMENT_MANUFACTORY']/Yield".formatted(type));
            Node node4 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s' and ImprovementType='IMPROVEMENT_CITADEL']/Yield".formatted(type));
            Node node5 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s' and ImprovementType='IMPROVEMENT_LANDMARK']/Yield".formatted(type));
            Node node6 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s' and ImprovementType='IMPROVEMENT_HOLY_SITE']/Yield".formatted(type));
            Node node7 = document.selectSingleNode("/GameData/Trait_SpecialistYieldChanges/Row[TraitType='%s' and SpecialistType='SPECIALIST_ARTIST']/Yield".formatted(type));
            Node node8 = document.selectSingleNode("/GameData/Trait_SpecialistYieldChanges/Row[TraitType='%s' and SpecialistType='SPECIALIST_SCIENTIST']/Yield".formatted(type));
            Node node9 = document.selectSingleNode("/GameData/Trait_SpecialistYieldChanges/Row[TraitType='%s' and SpecialistType='SPECIALIST_MERCHANT']/Yield".formatted(type));
            Node node10 = document.selectSingleNode("/GameData/Trait_SpecialistYieldChanges/Row[TraitType='%s' and SpecialistType='SPECIALIST_ENGINEER']/Yield".formatted(type));
            Node node11 = document.selectSingleNode("/GameData/Trait_SpecialistYieldChanges/Row[TraitType='%s' and SpecialistType='SPECIALIST_WRITER']/Yield".formatted(type));
            Node node12 = document.selectSingleNode("/GameData/Trait_SpecialistYieldChanges/Row[TraitType='%s' and SpecialistType='SPECIALIST_MUSICIAN']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Science, Academy)"));
            variableList.add(new NodeVariable(node2, "Yield(Science, CustomsHouse)"));
            variableList.add(new NodeVariable(node3, "Yield(Science, Manufactory)"));
            variableList.add(new NodeVariable(node4, "Yield(Science, Citadel)"));
            variableList.add(new NodeVariable(node5, "Yield(Science, Landmark)"));
            variableList.add(new NodeVariable(node6, "Yield(Science, HolySite)"));
            variableList.add(new NodeVariable(node7, "Yield(Science, Artist)"));
            variableList.add(new NodeVariable(node8, "Yield(Science, Scientist)"));
            variableList.add(new NodeVariable(node9, "Yield(Science, Merchant)"));
            variableList.add(new NodeVariable(node10, "Yield(Science, Engineer)"));
            variableList.add(new NodeVariable(node11, "Yield(Science, Writer)"));
            variableList.add(new NodeVariable(node12, "Yield(Science, Musician)"));
        }
    }

    private class TurtleShip extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_KOREA_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return KOREA_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return KOREA_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class Hwacha extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_KOREA_UNIT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return KOREA_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return KOREA_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }
}
