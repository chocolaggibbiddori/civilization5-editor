package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;

public abstract class BasicCivilization extends AbstractCivilization {

    @Override
    protected Path getDefaultLeaderPath() {
        return DEFAULT_LEADER_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return LEADER_TEXT_FILE_PATH;
    }

    public abstract class BasicTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return TRAIT_TEXT_FILE_PATH;
        }
    }

    public abstract class BasicUniqueUnit extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return UNIT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    public abstract class BasicUniqueBuilding extends AbstractUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return BUILDING_TEXT_FILE_PATH;
        }
    }

    public abstract class BasicUniqueImprovement extends AbstractUniqueImprovement {

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
