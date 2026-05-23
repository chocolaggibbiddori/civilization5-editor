package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;

public class Aztec extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Montezuma";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_BASIC_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new AztecTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Jaguar()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new FloatingGardens()};
    }

    private class AztecTrait extends BasicTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_BASIC_TRAIT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node = row.selectSingleNode("CultureFromKills");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Jaguar extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }

    private class FloatingGardens extends BasicUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BASIC_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return OBJECT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_LakePlotYieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldModifiers/Row[BuildingType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Food, Lake)"));
            variableList.add(new NodeVariable(node2, "Yield(Food, Modifiers)"));
        }
    }
}
