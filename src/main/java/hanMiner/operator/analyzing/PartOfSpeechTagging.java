package hanMiner.operator.analyzing;

import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanMiner.text.SimpleDocumentSet;
import java.util.List;

import static hanMiner.operator.processing.Tokenize.tokenize;

/**
 * This operator performs part-of-speech (POS) tagging. Please see the meaning of each tag on:
 * http://www.hankcs.com/nlp/part-of-speech-tagging.html#h2-8
 *
 * @author joeyhaohao
 *
 */
public class PartOfSpeechTagging extends Operator {

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public PartOfSpeechTagging(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<List<Term>> termsList = tokenize(documentSet);
        SimpleDocumentSet output = new SimpleDocumentSet(termsList, true);

        documentSetOutput.deliver(output);
    }
}
