package chocola.civilizationfiveeditor.v2.config;

import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import chocola.civilizationfiveeditor.v2.model.civilization.impl.*;
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
        CIVILIZATION_LIST.add(new Egypt());
        CIVILIZATION_LIST.add(new England());
        CIVILIZATION_LIST.add(new France());
        CIVILIZATION_LIST.add(new Germany());
        CIVILIZATION_LIST.add(new Greece());
        CIVILIZATION_LIST.add(new India());
        CIVILIZATION_LIST.add(new Iroquois());
        CIVILIZATION_LIST.add(new Japan());
        CIVILIZATION_LIST.add(new Ottoman());
        CIVILIZATION_LIST.add(new Persia());
    }

    public static List<Civilization> getCivilizationList() {
        return CIVILIZATION_LIST;
    }
}
