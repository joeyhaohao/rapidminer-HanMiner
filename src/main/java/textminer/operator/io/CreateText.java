package textminer.operator.io;

import java.util.LinkedList;
import java.util.List;

import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;

import static com.rapidminer.operator.FileEchoOperator.PARAMETER_TEXT;

/**
 * This is a temporary template.
 *
 * @author Joeyhaohao
 *
 */
@Deprecated
public class CreateText extends Operator {

    /**
     * @param description
     */

    /** The parameter name for &quot;The first value which should be merged.&quot; */
    public static final String PARAMETER_TEXT = "text";

    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public CreateText(OperatorDescription description) {
        super(description);
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList<>();

        ParameterType type = new ParameterTypeText(
                PARAMETER_TEXT,
                "Text editor",
                TextType.PLAIN,
                false);
        type.setExpert(false);
//        type.setPrimary(true);
        type.setDefaultValue("This is a default text");
        types.add(type);
        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        String text = getParameterAsString(PARAMETER_TEXT);
        SimpleResultObject result = new SimpleResultObject("Text",text);
        exampleSetOutput.deliver(result);
    }
}