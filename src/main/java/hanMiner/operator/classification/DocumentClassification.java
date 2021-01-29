package hanMiner.operator.classification;

import com.rapidminer.operator.OperatorDescription;

/**
 *
 * This operator classify documents according to topics using a Naive Bayes classifier.
 *
 * @author joeyhaohao
 */

public class DocumentClassification extends AbstractClassificationOperator {

    public DocumentClassification(OperatorDescription description) {
        super(description,
                "data/corpus/classification/搜狗文本分类语料库迷你版",
                "http://file.hankcs.com/corpus/sogou-text-classification-corpus-mini.zip",
                "data/model/classification/document_classifier.ser");
    }

}
