package chocola.civilizationfiveeditor.v2.model.civilization.impl;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import chocola.civilizationfiveeditor.v2.model.civilization.BasicCivilization;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Siam extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Ramkhamhaeng";
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new SiamTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new WarElephant()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Wat()};
    }

    private class SiamTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement().element("CityStateBonusModifier");

            variableList.add(new NodeVariable(node));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_JON_INHERITED_TEXT_FILE_PATH;
        }
    }

    private class WarElephant extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_UNIT_FILE_PATH;
        }
    }

    private class Wat extends BasicUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldModifiers/Row[BuildingType='%s']/Yield".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Building_FeatureYieldChanges/Row[BuildingType='%s']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Culture)"));
            variableList.add(new NodeVariable(node2, "Yield(Science, Modifiers)"));
            variableList.add(new NodeVariable(node3, "Yield(Science, Jungle)"));
        }

        @Override
        protected Path getBuildingFilePath() {
            return EXPANSION2_BASIC_BUILDING_FILE_PATH;
        }

        @Override
        protected Path getBuildingTextFilePath() {
            return OBJECT_TEXT_FILE_PATH;
        }
    }
}
