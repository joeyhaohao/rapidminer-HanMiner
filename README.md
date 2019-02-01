**Status:** Active (under active development, breaking changes may occur)

![](https://github.com/yunhaojohn/rapidminer-textminer/blob/master/src/main/resources/META-INF/icon.png) RapidMiner Textminer Extension
==============================

The Textminer Extension for [RapidMiner](https://www.rapidminer.com) aims at freeing data analysts/scientists from programming in Chinese text processing & mining. Integrated in Rapidminer environment, Textminer provides a set of easy-to-use text mining tools and allows users to work in the intuitive graphical user interface of Rapidminer.

## Features

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

5. Happy mining!

## Acknowledgement

1. [Rapidminer-studio](https://github.com/rapidminer/rapidminer-studio)

2. [Hanlp](https://github.com/hankcs/HanLP)
