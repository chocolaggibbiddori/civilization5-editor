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
import org.dom4j.Element;
import org.dom4j.Node;

public class Egypt extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Ramesses";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new EgyptTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new WarChariot()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new BurialTomb()};
    }

    private class EgyptTrait extends BasicTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node = row.selectSingleNode("WonderProductionModifier");

            variableList.add(new NodeVariable(node));
        }
    }

    private class WarChariot extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class BurialTomb extends BasicUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return OBJECT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));

            Element element = (Element) getRow();
            Element node2 = element.element("Happiness");

            variableList.add(new NodeVariable(node1, "Yield(Faith)"));
            variableList.add(new NodeVariable(node2));
        }
    }
}
