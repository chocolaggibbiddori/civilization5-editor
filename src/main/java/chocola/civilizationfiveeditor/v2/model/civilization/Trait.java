package chocola.civilizationfiveeditor.v2.model.civilization;

import java.util.List;

public interface Trait {

    String getDescription();

    List<TraitVariable> getVariableList();
}
