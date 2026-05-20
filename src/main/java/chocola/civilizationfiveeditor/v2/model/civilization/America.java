package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.service.GameDataLoader.gameData;

import chocola.civilizationfiveeditor.v2.model.*;
import chocola.civilizationfiveeditor.v2.model.GameData.Type;
import java.util.List;
import org.dom4j.Node;

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
            String key = "PlotBuyCostModifier";
            Node node = gameData
                    .getDocument(Type.TRAIT, TRAIT_FILE_PATH)
                    .selectSingleNode("/GameData/Traits/Row[Type='TRAIT_RIVER_EXPANSION']/%s".formatted(key));

            return List.of(new NodeVariable(node));
        }
    }
}
