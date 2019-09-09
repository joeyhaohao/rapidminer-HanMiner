![](https://github.com/joeyhaohao/rapidminer-Hanminer/blob/master/src/main/resources/META-INF/icon.png) RapidMiner Hanminer Extension
==============================

The Hanminer Extension of [RapidMiner](https://www.rapidminer.com) aims at freeing data analysts/scientists from repeated programming in handling Chinese (Han language) processing & mining tasks such as keyword extraction, sentiment analysis and public opinion monitor. Integrated in Rapidminer environment, Hanminer provides a set of easy-to-use text mining tools and allows users to work in the intuitive graphical user interface of Rapidminer.

## Features

* Input from text files or editor
* Text preprocessing
  * Word segmentation
  * Stopwords filtering
  * Word Count
* Mining
  * Vectorizer
    * TfIdfVectorizer
    * Word2vec/Doc2Vec
  * TextRank (in the future)
  * Sentiment Analysis (in the future)

## Installation

1. Install [RapidMiner Studio](https://rapidminer.com/get-started/)
2. Install Java JDK 1.8 or higher
3. Clone this repo

   `git clone https://github.com/yunhaojohn/rapidminer-textminer.git`
4. On your project folder, execute in a terminal

   `./gradlew installExtension`
5. Open Rapidminer Studio. Use operators from this extension to perform text mining tasks. Happy mining!

