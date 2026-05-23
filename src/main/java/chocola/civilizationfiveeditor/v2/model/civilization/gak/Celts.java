package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Celts extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Boudicca";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new CeltsTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new PictishWarrior()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new CeilidhHall()};
    }

    private class CeltsTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class PictishWarrior extends GodsAndKingsUniqueUnit {
    }

    private class CeilidhHall extends GodsAndKingsUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Element node2 = getElement().element("Happiness");

            variableList.add(new NodeVariable(node1, "Yield(Culture)"));
            variableList.add(new NodeVariable(node2));
        }
    }
}
