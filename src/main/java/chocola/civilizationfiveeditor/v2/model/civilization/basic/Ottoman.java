package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Element;

public class Ottoman extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Suleiman";
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_BASIC_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new OttomanTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Janissary(), new Sipahi()};
    }

    private class OttomanTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement().element("NavalUnitMaintenanceModifier");

            variableList.add(new NodeVariable(node));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_BASIC_TRAIT_FILE_PATH;
        }
    }

    private class Janissary extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }

    private class Sipahi extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }
}
