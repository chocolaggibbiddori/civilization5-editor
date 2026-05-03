package chocola.civilizationfiveeditor.model;

import java.nio.file.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UnitData {
    private final String type;
    private String description;
    private int combat;
    private int rangedCombat;
    private int cost;
    private int moves;
    private int range;
    private Path sourceFile;
}
