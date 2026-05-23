package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Huns extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Attila";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new HunsTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new HorseArcher(), new BatteringRam()};
    }

    private class HunsTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Element node1 = getElement().element("RazeSpeedModifier");
            Node node2 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2, "Yield(Production, Pasture)"));
        }
    }

    private class HorseArcher extends GodsAndKingsUniqueUnit {
    }

    private class BatteringRam extends GodsAndKingsUniqueUnit {
    }
}
