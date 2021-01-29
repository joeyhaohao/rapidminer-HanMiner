package hanMiner.operator.classification;

import com.rapidminer.operator.OperatorDescription;

/**
 *
 * This operator classify documents according to positive/negative sentiments using a Naive Bayes classifier.
 *
 * @author joeyhaohao
 */

public class SentimentAnalysis extends AbstractClassificationOperator {

    public SentimentAnalysis(OperatorDescription description) {
        super(description,
                "data/corpus/classification/ChnSentiCorp情感分析酒店评论",
                "http://file.hankcs.com/corpus/ChnSentiCorp.zip",
                "data/model/classification/sentiment_classifier.ser");
    }
}
