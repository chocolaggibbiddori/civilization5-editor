package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Indonesia extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "GajahMada";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new IndonesiaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new KrisSwordsman()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Candi()};
    }

    private class IndonesiaTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("UniqueLuxuryQuantity");

            variableList.add(new NodeVariable(node));
        }
    }

    private class KrisSwordsman extends BraveNewWorldUniqueUnit {
    }

    private class Candi extends BraveNewWorldUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Element node1 = getElement("GreatPeopleRateModifier");
            Node node2 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Building_YieldChangesPerReligion/Row[BuildingType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2, "Yield(Faith)"));
            variableList.add(new NodeVariable(node3, "Yield(Faith, PerReligion)"));
        }
    }
}
