package chocola.civilizationfiveeditor.v2.model.civilization.impl;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.EXPANSION2_INHERITED_TRAIT_TEXT_FILE_PATH;

import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.GodsAndKingsCivilization;
import java.nio.file.Path;
import java.util.List;

public class Carthage extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Dido";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new ByzantiumTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new ForestElephant(), new Quinquereme()};
    }

    private class ByzantiumTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_INHERITED_TRAIT_TEXT_FILE_PATH;
        }
    }

    private class ForestElephant extends GodsAndKingsUniqueUnit {
    }

    private class Quinquereme extends GodsAndKingsUniqueUnit {
    }
}
