package hanMiner.operator.processing;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanMiner.text.SimpleTextSet;

import java.util.ArrayList;
import java.util.List;

/**
 * This operator segments Chinese sentences into separate words. One sentence
 * takes one row. This means sentences should be separated by '\n'.
 *
 * @author joeyhaohao
 *
 */
public class Tokenization extends Operator {

    private InputPort textSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    public Tokenization(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleTextSet textSet = textSetInput.getData(SimpleTextSet.class);
        List<String> output = new ArrayList<>();


        for (String text: textSet.getExamples()){
            List<Term> segments = HanLP.segment(text);
            output.add(segments.toString().replaceAll("\\[|\\]|,",""));
        }
        SimpleTextSet resultObject = new SimpleTextSet(output);
        exampleSetOutput.deliver(resultObject);
    }
}
