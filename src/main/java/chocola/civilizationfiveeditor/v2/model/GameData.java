package chocola.civilizationfiveeditor.v2.model;

import java.nio.file.Path;
import java.util.Map;
import org.dom4j.Document;

public record GameData(Map<String, Document> documentMap) {

    public Document getDocument(Path documentPath) {
        String documentName = documentPath.toString();
        return documentMap.get("file:///" + documentName.replaceAll("\\\\", "/"));
    }
}
