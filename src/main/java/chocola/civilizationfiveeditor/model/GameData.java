package chocola.civilizationfiveeditor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class GameData {
    private final List<CivEntry>              civilizations = new ArrayList<>();
    private final Map<String, UnitData>       units         = new LinkedHashMap<>();
    private final Map<String, BuildingData>   buildings     = new LinkedHashMap<>();
    private final Map<String, TraitData>      traits        = new LinkedHashMap<>();
    private final Map<String, List<String>>   leaderTraits  = new HashMap<>();
    private final Map<String, String>         texts         = new LinkedHashMap<>();

    /** Resolve a TXT_KEY to Korean text; returns the key itself if not found. */
    public String getText(String key) {
        if (key == null) return "";
        return texts.getOrDefault(key, key);
    }

    public UnitData     getUnit(String type)     { return units.get(type); }
    public BuildingData getBuilding(String type) { return buildings.get(type); }
    public TraitData    getTrait(String type)    { return traits.get(type); }
}
