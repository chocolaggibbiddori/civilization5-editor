package chocola.civilizationfiveeditor.v2.model;

import org.dom4j.Node;

public interface Variable {

    Node getNode();

    String getKey();

    void setOriginValue(int originValue);

    int getValue();

    void setValue(int value);

    void setValue(String value);

    boolean isChanged();
}
