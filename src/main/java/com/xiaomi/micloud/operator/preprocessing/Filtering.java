package com.xiaomi.micloud.operator.preprocessing;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

public class Filtering extends Operator {

    /**
     * @param description
     */
    private InputPort exampleSetInput = getInputPorts().createPort("document");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("document");

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public WordSegmentation(OperatorDescription description) {
        super(description);
    }
}
