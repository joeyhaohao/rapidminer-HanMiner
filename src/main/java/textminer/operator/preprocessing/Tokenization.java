package textminer.operator.preprocessing;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import textminer.text.PlainText;

import java.util.List;

/**
 * Describe what your operator does.
 *
 * @author Insert your name here
 *
 */
public class Tokenization extends Operator {

    /**
     * @param description
     */
    private InputPort exampleSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");
    private JiebaSegmenter segmenter = new JiebaSegmenter();

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public Tokenization(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = exampleSetInput.getData(PlainText.class).toString();
        List<SegToken> tokens = segmenter.process(doc,SegMode.SEARCH);
        String result = tokens.toString().replaceAll("\\[|\\]","");
        PlainText resultObject = new PlainText(result);
        exampleSetOutput.deliver(resultObject);
    }
}