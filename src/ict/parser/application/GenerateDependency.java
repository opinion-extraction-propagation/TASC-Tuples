package ict.parser.application;

import ict.parser.algorithm.DependencyParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class GenerateDependency {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LexicalizedParser lp = LexicalizedParser
				.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		if (args.length > 0) {
			String contentfilename = args[0];
			String authorfilename = args[1];
			String dependencyfilename = args[2];
			DependencyParser dependencyParser = new DependencyParser();
			ArrayList<ArrayList<String>> ret = dependencyParser
					.getDependencyByLine(lp, contentfilename, authorfilename);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						dependencyfilename));
				for (ArrayList<String> arr : ret) {

					bw.write(arr.get(0) + "\t" + arr.get(1) + "\t" + arr.get(2)
							+ "\t" + arr.get(3) + "\t" + arr.get(4) + "\t"
							+ arr.get(5) + "\t" + arr.get(6) + "\n");
				}
				bw.flush();
				bw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out
					.println("java -jar GenerateDependency.jar contentfilename authorfilename dependencyfilename");
		}
	}
}
