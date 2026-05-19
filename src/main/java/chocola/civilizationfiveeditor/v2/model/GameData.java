package chocola.civilizationfiveeditor.v2.model;

import java.io.File;
import java.net.URI;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.dom4j.Document;

public record GameData(EnumMap<Type, Map<String, Document>> dataMap) {

    public GameData(List<DataType> dataTypeList) {
        this(new EnumMap<>(Type.class));

        dataTypeList.forEach(dataType -> {
            Type type = dataType.type();
            Document document = dataType.document();
            String documentName = document.getName();

            dataMap.computeIfAbsent(type, k -> new HashMap<>()).put(documentName, document);
        });
    }

    public Document getDocument(Type type, String documentName) {
        return dataMap.get(type).get("file:///" + documentName.replaceAll("\\\\", "/"));
    }

    public record DataType(Type type, Document document) {
    }

    public enum Type {

        TEXT, CIVILIZATION, LEADER, TRAIT, UNIT
    }

    @Getter
    public static class TypedFile extends File {

        private final Type type;

        public TypedFile(Type type, URI uri) {
            super(uri);
            this.type = type;
        }
    }
}
