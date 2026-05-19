package chocola.civilizationfiveeditor.v2.model;

public interface Variable {

    String getKey();

    int getValue();

    void setValue(int value);

    void setValue(String value);

    boolean isChanged();

    boolean equals(Object obj);

    int hashCode();
}
