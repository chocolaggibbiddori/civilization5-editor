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

    public abstract class AbstractTrait implements Trait {

        @Getter
        private final String type;
        @Getter
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

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = row
                    .selectSingleNode("Description")
                    .getText();

            String description = gameData
                    .getDocument(getTraitTextFilePath())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = TextUtils.stripInnerTags(description);
            return this.description;
        }

        @Override
        public List<Variable> getVariableList() {
            return variableList;
        }

        protected abstract void addVariables(List<Variable> variableList);
    }

    public abstract class AbstractUniqueUnit implements UniqueUnit {

        @Getter
        private final String type;
        @Getter
        private final Node row;
        private final List<Variable> variableList;
        private String description;

        @Getter
        private final Integer combat;
        @Getter
        private final Integer rangedCombat;
        @Getter
        private final Integer cost;
        @Getter
        private final Integer moves;
        @Getter
        private final Integer range;

        public AbstractUniqueUnit() {
            type = gameData
                    .getDocument(getCivilizationFilePath())
                    .selectSingleNode("/GameData/Civilization_UnitClassOverrides/Row[CivilizationType='%s' and contains(UnitType, '%s')]/UnitType"
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

            this.combat = combat == null ? null : Integer.parseInt(combat);
            this.rangedCombat = rangedCombat == null ? null : Integer.parseInt(rangedCombat);
            this.cost = cost == null ? null : Integer.parseInt(cost);
            this.moves = moves == null ? null : Integer.parseInt(moves);
            this.range = range == null ? null : Integer.parseInt(range);

            variableList = new ArrayList<>();
            addDefaultVariables();
            addVariables(variableList);
        }

        protected abstract Path getUnitFilePath();

        protected abstract Path getUnitTextFilePath();

        protected abstract Path getCivilopediaTextFilePath();

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

        @Override
        public List<Variable> getVariableList() {
            return variableList;
        }

        private void addDefaultVariables() {
            if (combat != null) variableList.add(new NodeVariable(row.selectSingleNode("Combat")));
            if (rangedCombat != null) variableList.add(new NodeVariable(row.selectSingleNode("RangedCombat")));
            if (cost != null) variableList.add(new NodeVariable(row.selectSingleNode("Cost")));
            if (moves != null) variableList.add(new NodeVariable(row.selectSingleNode("Moves")));
            if (range != null) variableList.add(new NodeVariable(row.selectSingleNode("Range")));
        }

        protected void addVariables(List<Variable> variableList) {
        }
    }

    public abstract class AbstractUniqueBuilding implements UniqueBuilding {

        @Getter
        private final String type;
        @Getter
        private final Node row;
        private final List<Variable> variableList;
        private String description;

        @Getter
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

        @Override
        public String getDescription() {
            if (description != null) {
                return description;
            }

            String descriptionKey = row
                    .selectSingleNode("Description")
                    .getText();

            String description = gameData
                    .getDocument(getBuildingTextFilePath())
                    .selectSingleNode("/GameData/Language_KO_KR/Row[@Tag='%s']/Text".formatted(descriptionKey))
                    .getText();

            this.description = description;
            return description;
        }

        @Override
        public List<Variable> getVariableList() {
            return variableList;
        }

        private void addDefaultVariables() {
            if (cost != null) variableList.add(new NodeVariable(row.selectSingleNode("Cost")));
        }

        protected abstract void addVariables(List<Variable> variableList);

        protected Document getDocument() {
            return gameData.getDocument(getBuildingFilePath());
        }
    }
}
