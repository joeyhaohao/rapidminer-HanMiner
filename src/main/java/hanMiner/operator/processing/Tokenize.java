package hanMiner.operator.processing;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanMiner.text.SimpleDocumentSet;

import java.util.ArrayList;
import java.util.List;

/**
 * This operator segments Chinese documents into words (tokens).
 *
 * @author joeyhaohao
 *
 */
public class Tokenize extends Operator {

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public Tokenize(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<String> output = new ArrayList<>();


        for (String doc: documentSet.getDocuments()){
            List<Term> segments = HanLP.segment(doc);
            output.add(segments.toString().replaceAll("\\[|\\]|,",""));
        }
        SimpleDocumentSet resultObject = new SimpleDocumentSet(output);
        exampleSetOutput.deliver(resultObject);
    }
}
