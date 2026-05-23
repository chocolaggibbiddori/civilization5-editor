package chocola.civilizationfiveeditor.v2.model.civilization.gak;

import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;

public class Byzantium extends GodsAndKingsCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Theodora";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new ByzantiumTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Cataphract(), new Dromon()};
    }

    private class ByzantiumTrait extends GodsAndKingsTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
        }
    }

    private class Cataphract extends GodsAndKingsUniqueUnit {
    }

    private class Dromon extends GodsAndKingsUniqueUnit {
    }
}
