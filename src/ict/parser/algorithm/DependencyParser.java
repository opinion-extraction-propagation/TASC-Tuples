package ict.parser.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.sun.tools.javac.util.Pair;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class DependencyParser {

	public ArrayList<ArrayList<String>> getDependencyByLine(
			LexicalizedParser lp, String filename, String authorfilename) {
		ArrayList<ArrayList<String>> retArrayList = new ArrayList<ArrayList<String>>();

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			BufferedReader authorReader = new BufferedReader(new FileReader(
					authorfilename));

			String line = "";
			String author = "";
			while ((line = br.readLine()) != null) {
				author = authorReader.readLine();
				Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory()
						.getTokenizer(new StringReader(line));
				List<? extends HasWord> sentence = toke.tokenize();
				Tree parse = lp.apply(sentence);
				List<Tree> childTrees = parse.getChildrenAsList();
				Stack<Tree> treeStack = new Stack<Tree>();
				treeStack.addAll(childTrees);

				Label prevLabel = null;
				Label curLabel = parse.label();
				HashMap<Integer, Pair<Label, Label>> wordTagMap = new HashMap<Integer, Pair<Label, Label>>();
				int depth = 1;
				while (!treeStack.isEmpty()) {
					Tree curTree = treeStack.pop();
					prevLabel = curLabel;
					curLabel = curTree.label();
					childTrees = curTree.getChildrenAsList();
					if (0 == childTrees.size()) {
						// word node
						wordTagMap.put(depth, new Pair<Label, Label>(curLabel,
								prevLabel));
						depth++;
					} else {
						treeStack.addAll(childTrees);
					}
				}

				final int numWord = wordTagMap.size();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
				List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
				for (TypedDependency typedDep : tdl) {
					int govIndex = typedDep.gov().index();
					int depIndex = typedDep.dep().index();
					if (wordTagMap.containsKey(govIndex)
							&& wordTagMap.containsKey(depIndex)) {
						ArrayList<String> arrList = new ArrayList<String>();
						arrList.add(typedDep.dep().nodeString());
						arrList.add(wordTagMap.get(numWord
								- typedDep.dep().index() + 1).snd.toString());
						arrList.add(typedDep.reln().toString());
						arrList.add(typedDep.gov().nodeString());
						arrList.add(wordTagMap.get(numWord
								- typedDep.gov().index() + 1).snd.toString());
						arrList.add(author);
						arrList.add(line);

						retArrayList.add(arrList);
					}

				}
			}
			br.close();
			authorReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retArrayList;
	}

	/**
	 * file => tokens => parse trees
	 * 
	 * @param lp
	 * @param filename
	 *            tuples
	 */
	public ArrayList<ArrayList<String>> getDependencyBySentence(
			LexicalizedParser lp, String filename) {
		ArrayList<ArrayList<String>> retArrayList = new ArrayList<ArrayList<String>>();

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

		for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
			Tree parse = lp.apply(sentence);

			List<Tree> childTrees = parse.getChildrenAsList();
			Stack<Tree> treeStack = new Stack<Tree>();
			treeStack.addAll(childTrees);

			Label prevLabel = null;
			Label curLabel = parse.label();
			HashMap<Integer, Pair<Label, Label>> wordTagMap = new HashMap<Integer, Pair<Label, Label>>();
			int depth = 1;
			while (!treeStack.isEmpty()) {
				Tree curTree = treeStack.pop();
				prevLabel = curLabel;
				curLabel = curTree.label();
				childTrees = curTree.getChildrenAsList();
				if (0 == childTrees.size()) {
					// word node
					wordTagMap.put(depth, new Pair<Label, Label>(curLabel,
							prevLabel));
					depth++;
				} else {
					treeStack.addAll(childTrees);
				}
			}

			final int numWord = wordTagMap.size();
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
			for (TypedDependency typedDep : tdl) {
				int govIndex = typedDep.gov().index();
				int depIndex = typedDep.dep().index();
				if (wordTagMap.containsKey(govIndex)
						&& wordTagMap.containsKey(depIndex)) {
					ArrayList<String> arrList = new ArrayList<String>();
					arrList.add(typedDep.dep().nodeString());
					arrList.add(wordTagMap.get(numWord - typedDep.dep().index()
							+ 1).snd.toString());
					arrList.add(typedDep.reln().toString());
					arrList.add(typedDep.gov().nodeString());
					arrList.add(wordTagMap.get(numWord - typedDep.gov().index()
							+ 1).snd.toString());

					retArrayList.add(arrList);

				}

			}
		}
		return retArrayList;
	}
}
