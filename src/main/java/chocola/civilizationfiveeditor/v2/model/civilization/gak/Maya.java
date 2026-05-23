package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Maya extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Pacal";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new MayaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Atlatlist()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new MayaPyramid()};
    }

    private class MayaTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class Atlatlist extends GodsAndKingsUniqueUnit {
    }

    private class MayaPyramid extends GodsAndKingsUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s' and YieldType='YIELD_FAITH']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s' and YieldType='YIELD_SCIENCE']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Faith)"));
            variableList.add(new NodeVariable(node2, "Yield(Science)"));
        }
    }
}
