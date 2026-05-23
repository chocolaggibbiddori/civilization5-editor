package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Germany extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Bismark";
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
        return new GermanyTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Panzer()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Hanse()};
    }

    private class GermanyTrait extends BasicTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_BASIC_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node1 = row.selectSingleNode("LandBarbarianConversionPercent");
            Node node2 = row.selectSingleNode("LandUnitMaintenanceModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }
    }

    private class Panzer extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }

    private class Hanse extends BasicUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return EXPANSION2_BUILDING_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldModifiers/Row[BuildingType='%s']/Yield".formatted(type));
            Element node3 = getElement("CityStateTradeRouteProductionModifier");

            variableList.add(new NodeVariable(node1, "Yield(Gold)"));
            variableList.add(new NodeVariable(node2, "Yield(Gold, Modifiers)"));
            variableList.add(new NodeVariable(node3));
        }
    }
}
