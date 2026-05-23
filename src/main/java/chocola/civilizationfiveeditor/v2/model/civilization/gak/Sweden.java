package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Element;

public class Sweden extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Gustavus";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new SwedenTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Hakkapeliitta(), new Carolean()};
    }

    private class SwedenTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element element = getElement();
            Element node1 = element.element("GreatPersonGiftInfluence");
            Element node2 = element.element("DOFGreatPersonModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }
    }

    private class Hakkapeliitta extends GodsAndKingsUniqueUnit {
    }

    private class Carolean extends GodsAndKingsUniqueUnit {
    }
}
