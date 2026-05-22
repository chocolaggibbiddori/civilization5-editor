package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_CIVILIZATION_FILE_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_TRAIT_FILE_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_UNIT_FILE_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION_DEFAULT_LEADER_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Node;

public class America extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Washington";
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
        return new AmericaTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new B17(), new Minuteman()};
    }

    private class AmericaTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Node node = getRow().selectSingleNode("PlotBuyCostModifier");

            variableList.add(new NodeVariable(node));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }
    }

    private class B17 extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class Minuteman extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }
}
