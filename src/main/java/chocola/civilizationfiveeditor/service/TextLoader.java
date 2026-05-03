package chocola.civilizationfiveeditor.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class TextLoader {

    private static final Pattern COLOR_TAG = Pattern.compile("\\[[^]]*]");

    public void load(Path gameRoot, Map<String, String> out) throws Exception {
        // Base game
        loadDir(gameRoot.resolve("Assets/Gameplay/XML/NewText/KO_KR"), out);

        // Expansions
        loadDir(gameRoot.resolve("Assets/DLC/Expansion/Gameplay/XML/Text/KO_KR"), out);
        loadDir(gameRoot.resolve("Assets/DLC/Expansion2/Gameplay/XML/Text/KO_KR"), out);

        // Individual DLCs (DLC_01 … DLC_07, DLC_Deluxe, etc.)
        for (String expansion : new String[]{"", "Expansion/", "Expansion2/"}) {
            Path base = gameRoot.resolve("Assets/DLC/" + expansion + (expansion.isEmpty() ? "" : "DLC/"));
            if (!expansion.isEmpty()) {
                base = gameRoot.resolve("Assets/DLC/" + expansion + "DLC");
            } else {
                base = gameRoot.resolve("Assets/DLC");
            }
            if (!Files.exists(base)) {
                continue;
            }
            try (var stream = Files.list(base)) {
                for (Path dlcDir : stream.toList()) {
                    if (!Files.isDirectory(dlcDir)) {
                        continue;
                    }
                    String name = dlcDir.getFileName().toString();
                    if (!name.startsWith("DLC_")) {
                        continue;
                    }
                    Path textDir = dlcDir.resolve("Gameplay/XML/Text/KO_KR");
                    loadDir(textDir, out);
                }
            }
        }
    }

    private void loadDir(Path dir, Map<String, String> out) throws Exception {
        if (!Files.exists(dir)) {
            return;
        }
        try (var stream = Files.list(dir)) {
            for (Path file : stream.toList()) {
                if (file.getFileName().toString().endsWith(".xml")) {
                    loadFile(file, out);
                }
            }
        }
    }

    private void loadFile(Path path, Map<String, String> out) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException e) {
                }

                public void error(SAXParseException e) {
                }

                public void fatalError(SAXParseException e) throws SAXParseException {
                    throw e;
                }
            });
            Document doc = builder.parse(path.toFile());

            NodeList sections = doc.getElementsByTagName("Language_KO_KR");
            for (int s = 0; s < sections.getLength(); s++) {
                NodeList rows = ((Element) sections.item(s)).getElementsByTagName("Row");
                for (int i = 0; i < rows.getLength(); i++) {
                    Element row = (Element) rows.item(i);
                    String tag = row.getAttribute("Tag");
                    if (tag.isBlank()) {
                        continue;
                    }
                    NodeList textNodes = row.getElementsByTagName("Text");
                    if (textNodes.getLength() == 0) {
                        continue;
                    }
                    String raw = textNodes.item(0).getTextContent().trim();
                    out.put(tag, strip(raw));
                }
            }
        } catch (Exception ignored) {
            // Skip malformed files
        }
    }

    public static String strip(String text) {
        if (text == null) {
            return "";
        }
        return COLOR_TAG.matcher(text).replaceAll("").trim();
    }
}
