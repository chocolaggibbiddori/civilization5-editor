package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.model.*;
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
    protected UniqueBuilding[] getUniqueBuildings(String[] uniqueBuildingTypes) {
        return new UniqueBuilding[0];
    }

    @Override
    public UniqueImprovement[] getUniqueImprovements() {
        return new UniqueImprovement[0];
    }

    private class AmericaTrait extends BasicTrait {

        @Override
        public List<Variable> getVariableList() {
            return List.of(new KeyValueVariable("PlotBuyCostModifier", -50));
        }
    }
}
