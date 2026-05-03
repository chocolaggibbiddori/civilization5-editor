package chocola.civilizationfiveeditor.service;

import chocola.civilizationfiveeditor.model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDataLoader {

    private static final String[] EXCLUDED_CIVS = {"CIVILIZATION_BARBARIAN", "CIVILIZATION_MINOR"};

    public GameData load(Path gameRoot) throws Exception {
        GameData data = new GameData();

        Path assetsXml = gameRoot.resolve("Assets/Gameplay/XML");
        Path exp1Xml = gameRoot.resolve("Assets/DLC/Expansion/Gameplay/XML");
        Path exp2Xml = gameRoot.resolve("Assets/DLC/Expansion2/Gameplay/XML");

        // Units: base → exp1 (base + expansion-only) → exp2 (base + expansion-only) → individual DLCs
        loadUnits(data, assetsXml.resolve("Units/CIV5Units.xml"));
        loadUnitsIfExists(data, exp1Xml.resolve("Units/CIV5Units.xml"));
        loadUnitsIfExists(data, exp1Xml.resolve("Units/CIV5Units_Expansion.xml"));
        loadUnitsIfExists(data, exp2Xml.resolve("Units/CIV5Units.xml"));
        loadUnitsIfExists(data, exp2Xml.resolve("Units/CIV5Units_Expansion2.xml"));

        // Buildings
        loadBuildings(data, assetsXml.resolve("Buildings/CIV5Buildings.xml"));
        loadBuildingsIfExists(data, exp1Xml.resolve("Buildings/CIV5Buildings.xml"));
        loadBuildingsIfExists(data, exp1Xml.resolve("Buildings/CIV5Buildings_Expansion.xml"));
        loadBuildingsIfExists(data, exp2Xml.resolve("Buildings/CIV5Buildings.xml"));
        loadBuildingsIfExists(data, exp2Xml.resolve("Buildings/CIV5Buildings_Expansion2.xml"));

        // Traits
        loadTraits(data, assetsXml.resolve("Civilizations/CIV5Traits.xml"));
        loadTraitsIfExists(data, exp1Xml.resolve("Civilizations/CIV5Traits.xml"));
        loadTraitsIfExists(data, exp1Xml.resolve("Civilizations/CIV5Traits_Expansion.xml"));
        loadTraitsIfExists(data, exp2Xml.resolve("Civilizations/CIV5Traits.xml"));
        loadTraitsIfExists(data, exp2Xml.resolve("Civilizations/CIV5Traits_Expansion2.xml"));

        // Leader traits
        loadLeaderTraits(data, assetsXml.resolve("Leaders"));
        loadLeaderTraitsIfExists(data, exp1Xml.resolve("Leaders"));
        loadLeaderTraitsIfExists(data, exp2Xml.resolve("Leaders"));

        // Civilizations
        loadCivs(data, assetsXml.resolve("Civilizations/CIV5Civilizations.xml"));
        loadCivsIfExists(data, exp1Xml.resolve("Civilizations/CIV5Civilizations_Expansion.xml"));
        loadCivsIfExists(data, exp2Xml.resolve("Civilizations/CIV5Civilizations_Expansion2.xml"));

        // Improvements (civ-specific)
        loadImprovementsIfExists(data, exp1Xml.resolve("Terrain/CIV5Improvements_Expansion.xml"));
        loadImprovementsIfExists(data, exp2Xml.resolve("Terrain/CIV5Improvements_Expansion2.xml"));

        // Individual DLC packs (DLC_01, DLC_02, …)
        loadDlcPacks(data, gameRoot);

        // Resolve leader → traits for each civ
        resolveCivTraits(data);

        // Resolve civ-specific improvements → civs
        resolveImprovements(data);

        // Load Korean text
        new TextLoader().load(gameRoot, data.getTexts());

        // Sort civs alphabetically by Korean display name
        data.getCivilizations().sort(Comparator.comparing(c -> data.getText(civShortDescKey(c.getType()))));

        return data;
    }

    private String civShortDescKey(String civType) {
        String name = civType.replace("CIVILIZATION_", "");
        return "TXT_KEY_CIV_" + name + "_SHORT_DESC";
    }

    // ── Units ──────────────────────────────────────────────────────────────

    private void loadUnitsIfExists(GameData data, Path path) throws Exception {
        if (Files.exists(path)) {
            loadUnits(data, path);
        }
    }

    private void loadUnits(GameData data, Path path) throws Exception {
        Document doc = parse(path);
        NodeList rows = doc.getElementsByTagName("Units");
        if (rows.getLength() == 0) {
            return;
        }

        NodeList unitRows = ((Element) rows.item(0)).getElementsByTagName("Row");
        for (int i = 0; i < unitRows.getLength(); i++) {
            Element row = (Element) unitRows.item(i);
            String type = text(row, "Type");
            if (type == null || type.isBlank()) {
                continue;
            }

            UnitData unit = data.getUnits().computeIfAbsent(type, UnitData::new);
            unit.setSourceFile(path);
            unit.setDescription(text(row, "Description"));
            unit.setCombat(intVal(row, "Combat"));
            unit.setRangedCombat(intVal(row, "RangedCombat"));
            unit.setCost(intVal(row, "Cost"));
            unit.setMoves(intVal(row, "Moves"));
            unit.setRange(intVal(row, "Range"));
        }
    }

    // ── Buildings ──────────────────────────────────────────────────────────

    private void loadBuildingsIfExists(GameData data, Path path) throws Exception {
        if (Files.exists(path)) {
            loadBuildings(data, path);
        }
    }

    private void loadBuildings(GameData data, Path path) throws Exception {
        Document doc = parse(path);
        NodeList sections = doc.getElementsByTagName("Buildings");
        if (sections.getLength() == 0) {
            return;
        }

        for (int s = 0; s < sections.getLength(); s++) {
            Element section = (Element) sections.item(s);
            NodeList rows = section.getElementsByTagName("Row");
            if (rows.getLength() == 0) {
                continue;
            }

            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String type = text(row, "Type");
                if (type == null || type.isBlank()) {
                    continue;
                }

                BuildingData b = data.getBuildings().computeIfAbsent(type, BuildingData::new);
                b.setSourceFile(path);
                b.setDescription(text(row, "Description"));
                b.setCost(intVal(row, "Cost"));
                b.setCulture(intVal(row, "Culture"));
                b.setGold(intVal(row, "Gold"));
                b.setHappiness(intVal(row, "Happiness"));
                b.setDefense(intVal(row, "Defense"));
                b.setGoldMaintenance(intVal(row, "GoldMaintenance"));
            }
        }
    }

    // ── Traits ─────────────────────────────────────────────────────────────

    private void loadTraitsIfExists(GameData data, Path path) throws Exception {
        if (Files.exists(path)) {
            loadTraits(data, path);
        }
    }

    private void loadTraits(GameData data, Path path) throws Exception {
        Document doc = parse(path);
        NodeList sections = doc.getElementsByTagName("Traits");
        if (sections.getLength() == 0) {
            return;
        }

        for (int s = 0; s < sections.getLength(); s++) {
            Element section = (Element) sections.item(s);
            NodeList rows = section.getElementsByTagName("Row");
            if (rows.getLength() == 0) {
                continue;
            }

            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String type = text(row, "Type");
                if (type == null || type.isBlank()) {
                    continue;
                }

                TraitData trait = data.getTraits().computeIfAbsent(type, TraitData::new);
                trait.setSourceFile(path);
                trait.setDescription(text(row, "Description"));

                NodeList children = row.getChildNodes();
                for (int c = 0; c < children.getLength(); c++) {
                    Node child = children.item(c);
                    if (child.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    String tagName = child.getNodeName();
                    if (tagName.equals("Type") || tagName.equals("Description") || tagName.equals("ShortDescription")) {
                        continue;
                    }
                    String value = child.getTextContent().trim();
                    if (!value.isBlank()) {
                        trait.setValue(tagName, value);
                    }
                }
            }
        }
    }

    // ── Leader Traits ──────────────────────────────────────────────────────

    private void loadLeaderTraitsIfExists(GameData data, Path dir) throws Exception {
        if (Files.exists(dir)) {
            loadLeaderTraits(data, dir);
        }
    }

    private void loadLeaderTraits(GameData data, Path dir) throws Exception {
        if (!Files.exists(dir)) {
            return;
        }
        try (var stream = Files.list(dir)) {
            for (Path file : stream.toList()) {
                if (file.getFileName().toString().startsWith("CIV5Leader_")) {
                    loadLeaderTraitsFromFile(data, file);
                }
            }
        }
    }

    private void loadLeaderTraitsFromFile(GameData data, Path path) throws Exception {
        Document doc = parse(path);
        NodeList sections = doc.getElementsByTagName("Leader_Traits");
        for (int s = 0; s < sections.getLength(); s++) {
            NodeList rows = ((Element) sections.item(s)).getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String leaderType = text(row, "LeaderType");
                String traitType = text(row, "TraitType");
                if (leaderType == null || traitType == null) {
                    continue;
                }
                List<String> traits = data.getLeaderTraits().computeIfAbsent(leaderType, k -> new ArrayList<>());
                if (!traits.contains(traitType)) {
                    traits.add(traitType);
                }
            }
        }
    }

    // ── Civilizations ──────────────────────────────────────────────────────

    private void loadCivsIfExists(GameData data, Path path) throws Exception {
        if (Files.exists(path)) {
            loadCivs(data, path);
        }
    }

    private void loadCivs(GameData data, Path path) throws Exception {
        Document doc = parse(path);
        Set<String> excluded = new HashSet<>(Arrays.asList(EXCLUDED_CIVS));

        Map<String, CivEntry> civMap = new LinkedHashMap<>();
        for (CivEntry c : data.getCivilizations()) {
            civMap.put(c.getType(), c);
        }

        NodeList civSections = doc.getElementsByTagName("Civilizations");
        for (int s = 0; s < civSections.getLength(); s++) {
            NodeList rows = ((Element) civSections.item(s)).getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String type = text(row, "Type");
                if (type == null || excluded.contains(type)) {
                    continue;
                }
                civMap.computeIfAbsent(type, k -> {
                    CivEntry entry = new CivEntry(k);
                    data.getCivilizations().add(entry);
                    return entry;
                });
            }
        }

        NodeList leaderSections = doc.getElementsByTagName("Civilization_Leaders");
        for (int s = 0; s < leaderSections.getLength(); s++) {
            NodeList rows = ((Element) leaderSections.item(s)).getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String civType = text(row, "CivilizationType");
                String leaderType = text(row, "LeaderheadType");
                if (civType == null || leaderType == null) {
                    continue;
                }
                CivEntry civ = getCivByType(data, civType);
                if (civ != null) {
                    civ.setLeaderType(leaderType);
                }
            }
        }

        NodeList unitOverrideSections = doc.getElementsByTagName("Civilization_UnitClassOverrides");
        for (int s = 0; s < unitOverrideSections.getLength(); s++) {
            NodeList rows = ((Element) unitOverrideSections.item(s)).getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String civType = text(row, "CivilizationType");
                String unitType = text(row, "UnitType");
                if (civType == null || unitType == null || unitType.isBlank()) {
                    continue;
                }
                if (excluded.contains(civType)) {
                    continue;
                }
                CivEntry civ = getCivByType(data, civType);
                if (civ != null && !civ.getUniqueUnitTypes().contains(unitType)) {
                    civ.getUniqueUnitTypes().add(unitType);
                }
            }
        }

        NodeList buildingOverrideSections = doc.getElementsByTagName("Civilization_BuildingClassOverrides");
        for (int s = 0; s < buildingOverrideSections.getLength(); s++) {
            NodeList rows = ((Element) buildingOverrideSections.item(s)).getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String civType = text(row, "CivilizationType");
                String buildingType = text(row, "BuildingType");
                if (civType == null || buildingType == null || buildingType.isBlank()) {
                    continue;
                }
                if (excluded.contains(civType)) {
                    continue;
                }
                CivEntry civ = getCivByType(data, civType);
                if (civ != null && !civ.getUniqueBuildingTypes().contains(buildingType)) {
                    civ.getUniqueBuildingTypes().add(buildingType);
                }
            }
        }
    }

    // ── DLC Packs (DLC_01, DLC_02, …) ─────────────────────────────────────

    private void loadDlcPacks(GameData data, Path gameRoot) throws Exception {
        Path dlcBase = gameRoot.resolve("Assets/DLC");
        if (!Files.exists(dlcBase)) {
            return;
        }
        try (var dlcDirs = Files.list(dlcBase)) {
            for (Path dlcDir : dlcDirs.toList()) {
                if (!dlcDir.getFileName().toString().startsWith("DLC_")) {
                    continue;
                }
                Path xmlDir = dlcDir.resolve("Gameplay/XML");
                if (!Files.exists(xmlDir)) {
                    continue;
                }
                try (var xmlFiles = Files.list(xmlDir)) {
                    for (Path xmlFile : xmlFiles.toList()) {
                        String name = xmlFile.getFileName().toString();
                        if (!name.endsWith(".xml")) {
                            continue;
                        }
                        if (name.startsWith("CIV5Units")) {
                            loadUnits(data, xmlFile);
                        } else if (name.startsWith("CIV5Buildings")) {
                            loadBuildings(data, xmlFile);
                        } else if (name.startsWith("CIV5Traits")) {
                            loadTraits(data, xmlFile);
                        } else if (name.startsWith("CIV5Leader_")) {
                            loadLeaderTraitsFromFile(data, xmlFile);
                        } else if (name.startsWith("CIV5Improvements")) {
                            loadImprovements(data, xmlFile);
                        } else if (name.startsWith("CIV5Civilization")) {
                            loadCivs(data, xmlFile);
                        }
                    }
                }
            }
        }
    }

    // ── Improvements ───────────────────────────────────────────────────────

    private void loadImprovementsIfExists(GameData data, Path path) throws Exception {
        if (Files.exists(path)) {
            loadImprovements(data, path);
        }
    }

    private void loadImprovements(GameData data, Path path) throws Exception {
        Document doc = parse(path);

        NodeList sections = doc.getElementsByTagName("Improvements");
        for (int s = 0; s < sections.getLength(); s++) {
            Element section = (Element) sections.item(s);
            NodeList rows = section.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String type = text(row, "Type");
                String civType = text(row, "CivilizationType");
                if (type == null || civType == null) {
                    continue;
                }
                ImprovementData imp = data.getImprovements().computeIfAbsent(type, ImprovementData::new);
                imp.setSourceFile(path);
                imp.setCivType(civType);
                imp.setDescription(text(row, "Description"));
            }
        }

        NodeList yieldSections = doc.getElementsByTagName("Improvement_Yields");
        for (int s = 0; s < yieldSections.getLength(); s++) {
            Element section = (Element) yieldSections.item(s);
            NodeList rows = section.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                String impType = text(row, "ImprovementType");
                String yieldType = text(row, "YieldType");
                if (impType == null || yieldType == null) {
                    continue;
                }
                ImprovementData imp = data.getImprovements().get(impType);
                if (imp == null) {
                    continue;
                }
                int val = intVal(row, "Yield");
                switch (yieldType) {
                    case "YIELD_FOOD" -> imp.setFood(val);
                    case "YIELD_PRODUCTION" -> imp.setProduction(val);
                    case "YIELD_GOLD" -> imp.setGold(val);
                    case "YIELD_SCIENCE" -> imp.setScience(val);
                    case "YIELD_CULTURE" -> imp.setCulture(val);
                    case "YIELD_FAITH" -> imp.setFaith(val);
                }
            }
        }
    }

    // ── Post-processing ────────────────────────────────────────────────────

    private void resolveCivTraits(GameData data) {
        for (CivEntry civ : data.getCivilizations()) {
            String leader = civ.getLeaderType();
            if (leader == null) {
                continue;
            }
            List<String> traits = data.getLeaderTraits().get(leader);
            if (traits != null) {
                civ.getTraitTypes().addAll(traits);
            }
        }
    }

    private void resolveImprovements(GameData data) {
        for (ImprovementData imp : data.getImprovements().values()) {
            String civType = imp.getCivType();
            if (civType == null) {
                continue;
            }
            CivEntry civ = getCivByType(data, civType);
            if (civ != null && !civ.getUniqueImprovementTypes().contains(imp.getType())) {
                civ.getUniqueImprovementTypes().add(imp.getType());
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private Document parse(Path path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(path.toFile());
    }

    private String text(Element row, String tag) {
        NodeList list = row.getElementsByTagName(tag);
        if (list.getLength() == 0) {
            return null;
        }
        String content = list.item(0).getTextContent().trim();
        return content.isBlank() ? null : content;
    }

    private int intVal(Element row, String tag) {
        String val = text(row, tag);
        if (val == null) {
            return 0;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private CivEntry getCivByType(GameData data, String type) {
        return data.getCivilizations().stream()
                .filter(c -> c.getType().equals(type))
                .findFirst().orElse(null);
    }
}
