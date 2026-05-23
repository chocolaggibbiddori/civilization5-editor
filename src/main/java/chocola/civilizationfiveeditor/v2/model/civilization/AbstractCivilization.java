package chocola.civilizationfiveeditor.v2.model.civilization;

import static chocola.civilizationfiveeditor.v2.service.GameDataLoader.gameData;

import chocola.civilizationfiveeditor.v2.model.*;
import chocola.civilizationfiveeditor.v2.util.TextUtils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public abstract class AbstractCivilization implements Civilization {

    protected final Path leaderPath = getDefaultLeaderPath().resolve("CIV5Leader_%s.xml".formatted(getLeaderEnglishName()));

    @Getter
    private final String type;

    private String name;
    private String leaderName;
    private AbstractTrait trait;
    private UniqueUnit[] uniqueUnits;
    private UniqueBuilding[] uniqueBuildings;
    private UniqueImprovement[] uniqueImprovements;

    public AbstractCivilization() {
        type = "CIVILIZATION_%s".formatted(getClass().getSimpleName().toUpperCase());
    }

    protected abstract Path getDefaultLeaderPath();

    protected abstract Path getCivilizationTextFilePath();

    protected abstract Path getCivilizationFilePath();

    protected abstract Path getLeaderTextFilePath();

    @Override
    public List<Path> requiredPathList() {
        return List.of(leaderPath);
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }

        String shortDescriptionTag = gameData
                .getDocument(getCivilizationFilePath())
                .selectSingleNode("/GameData/Civilizations/Row[Type='%s']/ShortDescription".formatted(type))
                .getText();
        String name = gameData
                .getDocument(getCivilizationTextFilePath())
                .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(shortDescriptionTag))
                .getText();

        this.name = TextUtils.stripInnerTags(name);
        return this.name;
    }

    @Override
    public String getLeaderName() {
        if (leaderName != null) {
            return leaderName;
        }

        String descriptionKey = gameData
                .getDocument(leaderPath)
                .selectSingleNode("/GameData/Leaders/Row/Description")
                .getText();

        return leaderName = gameData
                .getDocument(getLeaderTextFilePath())
                .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                .getText();
    }

    protected abstract String getLeaderEnglishName();

    @Override
    public final Trait getTrait() {
        return trait != null ? trait : (trait = createTrait());
    }

    protected abstract AbstractTrait createTrait();

    @Override
    public final UniqueUnit[] getUniqueUnits() {
        return uniqueUnits != null ? uniqueUnits : (uniqueUnits = createUniqueUnits());
    }

    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[0];
    }

    @Override
    public final UniqueBuilding[] getUniqueBuildings() {
        return uniqueBuildings != null ? uniqueBuildings : (uniqueBuildings = createUniqueBuildings());
    }

    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[0];
    }

    @Override
    public final UniqueImprovement[] getUniqueImprovements() {
        return uniqueImprovements != null ? uniqueImprovements : (uniqueImprovements = createUniqueImprovements());
    }

    protected UniqueImprovement[] createUniqueImprovements() {
        return new UniqueImprovement[0];
    }

    @Getter
    public abstract class AbstractTrait implements Trait {

        private final String type;
        private final Node row;
        private final List<Variable> variableList = new ArrayList<>();
        private String description;

        public AbstractTrait() {
            type = gameData
                    .getDocument(leaderPath)
                    .selectSingleNode("/GameData/Leader_Traits/Row/TraitType")
                    .getText();
            row = gameData
                    .getDocument(getTraitFilePath())
                    .selectSingleNode("/GameData/Traits/Row[Type='%s']".formatted(type));
            addVariables(variableList);
        }

        protected abstract Path getTraitFilePath();

        protected abstract Path getTraitTextFilePath();

        protected abstract void addVariables(List<Variable> variableList);

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = getElement().elementText("Description");
            String description = gameData
                    .getDocument(getTraitTextFilePath())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = TextUtils.stripInnerTags(description);
            return this.description;
        }

        protected Element getElement() {
            return (Element) row;
        }

        protected Element getElement(String element) {
            return getElement().element(element);
        }

        protected Document getDocument() {
            return row.getDocument();
        }
    }

    @Getter
    public abstract class AbstractUniqueUnit implements UniqueUnit {

        private final String type;
        private final Node row;
        private final List<Variable> variableList;
        private String description;

        private final Integer cost;
        private final Integer moves;
        private final Integer combat;
        private final Integer rangedCombat;
        private final Integer range;

        public AbstractUniqueUnit() {
            type = gameData
                    .getDocument(getCivilizationFilePath())
                    .selectSingleNode("/GameData/Civilization_UnitClassOverrides/Row[CivilizationType='%s' and contains(translate(UnitType, '_', ''), '%s')]/UnitType"
                            .formatted(AbstractCivilization.this.type, getClass().getSimpleName().toUpperCase()))
                    .getText();
            row = gameData
                    .getDocument(getUnitFilePath())
                    .selectSingleNode("/GameData/Units/Row[Type='%s']".formatted(type));

            Element element = (Element) row;
            String combat = element.elementText("Combat");
            String rangedCombat = element.elementText("RangedCombat");
            String cost = element.elementText("Cost");
            String moves = element.elementText("Moves");
            String range = element.elementText("Range");

            this.cost = cost == null ? null : Integer.parseInt(cost);
            this.moves = moves == null ? null : Integer.parseInt(moves);
            this.combat = combat == null ? null : Integer.parseInt(combat);
            this.rangedCombat = rangedCombat == null ? null : Integer.parseInt(rangedCombat);
            this.range = range == null ? null : Integer.parseInt(range);

            variableList = new ArrayList<>();
            addDefaultVariables();
            addVariables(variableList);
        }

        protected abstract Path getUnitFilePath();

        protected abstract Path getUnitTextFilePath();

        protected abstract Path getCivilopediaTextFilePath();

        protected abstract void addVariables(List<Variable> variableList);

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = gameData
                    .getDocument(getUnitFilePath())
                    .selectSingleNode("/GameData/Units/Row[Type='%s']/Description".formatted(type))
                    .getText();

            Node descriptionNode = gameData
                    .getDocument(getUnitTextFilePath())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey));

            if (descriptionNode == null) {
                descriptionNode = gameData
                        .getDocument(getCivilopediaTextFilePath())
                        .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey));
            }

            description = descriptionNode.getText();
            return description;
        }

        private void addDefaultVariables() {
            if (cost != null) variableList.add(new NodeVariable(row.selectSingleNode("Cost")));
            if (moves != null) variableList.add(new NodeVariable(row.selectSingleNode("Moves")));
            if (combat != null) variableList.add(new NodeVariable(row.selectSingleNode("Combat")));
            if (rangedCombat != null) variableList.add(new NodeVariable(row.selectSingleNode("RangedCombat")));
            if (range != null) variableList.add(new NodeVariable(row.selectSingleNode("Range")));
        }

        protected Element getElement() {
            return (Element) row;
        }

        protected Element getElement(String element) {
            return getElement().element(element);
        }

        protected Document getDocument() {
            return row.getDocument();
        }
    }

    @Getter
    public abstract class AbstractUniqueBuilding implements UniqueBuilding {

        private final String type;
        private final Node row;
        private final List<Variable> variableList;
        private String description;

        private final Integer cost;

        public AbstractUniqueBuilding() {
            type = gameData
                    .getDocument(getCivilizationFilePath())
                    .selectSingleNode("/GameData/Civilization_BuildingClassOverrides/Row[CivilizationType='%s']/BuildingType"
                            .formatted(AbstractCivilization.this.type))
                    .getText();
            row = gameData
                    .getDocument(getBuildingFilePath())
                    .selectSingleNode("/GameData/Buildings/Row[Type='%s']".formatted(type));

            Element element = (Element) row;
            String cost = element.elementText("Cost");

            this.cost = cost == null ? null : Integer.parseInt(cost);

            variableList = new ArrayList<>();
            addDefaultVariables();
            addVariables(variableList);
        }

        protected abstract Path getBuildingFilePath();

        protected abstract Path getBuildingTextFilePath();

        protected abstract void addVariables(List<Variable> variableList);

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = getElement().elementText("Description");
            String description = gameData
                    .getDocument(getBuildingTextFilePath())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = description;
            return description;
        }

        private void addDefaultVariables() {
            if (cost != null) variableList.add(new NodeVariable(row.selectSingleNode("Cost")));
        }

        protected Element getElement() {
            return (Element) row;
        }

        protected Element getElement(String element) {
            return getElement().element(element);
        }

        protected Document getDocument() {
            return row.getDocument();
        }
    }

    @Getter
    public abstract class AbstractUniqueImprovement implements UniqueImprovement {

        private final String type;
        private final Node row;
        private final List<Variable> variableList;
        private String description;

        public AbstractUniqueImprovement() {
            row = gameData
                    .getDocument(getImprovementFilePath())
                    .selectSingleNode("/GameData/Improvements/Row[CivilizationType='%s']"
                            .formatted(AbstractCivilization.this.type));

            type = getElement().elementText("Type");

            variableList = new ArrayList<>();
            addVariables(variableList);
        }

        protected abstract Path getImprovementFilePath();

        protected abstract Path getImprovementTextFilePath();

        protected abstract void addVariables(List<Variable> variableList);

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = getElement().elementText("Description");
            String description = gameData
                    .getDocument(getImprovementTextFilePath())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = description;
            return description;
        }

        protected Element getElement() {
            return (Element) row;
        }

        protected Element getElement(String element) {
            return getElement().element(element);
        }

        protected Document getDocument() {
            return row.getDocument();
        }
    }
}
