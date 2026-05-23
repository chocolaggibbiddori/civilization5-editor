package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Morocco extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "AhmadalMansur";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new MoroccoTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new BerberCavalry()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[]{new Kasbah()};
    }

    private class MoroccoTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Trait_YieldChangesPerTradePartner/Row[TraitType='%s' and YieldType='YIELD_GOLD']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Trait_YieldChangesPerTradePartner/Row[TraitType='%s' and YieldType='YIELD_CULTURE']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Trait_YieldChangesIncomingTradeRoute/Row[TraitType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Gold, PerTradePartner)"));
            variableList.add(new NodeVariable(node2, "Yield(Culture, PerTradePartner)"));
            variableList.add(new NodeVariable(node3, "Yield(Gold, IncomingTradeRoute)"));
        }
    }

    private class BerberCavalry extends BraveNewWorldUniqueUnit {
    }

    private class Kasbah extends BraveNewWorldUniqueImprovement {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Element node1 = getElement("DefenseModifier");
            Node node2 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s' and YieldType='YIELD_FOOD']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s' and YieldType='YIELD_PRODUCTION']/Yield".formatted(type));
            Node node4 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s' and YieldType='YIELD_GOLD']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2, "Yield(Food)"));
            variableList.add(new NodeVariable(node3, "Yield(Production)"));
            variableList.add(new NodeVariable(node4, "Yield(Gold)"));
        }
    }
}
