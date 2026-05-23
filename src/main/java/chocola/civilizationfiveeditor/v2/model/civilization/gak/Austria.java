package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Austria extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Maria";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new AustriaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Hussar()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new CoffeeHouse()};
    }

    private class AustriaTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class Hussar extends GodsAndKingsUniqueUnit {
    }

    private class CoffeeHouse extends GodsAndKingsUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldModifiers/Row[BuildingType='%s']/Yield".formatted(type));
            Element node3 = getElement("GreatPeopleRateModifier");

            variableList.add(new NodeVariable(node1, "Yield(Production)"));
            variableList.add(new NodeVariable(node2, "Yield(Production, Modifiers)"));
            variableList.add(new NodeVariable(node3));
        }
    }
}
