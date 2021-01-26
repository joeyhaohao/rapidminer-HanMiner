![](https://github.com/joeyhaohao/rapidminer-Hanminer/blob/master/src/main/resources/com/rapidminer/resources/icons/hanminer.png) HanMiner Extension for RapidMiner  
==============================

The HanMiner Extension of [RapidMiner](https://www.rapidminer.com) aims at freeing data analysts/scientists from repeated 
programming in Chinese (Mandarin, or Han language) processing & text mining tasks such as keyword extraction, sentiment analysis 
and public opinion monitor. Integrated in Rapidminer environment, Hanminer provides a set of easy-to-use text mining tools 
and allows users to work in the intuitive graphical user interface of RapidMiner.

## Features

* Text reader/writer
* Processing
  * Word segmentation
  * Stopwords filtering
* FeatureExtraction
  * Word Count
  * Vectorizer
    * CountVectorizer
    * TfIdfVectorizer
    * Doc2Vec
* Translation
  * Simplified Chinese to Traditional Chinese
  * Traditional Chinese to Simplifies Chinese
* Modeling
  * Sentiment Analysis
  
## Installation

1. Install [RapidMiner Studio](https://rapidminer.com/get-started/)
2. Install Java JDK 1.8 or higher
3. Clone this repo

   `git clone https://github.com/yunhaojohn/rapidminer-HanMiner.git`
4. On your project folder, execute in a terminal

   `./gradlew installExtension`
5. Open Rapidminer Studio. Use operators from this extension to perform text mining tasks. Happy mining!

