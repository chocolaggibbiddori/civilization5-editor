package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;

public abstract class BraveNewWorldCivilization extends AbstractCivilization {

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION2_DEFAULT_LEADER_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return EXPANSION2_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return EXPANSION2_LEADER_TEXT_FILE_PATH;
    }

    public abstract class BraveNewWorldTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_TRAIT_TEXT_FILE_PATH;
        }
    }

    public abstract class BraveNewWorldUniqueUnit extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return EXPANSION2_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return EXPANSION2_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    public abstract class BraveNewWorldUniqueBuilding extends AbstractUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return EXPANSION2_BUILDING_TEXT_FILE_PATH;
        }
    }

    public abstract class BraveNewWorldUniqueImprovement extends AbstractUniqueImprovement {

        @Override
        protected Path getImprovementFilePath() {
            return EXPANSION2_IMPROVEMENT_FILE_PATH;
        }

        @Override
        protected Path getImprovementTextFilePath() {
            return EXPANSION2_OBJECT_TEXT_FILE_PATH;
        }
    }
}
