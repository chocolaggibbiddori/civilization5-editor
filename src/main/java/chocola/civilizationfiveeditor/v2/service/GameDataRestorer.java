package chocola.civilizationfiveeditor.v2.service;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_BACKUP_PATH;
import static chocola.civilizationfiveeditor.v2.util.PathUtils.DEFAULT_GAME_PATH;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class GameDataRestorer {

    public static void restore() {
        if (!canRestore()) {
            return;
        }

        try {
            Files.walkFileTree(DEFAULT_BACKUP_PATH, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, DEFAULT_GAME_PATH.resolve(DEFAULT_BACKUP_PATH.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(file);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean canRestore() {
        try (var children = Files.list(DEFAULT_BACKUP_PATH)) {
            return children.findFirst().isPresent();
        } catch (IOException e) {
            return false;
        }
    }
}
