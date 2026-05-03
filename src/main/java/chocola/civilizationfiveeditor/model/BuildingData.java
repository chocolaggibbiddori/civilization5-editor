package chocola.civilizationfiveeditor.model;

import java.nio.file.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BuildingData {
    private final String type;
    private String description;
    private int cost;
    private int culture;
    private int gold;
    private int happiness;
    private int science;
    private int defense;
    private int goldMaintenance;
    private Path sourceFile;
}
