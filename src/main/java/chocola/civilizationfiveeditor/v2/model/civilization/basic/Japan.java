package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;

public class Japan extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "OdaNobunaga";
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new JapanTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Samurai(), new Zero()};
    }

    private class JapanTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Trait_ImprovementYieldChanges/Row[TraitType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Trait_UnimprovedFeatureYieldChanges/Row[TraitType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Culture, FishingBoat)"));
            variableList.add(new NodeVariable(node2, "Yield(Culture, Atoll)"));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_TRAIT_TEXT_FILE_PATH;
        }
    }

    private class Samurai extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class Zero extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }
}
