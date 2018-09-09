package com.xiaomi.micloud.operator.preprocessing;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.SimpleResultObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

import java.util.List;
import java.util.Locale;

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
        String str = exampleSetInput.getData(SimpleResultObject.class).toString();
        List<SegToken> tokens = segmenter.process(str,SegMode.SEARCH);
        String result = String.format(Locale.getDefault(), "\n%s\n%s", tokens.toString());
        SimpleResultObject output = new SimpleResultObject("Document",result);
        exampleSetOutput.deliver(output);
    }
}