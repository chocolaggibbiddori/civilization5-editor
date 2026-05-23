package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Poland extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Casimir";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new PolandTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new WingedHussar()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new DucalStable()};
    }

    private class PolandTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("FreeSocialPoliciesPerEra");

            variableList.add(new NodeVariable(node));
        }
    }

    private class WingedHussar extends BraveNewWorldUniqueUnit {
    }

    private class DucalStable extends BraveNewWorldUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_UnitCombatFreeExperiences/Row[BuildingType='%s']/Experience".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s' and ResourceType='RESOURCE_SHEEP' and YieldType='YIELD_PRODUCTION']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s' and ResourceType='RESOURCE_SHEEP' and YieldType='YIELD_GOLD']/Yield".formatted(type));
            Node node4 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s' and ResourceType='RESOURCE_HORSE' and YieldType='YIELD_PRODUCTION']/Yield".formatted(type));
            Node node5 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s' and ResourceType='RESOURCE_HORSE' and YieldType='YIELD_GOLD']/Yield".formatted(type));
            Node node6 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s' and ResourceType='RESOURCE_COW' and YieldType='YIELD_PRODUCTION']/Yield".formatted(type));
            Node node7 = document.selectSingleNode("/GameData/Building_ResourceYieldChanges/Row[BuildingType='%s' and ResourceType='RESOURCE_COW' and YieldType='YIELD_GOLD']/Yield".formatted(type));
            Node node8 = document.selectSingleNode("/GameData/Building_UnitCombatProductionModifiers/Row[BuildingType='%s']/Modifier".formatted(type));

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2, "Yield(PRODUCTION, SHEEP)"));
            variableList.add(new NodeVariable(node3, "Yield(GOLD, SHEEP)"));
            variableList.add(new NodeVariable(node4, "Yield(PRODUCTION, HORSE)"));
            variableList.add(new NodeVariable(node5, "Yield(GOLD, HORSE)"));
            variableList.add(new NodeVariable(node6, "Yield(PRODUCTION, COW)"));
            variableList.add(new NodeVariable(node7, "Yield(GOLD, COW)"));
            variableList.add(new NodeVariable(node8, "ProductionModifier"));
        }
    }
}
