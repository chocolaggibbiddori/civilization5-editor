package chocola.civilizationfiveeditor.v2.model.civilization.bnw;

import chocola.civilizationfiveeditor.v2.model.NodeVariable;
import chocola.civilizationfiveeditor.v2.model.UniqueBuilding;
import chocola.civilizationfiveeditor.v2.model.UniqueUnit;
import chocola.civilizationfiveeditor.v2.model.Variable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class Zulu extends BraveNewWorldCivilization {

    @Override
    protected String getLeaderEnglishName() {
        return "Shaka";
    }

    @Override
    protected AbstractTrait createTrait() {
        return new ZuluTrait();
    }

    @Override
    protected UniqueUnit[] createUniqueUnits() {
        return new UniqueUnit[]{new Impi()};
    }

    @Override
    protected UniqueBuilding[] createUniqueBuildings() {
        return new UniqueBuilding[]{new Ikanda()};
    }

    private class ZuluTrait extends BraveNewWorldTrait {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Trait_MaintenanceModifierUnitCombats/Row[TraitType='%s']/MaintenanceModifier".formatted(type));
            Element node2 = getElement("LevelExperienceModifier");

            variableList.add(new NodeVariable(node1));
            variableList.add(new NodeVariable(node2));
        }
    }

    private class Impi extends BraveNewWorldUniqueUnit {
    }

    private class Ikanda extends BraveNewWorldUniqueBuilding {

        @Override
        protected void addVariables(List<Variable> variableList) {
            Document document = getDocument();
            String type = getType();

            Node node1 = document.selectSingleNode("/GameData/Building_DomainFreeExperiences/Row[BuildingType='%s' and DomainType='DOMAIN_LAND']/Experience".formatted(type));
            Node node2 = document.selectSingleNode("/GameData/Building_DomainFreeExperiences/Row[BuildingType='%s' and DomainType='DOMAIN_SEA']/Experience".formatted(type));
            Node node3 = document.selectSingleNode("/GameData/Building_DomainFreeExperiences/Row[BuildingType='%s' and DomainType='DOMAIN_AIR']/Experience".formatted(type));

            variableList.add(new NodeVariable(node1, "Experience(Land)"));
            variableList.add(new NodeVariable(node2, "Experience(Sea)"));
            variableList.add(new NodeVariable(node3, "Experience(Air)"));
        }
    }
}
