package com.xiaomi.micloud.operator.io;

import java.util.LinkedList;
import java.util.List;

import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeList;
import com.rapidminer.parameter.ParameterTypeString;

import static com.rapidminer.operator.FileEchoOperator.PARAMETER_TEXT;

/**
 * Describe what your operator does.
 *
 * @author Insert your name here
 *
 */
public class CreateText extends Operator {

    /**
     * @param description
     */

    /** The parameter name for &quot;The first value which should be merged.&quot; */
    public static final String PARAMETER_VALUE_MAPPINGS = "value_mappings";
    public static final String PARAMETER_OLD_VALUES = "old_values";
    public static final String PARAMETER_NEW_VALUES = "new_values";
    public static final String PARAMETER_FILE = "file";

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

        ParameterType type = new ParameterTypeList(PARAMETER_VALUE_MAPPINGS, "The value mappings.", new ParameterTypeString(
                PARAMETER_OLD_VALUES, "The original values which should be replaced.", false), new ParameterTypeString(
                PARAMETER_NEW_VALUES, "Specifies the new value", false));
        type.setExpert(false);
        type.setPrimary(true);
        types.add(type);

        type = new ParameterTypeString(
                PARAMETER_TEXT,
                "This parameter defines which text is logged to the console when this operator is executed.",
                "This is a default text",
                false);
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