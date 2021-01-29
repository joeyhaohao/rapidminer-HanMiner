![](https://github.com/joeyhaohao/rapidminer-Hanminer/blob/master/src/main/resources/com/rapidminer/resources/icons/hanminer.png) HanMiner Extension for RapidMiner  
==============================

No programming knowledge required for Chinese text mining! The HanMiner Extension of [RapidMiner](https://www.rapidminer.com) 
provides a set of easy-to-use tools to perform Chinese Mandarin (or Han language) text processing/mining tasks such as 
tokenization, document vectorization, keywords extraction and sentiment analysis, in graphical interface of RapidMiner. 
It frees researchers/data analysts/scientists with limited background in language programming from coding in text mining. 
Simply drag and connect operators in your process at [Rapidminer studio](https://rapidminer.com/products/studio/) and 
run your tasks!

## Features

* Document reader/writer
* Processing
  * Word segmentation (tokenization)
  * Filtering
    * Filter stopwords
    * Filter tokens
    * Filter documents
* FeatureExtraction
  * Word Count
  * Keyword extraction
  * Vectorizer
    * Count Vectorizer
    * TfIdf Vectorizer
    * Doc2Vec
* Analyzing
  * Part-of-Speech (POS) Tagging
  * Name Entity Recognition
  * Dependency parsing
* Translation
  * Simplified Chinese to Traditional Chinese
  * Traditional Chinese to Simplifies Chinese
* Classification
    * Document classification
    * Sentiment Analysis

## Installation

1. Install [RapidMiner Studio](https://rapidminer.com/get-started/)
2. Install Java JDK 1.8 or higher
3. Clone this repo

   `git clone https://github.com/yunhaojohn/rapidminer-HanMiner.git`
4. On your project folder, execute in a terminal

   `./gradlew installExtension`
5. Open Rapidminer Studio. Use operators from this extension to perform text mining tasks. Happy mining!

