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

public class Russia extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Catherine";
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
        return new RussiaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Cossack()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Krepost()};
    }

    private class RussiaTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Trait_YieldChangesStrategicResources/Row[TraitType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Trait_ResourceQuantityModifiers/Row[TraitType='%s' and ResourceType='RESOURCE_HORSE']/ResourceQuantityModifier".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Trait_ResourceQuantityModifiers/Row[TraitType='%s' and ResourceType='RESOURCE_IRON']/ResourceQuantityModifier".formatted(type));
            Node node4 = document.selectSingleNode("/GameData/Trait_ResourceQuantityModifiers/Row[TraitType='%s' and ResourceType='RESOURCE_URANIUM']/ResourceQuantityModifier".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Production, StrategicResources)"));
            variableList.add(new NodeVariable(node2, "ResourceQuantityModifier(Horse)"));
            variableList.add(new NodeVariable(node3, "ResourceQuantityModifier(Iron)"));
            variableList.add(new NodeVariable(node4, "ResourceQuantityModifier(Uranium)"));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_BASIC_TRAIT_FILE_PATH;
        }
    }

    private class Cossack extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }

    private class Krepost extends BasicUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element element = (Element) getRow();
            Element node1 = element.element("PlotCultureCostModifier");
            Element node2 = element.element("PlotBuyCostModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BASIC_BUILDING_FILE_PATH;
        }
    }
}
