Generate Tuples
=========================================
generate &lt;author, opinion word, target word> tuples based on TASC
*****

### DependencyParser <br />

通过tweet内容，作者，生成语法依赖关系组（dependentWord,dependentTag,relation,governWord,governTag,author）

	输入：
		content author.name	
	输出：
		dependency

### OpinionWordTargetExtraction <br />
通过公共情感词库，tweet内容，语法依赖关系，生成话题相关情感词库，target词库

	输入：
		publicword content dependency
	输出：
		topicword targetword
 
### GenerateTuples <br />
通过依赖关系，target词库，话题相关情感词及权重，生成（author,targetword,opinionword,weight）四元组

	输入：
		dependency targetword positive neutral negative
	输出：
		positiveTuple，neutralTuple，negativeTuple

