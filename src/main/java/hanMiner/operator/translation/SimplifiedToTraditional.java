package hanMiner.operator.translation;

import com.hankcs.hanlp.HanLP;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanMiner.text.SimpleDocumentSet;
import hanMiner.text.DocumentSet;

import java.util.*;

/**
 *
 * This operator transforms simplified Chinese to traditional Chinese.
 *
 * @author joeyhaohao
 */

public class SimplifiedToTraditional extends Operator {

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public SimplifiedToTraditional(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        DocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<String> output = new ArrayList<>();

        for (String doc: documentSet.getDocuments()) {
            output.add(HanLP.convertToTraditionalChinese(doc));
        }

        documentSetOutput.deliver(new SimpleDocumentSet(output));
    }
}
