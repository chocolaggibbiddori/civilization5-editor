package chocola.civilizationfiveeditor.v2.model.civilization;

import chocola.civilizationfiveeditor.v2.model.GameData.TypedFile;
import java.util.List;

public interface Civilization {

    List<TypedFile> requiredFileList();

    String getName();

    String getLeaderName();

    Trait getTrait();
}
