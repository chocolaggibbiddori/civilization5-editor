package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
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
    }

    private class ForestElephant extends GodsAndKingsUniqueUnit {
    }

    private class Quinquereme extends GodsAndKingsUniqueUnit {
    }
}
