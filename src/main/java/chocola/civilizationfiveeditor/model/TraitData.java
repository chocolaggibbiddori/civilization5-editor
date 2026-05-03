package chocola.civilizationfiveeditor.model;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TraitData {
    private final String type;
    private String description;
    private Path sourceFile;
    private final Map<String, String> values = new LinkedHashMap<>();

    public void setValue(String key, String value) {
        values.put(key, value);
    }

    public String getValue(String key) {
        return values.getOrDefault(key, "0");
    }
}
