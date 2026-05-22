package chocola.civilizationfiveeditor.v2.model.civilization.impl;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.BasicCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;

public class Arabia extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "HarunAlRashid";
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new ArabiaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new CamelArcher()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Bazaar()};
    }

    private class ArabiaTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node1 = row.selectSingleNode("LandTradeRouteRangeBonus");
            Node node2 = row.selectSingleNode("TradeReligionModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_TRAIT_TEXT_FILE_PATH;
        }
    }

    private class CamelArcher extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class Bazaar extends BasicUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_FeatureYieldChanges/Row[BuildingType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Gold, Oil)"));
            variableList.add(new NodeVariable(node2, "Yield(Gold, Oasis)"));
        }

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BASIC_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return OBJECT_TEXT_FILE_PATH;
        }
    }
}
