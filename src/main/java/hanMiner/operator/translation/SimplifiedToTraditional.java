package hanMiner.operator.translation;

import com.hankcs.hanlp.HanLP;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanMiner.text.SimpleTextSet;
import hanMiner.text.TextSet;

import java.util.*;

/**
 *
 * This operator can transform simplified Chinese to traditional Chinese.
 *
 * @author joeyhaohao
 */

public class SimplifiedToTraditional extends Operator {

    private static final String PARAMETER_MAX_FEATURES = "max_features";

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort textOutput = getOutputPorts().createPort("text");

    public SimplifiedToTraditional(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        TextSet textSet = textInput.getData(SimpleTextSet.class);
        List<String> output = new ArrayList<>();

        for (String text: textSet.getExamples()) {
            output.add(HanLP.convertToTraditionalChinese(text));
        }

        textOutput.deliver(new SimpleTextSet(output));
    }
}
