package textminer.operator.processing;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.HanLP;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import textminer.operator.io.PlainText;
import java.util.List;

/**
 * This operator segments Chinese sentences into separate words. One sentence
 * takes one row. This means sentences should be separated by '\n'.
 *
 * @author Joeyhaohao
 *
 */
public class Tokenization extends Operator {

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    public Tokenization(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = textInput.getData(PlainText.class).toString();
        String[] textList = doc.split("\n");
        String result = new String();
        for (String text: textList){
            List<Term> segments = HanLP.segment(text);
            result += segments.toString().replaceAll("\\[|\\]|,","") + '\n';
        }
        PlainText resultObject = new PlainText(result);
        exampleSetOutput.deliver(resultObject);
    }
}