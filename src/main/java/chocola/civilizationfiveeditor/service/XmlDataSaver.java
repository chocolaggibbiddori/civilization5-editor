package chocola.civilizationfiveeditor.service;

import chocola.civilizationfiveeditor.model.BuildingData;
import chocola.civilizationfiveeditor.model.ImprovementData;
import chocola.civilizationfiveeditor.model.TraitData;
import chocola.civilizationfiveeditor.model.UnitData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlDataSaver {

    /**
     * Save a single unit's editable fields back to its source XML file.
     */
    public void saveUnit(UnitData unit) throws Exception {
        if (unit.getSourceFile() == null) {
            return;
        }
        Path path = unit.getSourceFile();
        Document doc = parse(path);

        Element row = findUnitRow(doc, unit.getType());
        if (row == null) {
            return;
        }

        setOrCreate(doc, row, "Combat", String.valueOf(unit.getCombat()));
        setOrCreate(doc, row, "RangedCombat", String.valueOf(unit.getRangedCombat()));
        setOrCreate(doc, row, "Cost", String.valueOf(unit.getCost()));
        setOrCreate(doc, row, "Moves", String.valueOf(unit.getMoves()));
        setOrCreate(doc, row, "Range", String.valueOf(unit.getRange()));

        save(doc, path);
    }

    /**
     * Save a single building's editable fields back to its source XML file.
     */
    public void saveBuilding(BuildingData building) throws Exception {
        if (building.getSourceFile() == null) {
            return;
        }
        Path path = building.getSourceFile();
        Document doc = parse(path);

        Element row = findBuildingRow(doc, building.getType());
        if (row == null) {
            return;
        }

        setOrCreate(doc, row, "Cost", String.valueOf(building.getCost()));
        setOrCreate(doc, row, "Culture", String.valueOf(building.getCulture()));
        setOrCreate(doc, row, "Gold", String.valueOf(building.getGold()));
        setOrCreate(doc, row, "Happiness", String.valueOf(building.getHappiness()));
        setOrCreate(doc, row, "Defense", String.valueOf(building.getDefense()));
        setOrCreate(doc, row, "GoldMaintenance", String.valueOf(building.getGoldMaintenance()));

        save(doc, path);
    }

    /**
     * Save a single improvement's yield values back to its source XML file.
     * Yields are stored in the Improvement_Yields cross-reference table.
     */
    public void saveImprovement(ImprovementData improvement) throws Exception {
        if (improvement.getSourceFile() == null) {
            return;
        }
        Path path = improvement.getSourceFile();
        Document doc = parse(path);

        setImprovementYield(doc, improvement.getType(), "YIELD_FOOD",       improvement.getFood());
        setImprovementYield(doc, improvement.getType(), "YIELD_PRODUCTION", improvement.getProduction());
        setImprovementYield(doc, improvement.getType(), "YIELD_GOLD",       improvement.getGold());
        setImprovementYield(doc, improvement.getType(), "YIELD_SCIENCE",    improvement.getScience());
        setImprovementYield(doc, improvement.getType(), "YIELD_CULTURE",    improvement.getCulture());
        setImprovementYield(doc, improvement.getType(), "YIELD_FAITH",      improvement.getFaith());

        save(doc, path);
    }

    private void setImprovementYield(Document doc, String improvementType, String yieldType, int value) {
        NodeList sections = doc.getElementsByTagName("Improvement_Yields");
        for (int s = 0; s < sections.getLength(); s++) {
            Element section = (Element) sections.item(s);
            NodeList rows = section.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                NodeList impNodes = row.getElementsByTagName("ImprovementType");
                NodeList yieldNodes = row.getElementsByTagName("YieldType");
                if (impNodes.getLength() > 0 && yieldNodes.getLength() > 0
                        && improvementType.equals(impNodes.item(0).getTextContent().trim())
                        && yieldType.equals(yieldNodes.item(0).getTextContent().trim())) {
                    NodeList yieldVals = row.getElementsByTagName("Yield");
                    if (yieldVals.getLength() > 0) {
                        yieldVals.item(0).setTextContent(String.valueOf(value));
                    }
                    return;
                }
            }
        }
        // Row doesn't exist yet — create it if value is non-zero
        if (value == 0) {
            return;
        }
        for (int s = 0; s < sections.getLength(); s++) {
            Element section = (Element) sections.item(s);
            Element row = doc.createElement("Row");
            appendTextElement(doc, row, "ImprovementType", improvementType);
            appendTextElement(doc, row, "YieldType", yieldType);
            appendTextElement(doc, row, "Yield", String.valueOf(value));
            section.appendChild(row);
            return;
        }
        // No Improvement_Yields section exists — create one
        Element section = doc.createElement("Improvement_Yields");
        Element row = doc.createElement("Row");
        appendTextElement(doc, row, "ImprovementType", improvementType);
        appendTextElement(doc, row, "YieldType", yieldType);
        appendTextElement(doc, row, "Yield", String.valueOf(value));
        section.appendChild(row);
        doc.getDocumentElement().appendChild(section);
    }

    private void appendTextElement(Document doc, Element parent, String tag, String value) {
        Element el = doc.createElement(tag);
        el.setTextContent(value);
        parent.appendChild(el);
    }

    /**
     * Save a single trait's values back to its source XML file.
     */
    public void saveTrait(TraitData trait) throws Exception {
        if (trait.getSourceFile() == null) {
            return;
        }
        Path path = trait.getSourceFile();
        Document doc = parse(path);

        Element row = findTraitRow(doc, trait.getType());
        if (row == null) {
            return;
        }

        for (Map.Entry<String, String> entry : trait.getValues().entrySet()) {
            setOrCreate(doc, row, entry.getKey(), entry.getValue());
        }

        save(doc, path);
    }

    // ── Backup / Restore ──────────────────────────────────────────────────

    public boolean hasBackup(Path sourceFile) {
        return Files.exists(sourceFile.resolveSibling(sourceFile.getFileName() + ".bak"));
    }

    public void restoreBackup(Path sourceFile) throws Exception {
        Path backup = sourceFile.resolveSibling(sourceFile.getFileName() + ".bak");
        if (Files.exists(backup)) {
            Files.copy(backup, sourceFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // ── DOM helpers ────────────────────────────────────────────────────────

    private Element findUnitRow(Document doc, String type) {
        return findRowByType(doc, "Units", type);
    }

    private Element findBuildingRow(Document doc, String type) {
        return findRowByType(doc, "Buildings", type);
    }

    private Element findTraitRow(Document doc, String type) {
        return findRowByType(doc, "Traits", type);
    }

    private Element findRowByType(Document doc, String sectionTag, String type) {
        NodeList sections = doc.getElementsByTagName(sectionTag);
        for (int s = 0; s < sections.getLength(); s++) {
            Element section = (Element) sections.item(s);
            NodeList rows = section.getElementsByTagName("Row");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                NodeList typeNodes = row.getElementsByTagName("Type");
                if (typeNodes.getLength() > 0 &&
                        type.equals(typeNodes.item(0).getTextContent().trim())) {
                    return row;
                }
            }
        }
        return null;
    }

    /**
     * Set existing tag's text, or create the tag if absent.
     */
    private void setOrCreate(Document doc, Element parent, String tag, String value) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() > 0) {
            list.item(0).setTextContent(value);
        } else if (!"0".equals(value)) {
            Element el = doc.createElement(tag);
            el.setTextContent(value);
            parent.appendChild(el);
        }
    }

    private Document parse(Path path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(path.toFile());
    }

    private void save(Document doc, Path path) throws Exception {
        // Backup original file once (only if .bak doesn't exist yet)
        Path backup = path.resolveSibling(path.getFileName() + ".bak");
        if (!Files.exists(backup)) {
            Files.copy(path, backup);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(path.toFile());
        transformer.transform(source, result);
    }
}
