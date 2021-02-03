package hanMiner.operator.classification;

import com.rapidminer.operator.OperatorDescription;

/**
 *
 * This operator classify documents according to positive/negative sentiments using a Naive Bayes classifier.
 * The default model was trained on Weibo sentiment dataset (120k):
 * https://github.com/SophonPlus/ChineseNlpCorpus/blob/master/datasets/weibo_senti_100k/intro.ipynb
 *
 * @author joeyhaohao
 */

public class SentimentAnalysis extends AbstractClassificationOperator {

    public SentimentAnalysis(OperatorDescription description) {
        super(description, "data/model/classification/weibo_sentiment_12k.ser");
    }
}
