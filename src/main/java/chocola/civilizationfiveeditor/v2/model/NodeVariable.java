package chocola.civilizationfiveeditor.v2.model;

import org.dom4j.Node;

public class NodeVariable implements Variable {

    private final Node node;
    private final String key;

    private int originValue;
    private int value;

    public NodeVariable(Node node) {
        this(node, node.getName());
    }

    public NodeVariable(Node node, String key) {
        this.node = node;
        this.key = key;

        int value = Integer.parseInt(node.getText().trim());
        this.originValue = value;
        this.value = value;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setOriginValue(int originValue) {
        this.originValue = originValue;
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
        if (value.isBlank()) {
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
