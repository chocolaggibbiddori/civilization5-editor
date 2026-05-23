package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Node;

public class England extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Elizabeth";
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
        return new EnglandTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new LongBowman(), new ShipOfTheLine()};
    }

    private class EnglandTrait extends BasicTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_JON_INHERITED_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node row = getRow();
            Node node1 = row.selectSingleNode("ExtraEmbarkMoves");
            Node node2 = row.selectSingleNode("ExtraSpies");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }
    }

    private class LongBowman extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class ShipOfTheLine extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }
}
