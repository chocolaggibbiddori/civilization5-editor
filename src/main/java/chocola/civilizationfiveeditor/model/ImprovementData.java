package chocola.civilizationfiveeditor.model;

import java.nio.file.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ImprovementData {
    private final String type;
    private String description;
    private String civType;
    private Path sourceFile;
    private int food;
    private int production;
    private int gold;
    private int science;
    private int culture;
    private int faith;
}
