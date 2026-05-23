package chocola.civilizationfiveeditor.v2.model.civilization.impl;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_CIVILIZATION_FILE_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_TRAIT_FILE_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_UNIT_FILE_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION_DEFAULT_LEADER_PATH;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.BasicCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Element;

public class Rome extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Augustus";
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
        return new RomeTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Ballista(), new Legion()};
    }

    private class RomeTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement().element("CapitalBuildingModifier");

            variableList.add(new NodeVariable(node));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }
    }

    private class Ballista extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class Legion extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }
}
