package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Element;

public class Portugal extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "MariaI";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new PortugalTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Nau()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[]{new Feitoria()};
    }

    private class PortugalTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("TradeRouteResourceModifier");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Nau extends BraveNewWorldUniqueUnit {
    }

    private class Feitoria extends BraveNewWorldUniqueImprovement {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("DefenseModifier");

            variableList.add(new NodeVariable(node));
        }
    }
}
