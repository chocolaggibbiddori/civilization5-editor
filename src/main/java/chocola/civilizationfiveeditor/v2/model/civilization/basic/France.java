package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;

public class France extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Napoleon";
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new FranceTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Musketeer()};
    }

    @Override
    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[]{new Chateau()};
    }

    private class FranceTrait extends BasicTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_TRAIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node = row.selectSingleNode("CapitalThemingBonusModifier");

            variableList.add(new NodeVariable(node));
        }
    }

    private class Musketeer extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class Chateau extends BasicUniqueImprovement {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s' and YieldType='YIELD_CULTURE']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Improvement_Yields/Row[ImprovementType='%s' and YieldType='YIELD_GOLD']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Improvement_TechYieldChanges/Row[ImprovementType='%s' and YieldType='YIELD_CULTURE']/Yield".formatted(type));
            Node node4 = document.selectSingleNode("/GameData/Improvement_TechYieldChanges/Row[ImprovementType='%s' and YieldType='YIELD_GOLD']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Culture)"));
            variableList.add(new NodeVariable(node2, "Yield(Gold)"));
            variableList.add(new NodeVariable(node3, "Yield(Culture, Tech)"));
            variableList.add(new NodeVariable(node4, "Yield(Gold, Tech)"));
        }
    }
}
