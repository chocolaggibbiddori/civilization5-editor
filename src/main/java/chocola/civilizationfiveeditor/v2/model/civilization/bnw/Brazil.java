package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Brazil extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Pedro";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new BrazilTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Pracinha()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[]{new BrazilwoodCamp()};
    }

    private class BrazilTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element element = getElement();
            Node node1 = element.element("GoldenAgeTourismModifier");
            Node node2 = element.element("GoldenAgeGreatArtistRateModifier");
            Node node3 = element.element("GoldenAgeGreatMusicianRateModifier");
            Node node4 = element.element("GoldenAgeGreatWriterRateModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
            variableList.add(new NodeVariable(node3));
            variableList.add(new NodeVariable(node4));
        }
    }

    private class Pracinha extends BraveNewWorldUniqueUnit {
    }

    private class BrazilwoodCamp extends BraveNewWorldUniqueImprovement {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Improvement_TechYieldChanges/Row[ImprovementType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Gold)"));
            variableList.add(new NodeVariable(node2, "Yield(Culture, Tech)"));
        }
    }
}
