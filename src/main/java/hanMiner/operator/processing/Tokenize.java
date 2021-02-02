package hanMiner.operator.processing;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import hanMiner.text.SimpleDocumentSet;

import java.util.ArrayList;
import java.util.List;

/**
 * This operator segments Chinese documents into words (tokens). Users can choose a Standard HMM
 * tokenizer or a high-speed tokenizer (3x speed but less accuracy).
 *
 * @author joeyhaohao
 *
 */
public class Tokenize extends Operator {

    public static final String PARAMETER_HIGH_SPEED = "high_speed_mode";

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public Tokenize(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_HIGH_SPEED,
                "If set to true, use a high-speed tokenizer. " +
                        "Otherwise, use the default HMM tokenizer",
                false,
                false);
        types.add(type);
        return types;
    }

    public static List<List<Term>> tokenize(SimpleDocumentSet documentSet, boolean high_speed,
                                            boolean enable_place, boolean enable_institution) {
        List<List<Term>> output = new ArrayList<>();
        // Name entity recognition is enabled by default
        Segment segment = HanLP.newSegment();
        if (enable_place) {
            segment.enablePlaceRecognize(true);
        }
        if (enable_institution) {
            segment.enableOrganizationRecognize(true);
        }
        for (String doc: documentSet.getDocuments()){
            List<Term> segments = high_speed? SpeedTokenizer.segment(doc): segment.seg(doc);
            output.add(segments);
        }
        return output;
    }

    public static List<List<Term>> tokenize(SimpleDocumentSet documentSet, boolean high_speed) {
        return tokenize(documentSet, high_speed, false, false);
    }

    public static List<List<Term>> tokenize(SimpleDocumentSet documentSet) {
        return tokenize(documentSet, false, false, false);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        boolean high_speed = getParameterAsBoolean(PARAMETER_HIGH_SPEED);

        List<List<Term>> termsList = tokenize(documentSet, high_speed);
        SimpleDocumentSet output = new SimpleDocumentSet(termsList, false);

        documentSetOutput.deliver(output);
    }
}
