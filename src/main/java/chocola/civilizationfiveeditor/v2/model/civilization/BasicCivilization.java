package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import java.nio.file.Path;

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
}
