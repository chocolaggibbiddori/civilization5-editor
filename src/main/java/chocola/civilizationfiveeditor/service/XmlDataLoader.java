package chocola.civilizationfiveeditor.service;

import chocola.civilizationfiveeditor.model.BuildingData;
import chocola.civilizationfiveeditor.model.CivEntry;
import chocola.civilizationfiveeditor.model.GameData;
import chocola.civilizationfiveeditor.model.TraitData;
import chocola.civilizationfiveeditor.model.UnitData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        // Units: base → exp1 → exp2, then individual DLC units
        loadUnits(data, assetsXml.resolve("Units/CIV5Units.xml"));
        loadUnitsIfExists(data, exp1Xml.resolve("Units/CIV5Units.xml"));
        loadUnitsIfExists(data, exp2Xml.resolve("Units/CIV5Units.xml"));
        loadDlcUnits(data, gameRoot);

        // Buildings
        loadBuildings(data, assetsXml.resolve("Buildings/CIV5Buildings.xml"));
        loadBuildingsIfExists(data, exp1Xml.resolve("Buildings/CIV5Buildings.xml"));
        loadBuildingsIfExists(data, exp2Xml.resolve("Buildings/CIV5Buildings.xml"));
        loadDlcBuildings(data, gameRoot);

        // Traits
        loadTraits(data, assetsXml.resolve("Civilizations/CIV5Traits.xml"));
        loadTraitsIfExists(data, exp1Xml.resolve("Civilizations/CIV5Traits.xml"));
        loadTraitsIfExists(data, exp2Xml.resolve("Civilizations/CIV5Traits.xml"));
        loadDlcTraits(data, gameRoot);

        // Leader traits (base)
        loadLeaderTraits(data, assetsXml.resolve("Leaders"));
        loadLeaderTraitsIfExists(data, exp1Xml.resolve("Leaders"));
        loadLeaderTraitsIfExists(data, exp2Xml.resolve("Leaders"));
        loadDlcLeaderTraits(data, gameRoot);

        // Civilizations: base
        loadCivs(data, assetsXml.resolve("Civilizations/CIV5Civilizations.xml"));
        // Expansion civs
        loadCivsIfExists(data, exp1Xml.resolve("Civilizations/CIV5Civilizations_Expansion.xml"));
        loadCivsIfExists(data, exp2Xml.resolve("Civilizations/CIV5Civilizations_Expansion2.xml"));
        // Individual DLC civs
        loadDlcCivs(data, gameRoot);

        // Resolve leader → traits for each civ
        resolveCivTraits(data);

        // Load Korean text
        new TextLoader().load(gameRoot, data.getTexts());

        // Sort civs alphabetically by Korean display name
        data.getCivilizations().sort(Comparator.comparing(c -> data.getText(civShortDescKey(c.getType()))));

        return data;
    }

    private String civShortDescKey(String civType) {
        // CIVILIZATION_AMERICA → TXT_KEY_CIV_AMERICA_SHORT_DESC
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

    private void loadDlcUnits(GameData data, Path gameRoot) throws Exception {
        for (String expansion : new String[]{"Expansion", "Expansion2"}) {
            Path dlcBase = gameRoot.resolve("Assets/DLC/" + expansion + "/DLC");
            if (!Files.exists(dlcBase)) {
                continue;
            }
            try (var stream = Files.list(dlcBase)) {
                for (Path dlcDir : stream.toList()) {
                    Path xmlDir = dlcDir.resolve("Gameplay/XML");
                    if (!Files.exists(xmlDir)) {
                        continue;
                    }
                    try (var xmlFiles = Files.list(xmlDir)) {
                        for (Path xmlFile : xmlFiles.toList()) {
                            if (xmlFile.getFileName().toString().startsWith("CIV5Units")) {
                                loadUnits(data, xmlFile);
                            }
                        }
                    }
                }
            }
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

        // Could be multiple <Buildings> sections across repeated loads; pick first with Row children
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

    private void loadDlcBuildings(GameData data, Path gameRoot) throws Exception {
        for (String expansion : new String[]{"Expansion", "Expansion2"}) {
            Path dlcBase = gameRoot.resolve("Assets/DLC/" + expansion + "/DLC");
            if (!Files.exists(dlcBase)) {
                continue;
            }
            try (var stream = Files.list(dlcBase)) {
                for (Path dlcDir : stream.toList()) {
                    Path xmlDir = dlcDir.resolve("Gameplay/XML");
                    if (!Files.exists(xmlDir)) {
                        continue;
                    }
                    try (var xmlFiles = Files.list(xmlDir)) {
                        for (Path xmlFile : xmlFiles.toList()) {
                            if (xmlFile.getFileName().toString().startsWith("CIV5Buildings")) {
                                loadBuildings(data, xmlFile);
                            }
                        }
                    }
                }
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

    private void loadDlcTraits(GameData data, Path gameRoot) throws Exception {
        for (String expansion : new String[]{"Expansion", "Expansion2"}) {
            Path dlcBase = gameRoot.resolve("Assets/DLC/" + expansion + "/DLC");
            if (!Files.exists(dlcBase)) {
                continue;
            }
            try (var stream = Files.list(dlcBase)) {
                for (Path dlcDir : stream.toList()) {
                    Path xmlDir = dlcDir.resolve("Gameplay/XML");
                    if (!Files.exists(xmlDir)) {
                        continue;
                    }
                    try (var xmlFiles = Files.list(xmlDir)) {
                        for (Path xmlFile : xmlFiles.toList()) {
                            if (xmlFile.getFileName().toString().startsWith("CIV5Traits")) {
                                loadTraits(data, xmlFile);
                            }
                        }
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
                data.getLeaderTraits().computeIfAbsent(leaderType, k -> new ArrayList<>()).add(traitType);
            }
        }
    }

    private void loadDlcLeaderTraits(GameData data, Path gameRoot) throws Exception {
        for (String expansion : new String[]{"Expansion", "Expansion2"}) {
            Path leadersDir = gameRoot.resolve("Assets/DLC/" + expansion + "/Gameplay/XML/Leaders");
            loadLeaderTraitsIfExists(data, leadersDir);

            Path dlcBase = gameRoot.resolve("Assets/DLC/" + expansion + "/DLC");
            if (!Files.exists(dlcBase)) {
                continue;
            }
            try (var stream = Files.list(dlcBase)) {
                for (Path dlcDir : stream.toList()) {
                    Path xmlDir = dlcDir.resolve("Gameplay/XML");
                    if (!Files.exists(xmlDir)) {
                        continue;
                    }
                    try (var xmlFiles = Files.list(xmlDir)) {
                        for (Path xmlFile : xmlFiles.toList()) {
                            if (xmlFile.getFileName().toString().startsWith("CIV5Leader_")) {
                                loadLeaderTraitsFromFile(data, xmlFile);
                            }
                        }
                    }
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

        // Civ type list
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

        // Leader assignments
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

        // Unique unit overrides
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

        // Unique building overrides
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

    private void loadDlcCivs(GameData data, Path gameRoot) throws Exception {
        for (String expansion : new String[]{"Expansion", "Expansion2"}) {
            Path dlcBase = gameRoot.resolve("Assets/DLC/" + expansion + "/DLC");
            if (!Files.exists(dlcBase)) {
                continue;
            }
            try (var stream = Files.list(dlcBase)) {
                for (Path dlcDir : stream.toList()) {
                    Path xmlDir = dlcDir.resolve("Gameplay/XML");
                    if (!Files.exists(xmlDir)) {
                        continue;
                    }
                    try (var xmlFiles = Files.list(xmlDir)) {
                        for (Path xmlFile : xmlFiles.toList()) {
                            String name = xmlFile.getFileName().toString();
                            if (name.startsWith("CIV5Civilization") || name.startsWith("Civ5Civilization")) {
                                loadCivs(data, xmlFile);
                            }
                        }
                    }
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
