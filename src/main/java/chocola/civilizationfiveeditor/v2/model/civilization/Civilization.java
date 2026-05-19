package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.model.GameData.TypedFile;
import chocola.civilizationfiveeditor.v2.model.Trait;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import java.util.List;

public interface Civilization {

    List<TypedFile> requiredFileList();

    String getName();

    String getLeaderName();

    Trait getTrait();

    UniqueUnit[] getUniqueUnits();

    UniqueBuilding[] getUniqueBuildings();
}
