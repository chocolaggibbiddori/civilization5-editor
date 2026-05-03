package chocola.civilizationfiveeditor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class CivEntry {
    private final String type;
    @Setter private String leaderType;
    private final List<String> uniqueUnitTypes     = new ArrayList<>();
    private final List<String> uniqueBuildingTypes = new ArrayList<>();
    private final List<String> traitTypes          = new ArrayList<>();

    /** CIVILIZATION_AMERICA → America */
    public String getDisplayName() {
        String name = type.replace("CIVILIZATION_", "");
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    @Override
    public String toString() { return getDisplayName(); }
}
