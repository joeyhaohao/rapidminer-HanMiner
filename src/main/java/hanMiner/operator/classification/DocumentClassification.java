package hanMiner.operator.classification;

import com.rapidminer.operator.OperatorDescription;

/**
 *
 * This operator classify documents according to topics using a Naive Bayes classifier.
 * The default model was trained on mini Sogou news dataset (12k, 8 categories):
 * https://github.com/lijqhs/text-classification-cn
 *
 * @author joeyhaohao
 */

public class DocumentClassification extends AbstractClassificationOperator {

    public DocumentClassification(OperatorDescription description) {
        super(description, "data/model/classification/sogou_8cat.ser");
    }

}
