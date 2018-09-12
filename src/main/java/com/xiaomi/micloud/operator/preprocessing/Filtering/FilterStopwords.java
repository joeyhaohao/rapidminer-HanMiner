package com.xiaomi.micloud.operator.preprocessing.Filtering;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import java.util.HashSet;
import java.util.Set;

public class FilterStopwords extends Operator {

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
    public FilterStopwords(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
//        Set stopwordSet = new HashSet<String> {
//
//        }

    }
}
