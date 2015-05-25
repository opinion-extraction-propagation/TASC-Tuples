package ict.parser.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GenerateTripleTuples {
	public static ArrayList<ArrayList<String>> generateTuples(
			String dependencyFileName, String targetwordFileName,
			String topicwordFileName) {
		ArrayList<ArrayList<String>> retTupleArrayList = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader targetwordReader = new BufferedReader(
					new FileReader(targetwordFileName));
			HashSet<String> targewordSet = new HashSet<String>();
			String targetword = "";
			while ((targetword = targetwordReader.readLine()) != null) {
				targewordSet.add(targetword);
			}
			targetwordReader.close();

			BufferedReader topicwordReader = new BufferedReader(new FileReader(
					topicwordFileName));
			String topicwordWeight = "";
			HashMap<String, Double> topicwordWeightMap = new HashMap<String, Double>();
			while ((topicwordWeight = topicwordReader.readLine()) != null) {
				String[] arr = topicwordWeight.split("\t");
				String topicword = arr[0];
				Double weight = Double.parseDouble(arr[1]);
				topicwordWeightMap.put(topicword, weight);
			}
			topicwordReader.close();

			BufferedReader dependencyReader = new BufferedReader(
					new FileReader(dependencyFileName));
			String dependency = "";
			while ((dependency = dependencyReader.readLine()) != null) {
				String[] arr = dependency.split("\t");
				String dependentWordString = arr[0].toLowerCase();
				String governWordString = arr[3].toLowerCase();
				String authorString = arr[5];
				String contentString = arr[6];
				if (topicwordWeightMap.containsKey(dependentWordString)
						&& targewordSet.contains(governWordString)) {
					ArrayList<String> arrlist = new ArrayList<String>();
					arrlist.add(authorString);
					arrlist.add(governWordString);
					arrlist.add(dependentWordString);
					arrlist.add(Double.toString(topicwordWeightMap
							.get(dependentWordString)));
					arrlist.add(contentString);
					retTupleArrayList.add(arrlist);
				} else if (topicwordWeightMap.containsKey(governWordString)
						&& targewordSet.contains(dependentWordString)) {
					ArrayList<String> arrlist = new ArrayList<String>();
					arrlist.add(authorString);
					arrlist.add(dependentWordString);
					arrlist.add(governWordString);
					arrlist.add(Double.toString(topicwordWeightMap
							.get(governWordString)));
					arrlist.add(contentString);
					retTupleArrayList.add(arrlist);
				}

			}
			dependencyReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retTupleArrayList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length > 0) {
			String dependencyFileName = args[0];
			String targetwordFileName = args[1];
			// String positivewordWeightFileName = args[2];
			// String neutralwordWeightFileName = args[3];
			// String negativewordWeightFileName = args[4];
			String sentimentwordWeightFileName = args[2];

			// String positivetuplesString = args[5];
			// String neutraltupleString = args[6];
			// String negativetupleString = args[7];
			String tuplesFileName = args[3];

			ArrayList<ArrayList<String>> positiveArrayList = generateTuples(
					dependencyFileName, targetwordFileName,
					sentimentwordWeightFileName);
			try {
				BufferedWriter resultWriter = new BufferedWriter(
						new FileWriter(tuplesFileName));
				for (ArrayList<String> arrList : positiveArrayList) {
					resultWriter.write(arrList.get(0) + "\t" + arrList.get(1)
							+ "\t" + arrList.get(2) + "\t" + arrList.get(3)
							+ "\t" + arrList.get(4) + "\n");
				}
				resultWriter.flush();
				resultWriter.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
