package com.xiaomi.micloud.operator.preprocessing;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.huaban.analysis.jieba.viterbi.JiebaSegmenter;

/**
 * Describe what your operator does.
 *
 * @author Insert your name here
 *
 */
public class WordSegmentation extends Operator {

    /**
     * @param description
     */
    private InputPort exampleSetInput = getInputPorts().createPort("document");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("document");
    private JiebaSegmenter segmenter = new JiebaSegmenter();

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public WordSegmentation(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        exampleSetOutput.deliver(exampleSetInput.getAnyDataOrNull());
    }
}