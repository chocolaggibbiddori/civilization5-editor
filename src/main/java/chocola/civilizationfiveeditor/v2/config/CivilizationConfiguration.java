package chocola.civilizationfiveeditor.v2.config;

import chocola.civilizationfiveeditor.v2.model.civilization.Civilization;
import chocola.civilizationfiveeditor.v2.model.civilization.basic.*;
import chocola.civilizationfiveeditor.v2.model.civilization.bnw.*;
import chocola.civilizationfiveeditor.v2.model.civilization.dlc.Mongol;
import chocola.civilizationfiveeditor.v2.model.civilization.gak.*;
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
        CIVILIZATION_LIST.add(new Rome());
        CIVILIZATION_LIST.add(new Russia());
        CIVILIZATION_LIST.add(new Siam());
        CIVILIZATION_LIST.add(new Songhai());
        CIVILIZATION_LIST.add(new Austria());
        CIVILIZATION_LIST.add(new Byzantium());
        CIVILIZATION_LIST.add(new Carthage());
        CIVILIZATION_LIST.add(new Celts());
        CIVILIZATION_LIST.add(new Ethiopia());
        CIVILIZATION_LIST.add(new Huns());
        CIVILIZATION_LIST.add(new Maya());
        CIVILIZATION_LIST.add(new Netherlands());
        CIVILIZATION_LIST.add(new Sweden());
        CIVILIZATION_LIST.add(new Assyria());
        CIVILIZATION_LIST.add(new Brazil());
        CIVILIZATION_LIST.add(new Indonesia());
        CIVILIZATION_LIST.add(new Morocco());
        CIVILIZATION_LIST.add(new Poland());
        CIVILIZATION_LIST.add(new Portugal());
        CIVILIZATION_LIST.add(new Shoshone());
        CIVILIZATION_LIST.add(new Venice());
        CIVILIZATION_LIST.add(new Zulu());
        CIVILIZATION_LIST.add(new Mongol());
    }

    public static List<Civilization> getCivilizationList() {
        return CIVILIZATION_LIST;
    }
}
