package ict.parser.application;

import ict.parser.algorithm.OpinionWordTargetExtraction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class ExtractOpinionTarget {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LexicalizedParser lp = LexicalizedParser
				.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");

		if (args.length > 0) {
			// input
			String publicwordFileNameString = args[0];
			String dependencyFileNameString = args[1];
			// output
			String topicwordFileNameString = args[2];
			String targetwordFileNameString = args[3];

			OpinionWordTargetExtraction opinionWordTargetExtraction = new OpinionWordTargetExtraction();

			ArrayList<ArrayList<String>> dependencyList = opinionWordTargetExtraction
					.loadDependencyRelation(dependencyFileNameString);

			HashSet<String> publicWordSet = opinionWordTargetExtraction
					.loadPublicSentimentWord(publicwordFileNameString);

			opinionWordTargetExtraction.ExtractionOpinionWord(publicWordSet,
					dependencyList);

			try {

				BufferedWriter bw = new BufferedWriter(new FileWriter(
						topicwordFileNameString));
				System.out.println("topic word set ");
				for (String topicword : opinionWordTargetExtraction.topicwordSet) {
					bw.write(topicword + "\n");
				}
				bw.flush();
				bw.close();
				// System.out.println("");
				bw = new BufferedWriter(
						new FileWriter(targetwordFileNameString));
				System.out.println("target word set ");
				for (String targetword : opinionWordTargetExtraction.targetSet) {
					bw.write(targetword + "\n");
				}
				System.out.println("");
				bw.flush();
				bw.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println("opinionExpanded word set ");
			// for (String opinionExpandedword : opinionExpanded) {
			// System.out.println(opinionExpandedword);
			// }
			// System.out.println("");

		}

	}
}
