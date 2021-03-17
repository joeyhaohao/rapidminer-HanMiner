![](https://github.com/joeyhaohao/rapidminer-Hanminer/blob/master/src/main/resources/com/rapidminer/resources/icons/com.rapidminer.extension.hanminer.png) HanMiner Extension for RapidMiner  
==============================

Understanding unstructured text data without the need to code! The HanMiner Extension of [RapidMiner](https://www.rapidminer.com) 
provides a fast and easy-to-use toolset for text processing/mining in Chinese Mandarin (Han language). It enables 
researchers/data analysts to extract valuable information from text with no programming knowledge required. Use it to 
build your own workflow for public opinion monitoring, sentiment analysis, keyword extraction for wordle, etc.

## Features
* Document reader/writer
* Processing
  * Word segmentation (tokenization)
  * Filtering
    * Filter stopwords
    * Filter tokens
    * Filter documents
* Feature Extraction
  * Word Count
  * Keyword extraction
  * Vectorizer
    * Count Vectorizer
    * TfIdf Vectorizer
    * Doc2Vec
* Analyzing
  * Part-of-Speech (POS) Tagging
  * Name Entity Recognition (NER)
* Translation
  * Simplified Chinese to Traditional Chinese
  * Traditional Chinese to Simplifies Chinese
* Classification
    * Document classification
    * Sentiment Analysis

## Getting Started
### Run with Intellij
1. Clone this repository
   `https://github.com/joeyhaohao/rapidminer-HanMiner.git`
2. Open the project with [Intellij](https://www.jetbrains.com/idea/). Use Java 1.8 as project SDK. 
3. Build the project
4. Run `GuiLauncher` under the source folder

## Acknowledgement
Some NLP models and functions of this project are supported by [Hanlp](https://hanlp.hankcs.com/docs/).
