package br.com.poiwifidiscover.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import br.com.poiwifidiscover.model.WifiData;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.PART;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Classifiers {

	public static LinkedHashMap<Integer, Double> buildClassifier(WifiData wifiData, ClassificationAlgorithm option) {

		TreeMap<Integer, Double> probabilityMap = new TreeMap<>();
		try {
			DataSource ds = new DataSource(Constants.ARFF_FILE);
			Instances ins = ds.getDataSet();

			ins.setClassIndex(ins.numAttributes() - 1);
			Instance newInstance = newInstance(ins, wifiData);

			double probability[] = null;
			Classifier classifier = null;

			switch (option) {
			case NAIVE_BAYES:
				classifier = new NaiveBayes();
				break;
			case KNN:
				classifier = new IBk();
				break;
			case J48:
				classifier = new J48();
				break;
			case K_STAR:
				classifier = new KStar();
				break;
			case ONE_R:
				classifier = new OneR();
				break;
			}

			classifier.buildClassifier(ins);
			probability = classifier.distributionForInstance(newInstance);

			String labels = ins.classAttribute().toString().split(" ")[2];
			String[] labelArr = labels.substring(1, labels.length() - 1).split(",");

			for (int i = 0; i < probability.length; i++) {
				probabilityMap.put(Integer.parseInt(labelArr[i]), probability[i]);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (LinkedHashMap<Integer, Double>) sortByValue(probabilityMap);

	}

	public static void wsml2arff(ArrayList<WifiData> wifiDataList) {

		TreeSet<String> bssidSet = new TreeSet<>();
		TreeSet<Integer> idThingSet = new TreeSet<>();

		for (WifiData wifiData : wifiDataList) {
			bssidSet.addAll(wifiData.getWifiMap().keySet());
			idThingSet.add(wifiData.getIDThing());
		}

		// inicializa todos os valores do conjunto de exemplo com MIN_RSSI
		int[][] examples = new int[wifiDataList.size()][bssidSet.size() + 1];
		for (int[] is : examples) {
			Arrays.fill(is, Constants.MIN_RSSI);
		}

		ArrayList<String> attrsBSSIDList = new ArrayList<>(bssidSet);

		int x = 0;
		// poi

		for (WifiData wifiData : wifiDataList) {
			// dados wifi
			for (Map.Entry<String, Integer> entry2 : wifiData.getWifiMap().entrySet()) {
				examples[x][attrsBSSIDList.indexOf(entry2.getKey())] = entry2.getValue();
			}
			examples[x][bssidSet.size()] = wifiData.getIDThing();
			x++;
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("@relation wifiPOI\r\n\r\n");

		for (String BSSID : bssidSet) {
			stringBuilder.append("@attribute \"");
			stringBuilder.append(BSSID);
			stringBuilder.append("\" numeric\r\n");
		}

		stringBuilder.append("@attribute poi {");

		for (int id : idThingSet) {
			stringBuilder.append(id);
			stringBuilder.append(",");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		stringBuilder.append("}");

		stringBuilder.append("\r\n\r\n@data\r\n");

		for (int[] example : examples) {
			for (int i : example) {
				stringBuilder.append(i);
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			stringBuilder.append("\r\n");
		}

		String arff = stringBuilder.toString();
		FileManager.writeString(Constants.ARFF_FILE, arff, false);

	}

	public static void main(String[] args) {
	}

	private static Instance newInstance(Instances ins, WifiData wifiData) {

		ArrayList<String> attrList = new ArrayList<>();
		for (Attribute attr : Collections.list(ins.enumerateAttributes())) {
			attrList.add(attr.toString().split(" ")[1]);
		}

		Instance newInstance = new DenseInstance(ins.numAttributes());
		newInstance.setDataset(ins);

		for (int i = 0; i < attrList.size(); i++) {
			newInstance.setValue(i, -300);
		}

		for (Entry<String, Integer> entry : wifiData.getWifiMap().entrySet()) {
			if (attrList.contains(entry.getKey()))
				newInstance.setValue(attrList.indexOf(entry.getKey()), entry.getValue());
		}

		return newInstance;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {

		return map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

}