package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;

public class Assyria extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Ashurbanipal";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new AssyriaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new SiegeTower()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new RoyalLibrary()};
    }

    private class AssyriaTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class SiegeTower extends BraveNewWorldUniqueUnit {
    }

    private class RoyalLibrary extends BraveNewWorldUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node = document.selectSingleNode("/GameData/Building_YieldChangesPerPop/Row[BuildingType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node, "Yield(Science, PerPop)"));
        }
    }
}
