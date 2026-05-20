package chocola.civilizationfiveeditor.v2.service;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_BACKUP_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;
import static java.util.stream.Collectors.toSet;

import chocola.civilizationfiveeditor.v2.model.Variable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import org.dom4j.Document;
import org.dom4j.Node;

public class GameDataSaver {

    public static void save(Set<Variable> variableSet) {
        variableSet
                .stream()
                .filter(Variable::isChanged)
                .peek(variable -> {
                    Node node = variable.getNode();
                    int value = variable.getValue();

                    node.setText(String.valueOf(value));
                    variable.setOriginValue(value);
                })
                .map(variable -> variable.getNode().getDocument())
                .collect(toSet())
                .forEach(document -> {
                    backup(document);
                    save(document);
                });
    }

    private static void backup(Document document) {
        Path origin = Path.of(document.getName().substring("file:///".length()));
        Path backupPath = DEFAULT_BACKUP_PATH.resolve(DEFAULT_GAME_PATH.relativize(origin));

        try {
            if (Files.notExists(backupPath)) {
                Files.createDirectories(backupPath.getParent());
                Files.copy(origin, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void save(Document document) {
        Path origin = Path.of(document.getName().substring("file:///".length()));

        try (var writer = Files.newBufferedWriter(origin, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            document.write(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
