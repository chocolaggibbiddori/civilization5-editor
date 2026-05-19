package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.model.KeyValueVariable;
import chocola.civilizationfiveeditor.v2.model.Trait;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;

public class America extends BasicCivilization {

    private final AmericaTrait trait;

    public America() {
        super();
        trait = new AmericaTrait();
    }

    @Override
    protected String getLeaderEnglishName() {
        return "Washington";
    }

    @Override
    public Trait getTrait() {
        return trait;
    }

    @Override
    protected String[] getUniqueUnitTypes() {
        return new String[]{"AMERICAN_MINUTEMAN", "AMERICAN_B17"};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[0];
    }

    private class AmericaTrait extends BasicTrait {

        @Override
        public List<Variable> getVariableList() {
            return List.of(new KeyValueVariable("PlotBuyCostModifier", -50));
        }
    }
}
