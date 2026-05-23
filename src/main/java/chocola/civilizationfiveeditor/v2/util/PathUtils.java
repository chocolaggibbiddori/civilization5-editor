package chocola.civilizationfiveeditor.v2.util;

import java.nio.file.Path;

public class PathUtils {

    public static final Path DEFAULT_GAME_PATH = Path.of("C:/Program Files (x86)/Steam/steamapps/common/Sid Meier's Civilization V/Assets");
    public static final Path DEFAULT_BACKUP_PATH = Path.of(System.getProperty("user.home"), ".civ5editor", "backups");

    public static final Path BASIC_GAME_PATH = DEFAULT_GAME_PATH.resolve("Gameplay/XML");
    public static final Path EXPANSION_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion/Gameplay/XML");
    public static final Path EXPANSION2_GAME_PATH = DEFAULT_GAME_PATH.resolve("DLC/Expansion2/Gameplay/XML");

    // basic
    public static final Path DEFAULT_TEXT_PATH = BASIC_GAME_PATH.resolve("NewText/KO_KR");
    public static final Path CIVILIZATION_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Civilizations.xml");
    public static final Path TRAIT_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Jon.xml");
    public static final Path LEADER_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Leaders.xml");
    public static final Path UNIT_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Units.xml");
    public static final Path CIVILOPEDIA_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Civilopedia.xml");
    public static final Path BUILDING_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Buildings.xml");
    public static final Path OBJECT_TEXT_FILE_PATH = DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Objects.xml");

    public static final Path DEFAULT_CIVILIZATION_PATH = BASIC_GAME_PATH.resolve("Civilizations");
    public static final Path CIVILIZATION_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Civilizations.xml");
    public static final Path TRAIT_FILE_PATH = DEFAULT_CIVILIZATION_PATH.resolve("CIV5Traits.xml");

    public static final Path DEFAULT_LEADER_PATH = BASIC_GAME_PATH.resolve("Leaders");

    public static final Path DEFAULT_UNIT_PATH = BASIC_GAME_PATH.resolve("Units");
    public static final Path UNIT_FILE_PATH = DEFAULT_UNIT_PATH.resolve("CIV5Units.xml");

    public static final Path DEFAULT_BUILDING_PATH = BASIC_GAME_PATH.resolve("Buildings");
    public static final Path BUILDING_FILE_PATH = DEFAULT_BUILDING_PATH.resolve("CIV5Buildings.xml");

    // expansion
    public static final Path EXPANSION_DEFAULT_LEADER_PATH = EXPANSION_GAME_PATH.resolve("Leaders");

    // expansion2
    public static final Path EXPANSION2_DEFAULT_TEXT_PATH = EXPANSION2_GAME_PATH.resolve("Text/KO_KR");
    public static final Path EXPANSION2_TRAIT_TEXT_FILE_PATH = EXPANSION2_DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Traits_Expansion2.xml");
    public static final Path EXPANSION2_INHERITED_TRAIT_TEXT_FILE_PATH = EXPANSION2_DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Traits_Inherited_Expansion2.xml");
    public static final Path EXPANSION2_JON_INHERITED_TEXT_FILE_PATH = EXPANSION2_DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Jon_Inherited_Expansion2.xml");
    public static final Path EXPANSION2_OBJECT_TEXT_FILE_PATH = EXPANSION2_DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Objects_Expansion2.xml");
    public static final Path EXPANSION2_BUILDING_TEXT_FILE_PATH = EXPANSION2_DEFAULT_TEXT_PATH.resolve("CIV5GameTextInfos_Buildings_Expansion2.xml");

    public static final Path EXPANSION2_DEFAULT_CIVILIZATION_PATH = EXPANSION2_GAME_PATH.resolve("Civilizations");
    public static final Path EXPANSION2_CIVILIZATION_FILE_PATH = EXPANSION2_DEFAULT_CIVILIZATION_PATH.resolve("CIV5Civilizations.xml");
    public static final Path EXPANSION2_TRAIT_FILE_PATH = EXPANSION2_DEFAULT_CIVILIZATION_PATH.resolve("CIV5Traits.xml");

    public static final Path EXPANSION2_DEFAULT_UNIT_PATH = EXPANSION2_GAME_PATH.resolve("Units");
    public static final Path EXPANSION2_UNIT_FILE_PATH = EXPANSION2_DEFAULT_UNIT_PATH.resolve("CIV5Units.xml");

    public static final Path EXPANSION2_DEFAULT_BUILDING_PATH = EXPANSION2_GAME_PATH.resolve("Buildings");
    public static final Path EXPANSION2_BASIC_BUILDING_FILE_PATH = EXPANSION2_DEFAULT_BUILDING_PATH.resolve("CIV5Buildings.xml");
    public static final Path EXPANSION2_BUILDING_FILE_PATH = EXPANSION2_DEFAULT_BUILDING_PATH.resolve("CIV5Buildings_Expansion2.xml");

    public static final Path EXPANSION2_DEFAULT_TERRAIN_PATH = EXPANSION2_GAME_PATH.resolve("Terrain");
    public static final Path EXPANSION2_IMPROVEMENT_FILE_PATH = EXPANSION2_DEFAULT_TERRAIN_PATH.resolve("CIV5Improvements_Expansion2.xml");
}
