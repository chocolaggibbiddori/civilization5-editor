package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.model.Trait;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueImprovement;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import java.nio.file.Path;
import java.util.List;

public interface Civilization {

    List<Path> requiredPathList();

    String getName();

    String getLeaderName();

    Trait getTrait();

    UniqueUnit[] getUniqueUnits();

    UniqueBuilding[] getUniqueBuildings();

    UniqueImprovement[] getUniqueImprovements();
}
