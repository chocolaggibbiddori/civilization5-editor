package chocola.civilizationfiveeditor.v2.util;

import java.nio.file.Path;

public class PathUtils {

    public static final Path DEFAULT_GAME_PATH = Path.of("C:/Program Files (x86)/Steam/steamapps/common/Sid Meier's Civilization V/Assets");
    public static final Path DEFAULT_BACKUP_PATH = Path.of(System.getProperty("user.home"), ".civ5editor", "backups");
}
