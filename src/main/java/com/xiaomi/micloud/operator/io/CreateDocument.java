package com.xiaomi.micloud.operator.io;

import java.util.LinkedList;
import java.util.List;

import com.rapidminer.operator.*;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;

import static com.rapidminer.operator.FileEchoOperator.PARAMETER_TEXT;

/**
 * Describe what your operator does.
 *
 * @author Insert your name here
 *
 */
public class CreateDocument extends Operator {

    /**
     * @param description
     */

    private OutputPort exampleSetOutput = getOutputPorts().createPort("document");

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public CreateDocument(OperatorDescription description) {
        super(description);
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList<>();

        types.add(new ParameterTypeString(
                PARAMETER_TEXT,
                "This parameter defines which text is logged to the console when this operator is executed.",
                "This is a default text",
                false));
        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        String text = getParameterAsString(PARAMETER_TEXT);
        SimpleResultObject result = new SimpleResultObject("Document",text);
        exampleSetOutput.deliver(result);
    }
}