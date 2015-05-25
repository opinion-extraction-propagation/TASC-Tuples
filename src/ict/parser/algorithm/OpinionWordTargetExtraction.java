package ict.parser.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class OpinionWordTargetExtraction {
	public HashSet<String> MRSet = new HashSet<String>() {
		{
			// modifier
			add("amod");
			add("appos");
			add("advcl");
			add("det");
			add("predet");
			add("preconj");
			add("vmod");
			add("mwe");
			add("mark");
			add("appos");
			add("advmod");
			add("neg");
			add("rcmod");
			add("quantmod");
			add("nn");
			add("npadvmod");
			add("tmod");
			add("num");
			add("number");
			add("prep");
			add("poss");
			add("possessive");
			add("prt");
			// subject
			add("nsubj");
			add("nsubjpass");
			add("csubj");
			add("csubjpass");
			// object
			add("dobj");
			add("iobj");
			add("pobj");
		}
	};
	public HashSet<String> ConjSet = new HashSet<String>() {
		{
			add("conj");
		}
	};

	public HashSet<String> TargetTagSet = new HashSet<String>() {
		{
			add("NN");
			add("NNS");
			add("NNP");
			add("NNPS");
		}
	};
	public HashSet<String> OpinionTagSet = new HashSet<String>() {
		{
			// JJ
			add("JJ");
			add("JJR");
			add("JJS");
			// RB
			add("RB");
			add("RBR");
			add("RBS");
			// VB
			add("VB");
			add("VBD");
			add("VBG");
			add("VBN");
			add("VBP");
			add("VBZ");
		}
	};

	/**
	 * opinion word extraction and target word extraction algorithm output
	 */
	public HashSet<String> topicwordSet = new HashSet<String>();
	public HashSet<String> targetSet = new HashSet<String>();
	public HashSet<String> opinionExpanded = new HashSet<String>();

	/**
	 * 
	 * @param publicWordSet
	 * @param tuples
	 */
	public void ExtractionOpinionWord(HashSet<String> publicWordSet,
			ArrayList<ArrayList<String>> tuples) {

		opinionExpanded = publicWordSet;
		int times = 0;
		while (true) {
			times++;
			HashSet<String> extractedTargetSet1 = new HashSet<String>();
			HashSet<String> extractedOpinionSet1 = new HashSet<String>();

			for (ArrayList<String> tupleString : tuples) {
				String dependentWordString = tupleString.get(0);
				String dependentTagString = tupleString.get(1);
				String relationString = tupleString.get(2);
				String governWordString = tupleString.get(3);
				String governTagString = tupleString.get(4);

				// Extract target words {T1} not in {Target} using R1 based on
				// {O-Expanded}
				if (filteringRule1(dependentWordString, dependentTagString,
						relationString, governWordString, governTagString,
						opinionExpanded)
						&& !targetSet.contains(governWordString)) {
					extractedTargetSet1.add(governWordString);
				}

				// Extract opinion words {O1} not in {O-Expanded} using R4 based
				// on
				// {O-Expanded}
				if (filteringRule4(dependentWordString, dependentTagString,
						relationString, governWordString, governTagString,
						opinionExpanded)
						&& !opinionExpanded.contains(dependentWordString)) {
					extractedOpinionSet1.add(dependentWordString);
				}
			}

			targetSet.addAll(extractedTargetSet1);
			opinionExpanded.addAll(extractedOpinionSet1);
			topicwordSet.addAll(extractedOpinionSet1);

			HashSet<String> extractedTargetSet2 = new HashSet<String>();
			HashSet<String> extractedOpinionSet2 = new HashSet<String>();

			for (ArrayList<String> tupleString : tuples) {
				String dependentWordString = tupleString.get(0);
				String dependentTagString = tupleString.get(1);
				String relationString = tupleString.get(2);
				String governWordString = tupleString.get(3);
				String governTagString = tupleString.get(4);

				// Extract target words {T2} not in {Target} using R3 based on
				// extractedTargetSet1
				if (filteringRule3(dependentWordString, dependentTagString,
						relationString, governWordString, governTagString,
						extractedTargetSet1)
						&& !targetSet.contains(dependentWordString)) {
					extractedTargetSet2.add(governWordString);
				}

				// Extract opinion words {O2} not in {O-Expanded} using R2 based
				// on
				// {Target}
				if (filteringRule2(dependentWordString, dependentTagString,
						relationString, governWordString, governTagString,
						extractedTargetSet1)
						&& !opinionExpanded.contains(dependentWordString)) {
					extractedOpinionSet2.add(dependentWordString);
				}
			}

			targetSet.addAll(extractedTargetSet2);
			opinionExpanded.addAll(extractedOpinionSet2);
			topicwordSet.addAll(extractedOpinionSet2);
			if (0 == extractedOpinionSet1.size()
					&& 0 == extractedOpinionSet2.size()
					&& 0 == extractedTargetSet1.size()
					&& 0 == extractedTargetSet2.size())
				break;
		}
		System.out.println("times:\t" + times);
	}

	/**
	 * Rule1: O→ O-Dep →T s.t. O∈{O}, O-Dep∈ {MR}, POS(T) ∈ {NN} then output t =
	 * T
	 * 
	 * @param dependentWordString
	 * @param dependentTagString
	 * @param relationString
	 * @param governWordString
	 * @param governTagString
	 * @return
	 */
	public boolean filteringRule1(String dependentWordString,
			String dependentTagString, String relationString,
			String governWordString, String governTagString,
			HashSet<String> opinionExpanded) {
		if (opinionExpanded.contains(dependentWordString)
				&& MRSet.contains(relationString)
				&& TargetTagSet.contains(governTagString))
			return true;
		return false;
	}

	/**
	 * Rule2: O→ O-Dep →T s.t. T∈{T}, O-Dep∈ {MR}, POS(O) ∈ {JJ,RB,VB} then
	 * output o = O
	 * 
	 * @param dependentWordString
	 * @param dependentTagString
	 * @param relationString
	 * @param governWordString
	 * @param governTagString
	 * @return
	 */
	public boolean filteringRule2(String dependentWordString,
			String dependentTagString, String relationString,
			String governWordString, String governTagString,
			HashSet<String> extractedTargetSet1) {
		if (extractedTargetSet1.contains(governWordString)
				&& MRSet.contains(relationString)
				&& OpinionTagSet.contains(dependentTagString))
			return true;
		return false;
	}

	/**
	 * Rule3:Ti(j) → Ti(j)-Dep → Tj(i) s.t. Tj(i) ∈ {T},Ti(j)- Dep∈
	 * {CONJ},POS(Ti(j)) ∈ {NN} then output t=Ti(j)
	 * 
	 * @param dependentWordString
	 * @param dependentTagString
	 * @param relationString
	 * @param governWordString
	 * @param governTagString
	 * @return
	 */
	public boolean filteringRule3(String dependentWordString,
			String dependentTagString, String relationString,
			String governWordString, String governTagString,
			HashSet<String> extractedTargetSet1) {
		if (relationString.contains("_"))
			relationString = relationString.split("_")[0];

		if (extractedTargetSet1.contains(governWordString)
				&& ConjSet.contains(relationString)
				&& TargetTagSet.contains(dependentTagString))
			return true;
		return false;
	}

	/**
	 * Rule4:Oi(j) →Oi(j)-Dep→ Oj(i) s.t. Oj(i) ∈ {O}, Oi(j)-Dep∈
	 * {CONJ},POS(Oi(j)) ∈ {JJ} then output o=Oi(j)
	 * 
	 * @param dependentWordString
	 * @param dependentTagString
	 * @param relationString
	 * @param governWordString
	 * @param governTagString
	 * @return
	 */
	public boolean filteringRule4(String dependentWordString,
			String dependentTagString, String relationString,
			String governWordString, String governTagString,
			HashSet<String> opinionExpanded) {
		if (relationString.contains("_"))
			relationString = relationString.split("_")[0];

		if (opinionExpanded.contains(governTagString)
				&& ConjSet.contains(relationString)
				&& OpinionTagSet.contains(dependentTagString))
			return true;
		return false;
	}

	public HashSet<String> loadPublicSentimentWord(String filename) {
		HashSet<String> publicWordSet = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = br.readLine()) != null) {
				// process the line.
				publicWordSet.add(line.toLowerCase());
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return publicWordSet;
	}

	public ArrayList<ArrayList<String>> loadDependencyRelation(String filename) {
		ArrayList<ArrayList<String>> dependencyArrayList = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = br.readLine()) != null) {
				// process the line.
				String arr[] = line.split("\t");
				ArrayList<String> arrList = new ArrayList<String>();
				String dependentWordString = arr[0].toLowerCase();
				String dependentTagString = arr[1];
				String relationString = arr[2];
				String governWordString = arr[3].toLowerCase();
				String governTagString = arr[4];

				arrList.add(dependentWordString);
				arrList.add(dependentTagString);
				arrList.add(relationString);
				arrList.add(governWordString);
				arrList.add(governTagString);

				dependencyArrayList.add(arrList);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dependencyArrayList;
	}

}
