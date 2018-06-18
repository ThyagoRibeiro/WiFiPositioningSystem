package br.com.poiwifidiscover.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.wsml.ParserException;

import br.com.poiwifidiscover.model.Attribute;
import br.com.poiwifidiscover.model.Thing;
import br.com.poiwifidiscover.model.WifiData;
import br.com.poiwifidiscover.util.ClassificationAlgorithm;
import br.com.poiwifidiscover.util.Classifiers;

public class ThingDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static ArrayList<Thing> createThingList(Set<Map<Variable, Term>> result) {

		ArrayList<Thing> objList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String thingStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("thing"))
					thingStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(thingStr)) {
				objMap.get(thingStr).put(attribute, value);
			} else {
				// System.out.println("false");
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(thingStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			Thing newThing = new Thing();

			newThing.setID(Integer.parseInt(objAttr.get("hasID")));
			newThing.setNome(new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newThing.setDescricao(new Attribute(objAttr.get("hasDescriptionText"), objAttr.get("hasDescriptionAudio"),
					objAttr.get("hasDescriptionVideo")));
			newThing.setMensagem(new Attribute(objAttr.get("hasMessageText"), objAttr.get("hasMessageAudio"),
					objAttr.get("hasMessageVideo")));
			newThing.setAlerta(new Attribute(objAttr.get("hasAlertText"), objAttr.get("hasAlertAudio"),
					objAttr.get("hasAlertVideo")));
			newThing.setDisplay(new Attribute(objAttr.get("hasDisplayText"), objAttr.get("hasDisplayAudio"),
					objAttr.get("hasDisplayVideo")));
			newThing.setLocalizacao(new Attribute(objAttr.get("hasLocalityText"), objAttr.get("hasLocalityAudio"),
					objAttr.get("hasLocalityVideo")));

			objList.add(newThing);
		}

		return objList;
	}

	public static ArrayList<Thing> getSuccessors(Thing thing) {

		ArrayList<Thing> objList = new ArrayList<>();

		String query = "?thing[hasSuccessor hasValue ?successor] and ?thing[hasID hasValue " + thing.getID() + "]";

		for (Map<Variable, Term> map : runQuery(query)) {

			for (Variable key : map.keySet()) {

				if (key.toString().equals("?successor"))
					objList.add(select(map.get(key).toString().replaceAll(".*#", "")));
			}

		}
		return objList;
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {

		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<Thing> select() {
		return createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?concept subConceptOf Thing"));
	}

	public static Thing select(int id) {

		Set<Map<Variable, Term>> set = runQuery(
				"?thing[?attribute hasValue ?value] memberOf Thing and ?thing[hasID hasValue " + id + "]");

		if (set.size() > 0)
			return createThingList(set).get(0);
		else
			return null;
	}

	private static Thing select(String thingIdentifier) {
		return createThingList(
				runQuery("?thing[?attribute hasValue ?value] memberOf Thing and ?thing = " + thingIdentifier)).get(0);
	}

	public void delete() {
		// TODO Auto-generated method stub

	}

	public void insert() {
		// TODO Auto-generated method stub

	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public static Thing discoverThing(WifiData wifiData, String option) {

		HashMap<Integer, Double> probabilityMap = Classifiers.buildClassifier(wifiData,
				ClassificationAlgorithm.valueOf(option));

		java.util.Map.Entry<Integer, Double> entry = Collections.min(probabilityMap.entrySet(),
				new Comparator<java.util.Map.Entry<Integer, Double>>() {
					public int compare(java.util.Map.Entry<Integer, Double> entry1,
							java.util.Map.Entry<Integer, Double> entry2) {
						return entry1.getValue().compareTo(entry2.getValue());
					}
				});

		return ThingDAO.select(entry.getKey());
	}
}
