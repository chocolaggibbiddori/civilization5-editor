package chocola.civilizationfiveeditor.v2.model.civilization;

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

    private class AmericaTrait extends BasicTrait {

        @Override
        public List<TraitVariable> getVariableList() {
            return List.of(new BasicTraitVariable("PlotBuyCostModifier", -50));
        }
    }
}
