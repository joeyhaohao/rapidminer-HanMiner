package hanMiner.operator.translation;

import com.hankcs.hanlp.HanLP;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanMiner.text.SimpleTextSet;
import hanMiner.text.TextSet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This operator transforms traditional Chinese to simplified Chinese.
 *
 * @author joeyhaohao
 */

public class TraditionalToSimplified extends Operator {

    private InputPort textSetInput = getInputPorts().createPort("text");
    private OutputPort textSetOutput = getOutputPorts().createPort("text");

    public TraditionalToSimplified(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        TextSet textSet = textSetInput.getData(SimpleTextSet.class);
        List<String> output = new ArrayList<>();

        for (String text: textSet.getExamples()) {
            output.add(HanLP.convertToSimplifiedChinese(text));
        }

        textSetOutput.deliver(new SimpleTextSet(output));
    }
}
