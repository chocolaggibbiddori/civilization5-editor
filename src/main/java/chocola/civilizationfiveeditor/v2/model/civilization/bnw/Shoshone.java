package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Element;

public class Shoshone extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Pocatello";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new ShoshoneTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new PathFinder(), new ComancheRiders()};
    }

    private class ShoshoneTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("ExtraFoundedCityTerritoryClaimRange");

            variableList.add(new NodeVariable(node));
        }
    }

    private class PathFinder extends BraveNewWorldUniqueUnit {
    }

    private class ComancheRiders extends BraveNewWorldUniqueUnit {
    }
}
