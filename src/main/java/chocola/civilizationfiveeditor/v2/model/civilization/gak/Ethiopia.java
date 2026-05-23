package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Ethiopia extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Selassie";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new EthiopiaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new MehalSefari()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Stele()};
    }

    private class EthiopiaTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement().element("CombatBonusVsLargerCiv");

            variableList.add(new NodeVariable(node));
        }
    }

    private class MehalSefari extends GodsAndKingsUniqueUnit {
    }

    private class Stele extends GodsAndKingsUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s' and YieldType='YIELD_FAITH']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s' and YieldType='YIELD_CULTURE']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Faith)"));
            variableList.add(new NodeVariable(node2, "Yield(Culture)"));
        }
    }
}
