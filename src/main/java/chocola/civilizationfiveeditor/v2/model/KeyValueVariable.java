package chocola.civilizationfiveeditor.v2.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class KeyValueVariable implements Variable {

    private final String key;
    private final int originValue;

    @EqualsAndHashCode.Exclude
    private int value;

    public KeyValueVariable(String key, int value) {
        this.key = key;
        this.originValue = value;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void setValue(String value) {
        if (value == null) {
            this.value = 0;
            return;
        }

        setValue(Integer.parseInt(value));
    }

    @Override
    public boolean isChanged() {
        return originValue != value;
    }
}
