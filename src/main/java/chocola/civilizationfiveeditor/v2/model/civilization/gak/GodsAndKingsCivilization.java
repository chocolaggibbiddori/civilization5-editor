package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.AbstractCivilization;
import java.nio.file.Path;
import java.util.List;

public abstract class GodsAndKingsCivilization extends AbstractCivilization {

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected Path getCivilizationTextFilePath() {
        return EXPANSION_CIVILIZATION_TEXT_FILE_PATH;
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getLeaderTextFilePath() {
        return EXPANSION_LEADER_TEXT_FILE_PATH;
    }

    public abstract class GodsAndKingsTrait extends AbstractTrait {

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_INHERITED_TRAIT_TEXT_FILE_PATH;
        }
    }

    public abstract class GodsAndKingsUniqueUnit extends AbstractUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION_UNIT_FILE_PATH;
        }

        @Override
        protected Path getCivilopediaTextFilePath() {
            return EXPANSION_CIVILOPEDIA_TEXT_FILE_PATH;
        }

        @Override
        protected Path getUnitTextFilePath() {
            return EXPANSION_UNIT_TEXT_FILE_PATH;
        }

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    public abstract class GodsAndKingsUniqueBuilding extends AbstractUniqueBuilding {

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return EXPANSION_BUILDING_TEXT_FILE_PATH;
        }
    }

    public abstract class GodsAndKingsUniqueImprovement extends AbstractUniqueImprovement {

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
