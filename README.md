![](https://github.com/joeyhaohao/rapidminer-Hanminer/blob/master/src/main/resources/com/rapidminer/resources/icons/hanminer.png) HanMiner Extension for RapidMiner  
==============================

No programming knowledge required for Chinese text mining! The HanMiner Extension of [RapidMiner](https://www.rapidminer.com) 
provides fast and easy-to-use tool sets to perform Chinese Mandarin (or Han language) text processing/mining tasks 
such as word segmentation, document vectorization, keywords extraction and sentiment analysis, in graphical interface 
of RapidMiner. It aims at saving researchers/data analysts with limited background in language programming from coding 
in text mining. Simply drag and connect operators in your process at [Rapidminer studio](https://rapidminer.com/products/studio/) 
and run your tasks!

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
  * Name Entity Recognition
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
2. Open the project with [Intelli](https://www.jetbrains.com/idea/). Use Java 1.8 as project SDK. 
3. Build the project
4. Run `GuiLauncher` under the source folder

## Acknowledgement
Some NLP models and functions of this project are imported from [Hanlp](https://hanlp.hankcs.com/docs/).
