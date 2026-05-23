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

public class India extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Gandhi";
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_BASIC_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new IndiaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new WarElephant()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new MughalFort()};
    }

    private class IndiaTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node1 = row.selectSingleNode("CityUnhappinessModifier");
            Node node2 = row.selectSingleNode("PopulationUnhappinessModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_BASIC_TRAIT_FILE_PATH;
        }
    }

    private class WarElephant extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }

    private class MughalFort extends BasicUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Element node2 = getElement().element("TechEnhancedTourism");

            variableList.add(new NodeVariable(node1, "Yield(Culture)"));
            variableList.add(new NodeVariable(node2));
        }

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BASIC_BUILDING_FILE_PATH;
        }
    }
}
