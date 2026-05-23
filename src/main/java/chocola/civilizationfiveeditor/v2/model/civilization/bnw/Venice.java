package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Element;

public class Venice extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "EnricoDandolo";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new VeniceTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Merchant(), new Galleass()};
    }

    private class VeniceTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("NumTradeRoutesModifier");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Merchant extends BraveNewWorldUniqueUnit {

        @Override
        protected void addVariables(List<Variable> variableList) {
            variableList.clear();

            Element element = getElement();
            Element node1 = element.element("Moves");
            Element node2 = element.element("BaseGold");
            Element node3 = element.element("NumGoldPerEra");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
            variableList.add(new NodeVariable(node3));
        }
    }

    private class Galleass extends BraveNewWorldUniqueUnit {
    }
}
