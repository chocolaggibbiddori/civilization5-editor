package chocola.civilizationfiveeditor.v2.config;

import chocola.civilizationfiveeditor.v2.model.civilization.*;
import java.util.ArrayList;
import java.util.List;

public class CivilizationConfiguration {

    private static final List<Civilization> CIVILIZATION_LIST = new ArrayList<>();

    public static void init() {
        CIVILIZATION_LIST.clear();
        CIVILIZATION_LIST.add(new America());
    }

    public static List<Civilization> getCivilizationList() {
        return CIVILIZATION_LIST;
    }
}
