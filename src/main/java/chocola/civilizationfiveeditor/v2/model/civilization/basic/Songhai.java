package chocola.civilizationfiveeditor.v2.model.civilization.basic;

import static chocola.civilizationfiveeditor.v2.util.PathUtils.*;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.nio.file.Path;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Songhai extends BasicCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Askia";
    }

    @Override
    protected Path getCivilizationFilePath() {
        return EXPANSION2_BASIC_CIVILIZATION_FILE_PATH;
    }

    @Override
    protected Path getDefaultLeaderPath() {
        return EXPANSION_DEFAULT_LEADER_PATH;
    }

    @Override
    protected AbstractTrait createTrait() {
        return new SonghaiTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new MuslimCavalry()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new MudPyramidMosque()};
    }

    private class SonghaiTrait extends BasicTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Element node = getElement("PlunderModifier");

            variableList.add(new NodeVariable(node));
        }

        @Override
        protected Path getTraitFilePath() {
            return EXPANSION2_BASIC_TRAIT_FILE_PATH;
        }

        @Override
        protected Path getTraitTextFilePath() {
            return EXPANSION2_INHERITED_TRAIT_TEXT_FILE_PATH;
        }
    }

    private class MuslimCavalry extends BasicUniqueUnit {

        @Override
        protected Path getUnitFilePath() {
            return EXPANSION2_BASIC_UNIT_FILE_PATH;
        }
    }

    private class MudPyramidMosque extends BasicUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s' and YieldType='YIELD_FAITH']/Yield".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_YieldChanges/Row[BuildingType='%s' and YieldType='YIELD_CULTURE']/Yield".formatted(type));

            variableList.add(new NodeVariable(node1, "Yield(Faith)"));
            variableList.add(new NodeVariable(node2, "Yield(Culture)"));
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
