package chocola.civilizationfiveeditor.v2.config;

import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import chocola.civilizationfiveeditor.v2.model.civilization.impl.America;
import chocola.civilizationfiveeditor.v2.model.civilization.impl.Arabia;
import chocola.civilizationfiveeditor.v2.model.civilization.impl.Aztec;
import chocola.civilizationfiveeditor.v2.model.civilization.impl.China;
import java.util.ArrayList;
import java.util.List;

public class CivilizationConfiguration {

    private static final List<Civilization> CIVILIZATION_LIST = new ArrayList<>();

    public static void init() {
        CIVILIZATION_LIST.clear();
        CIVILIZATION_LIST.add(new America());
        CIVILIZATION_LIST.add(new Arabia());
        CIVILIZATION_LIST.add(new Aztec());
        CIVILIZATION_LIST.add(new China());
    }

    public static List<Civilization> getCivilizationList() {
        return CIVILIZATION_LIST;
    }
}
