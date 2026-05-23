package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_INHERITED_UNIT_TEXT_FILE_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Netherlands extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "William";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new NetherlandsTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new SeaBeggar()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[]{new Polder()};
    }

    private class NetherlandsTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement().element("LuxuryHappinessRetention");

            variableList.add(new NodeVariable(node));
        }
    }

    private class SeaBeggar extends GodsAndKingsUniqueUnit {

        @Override
        protected Path getUnitTextFilePath() {
            return EXPANSION2_INHERITED_UNIT_TEXT_FILE_PATH;
        }
    }

    private class Polder extends GodsAndKingsUniqueImprovement {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Improvement_TechYieldChanges/Row[ImprovementType='%s' and YieldType='YIELD_PRODUCTION']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Improvement_TechYieldChanges/Row[ImprovementType='%s' and YieldType='YIELD_GOLD']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Food)"));
            variableList.add(new NodeVariable(node2, "Yield(Production, Tech)"));
            variableList.add(new NodeVariable(node3, "Yield(Gold, Tech)"));
        }

        @Override
        protected Path getImprovementFilePath() {
            return super.getImprovementFilePath();
        }
    }
}
