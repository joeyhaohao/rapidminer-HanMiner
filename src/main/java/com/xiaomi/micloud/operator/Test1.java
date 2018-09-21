package com.xiaomi.micloud.operator;

import java.util.logging.Level;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.*;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.Ontology;

/**
 * Describe what your operator does.
 *
 * @author Insert your name here
 *
 */
public class Test1 extends Operator {

    /**
     * @param description
     */
    private InputPort exampleSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public Test1(OperatorDescription description) {
        super(description);
        // checks whether a compatible IOObject is delivered
//        exampleSetInput.addPrecondition(
//                new SimplePrecondition(exampleSetInput, new MetaData(ExampleSet.class) ));

        getTransformer().addRule(new ExampleSetPassThroughRule(exampleSetInput, exampleSetOutput, SetRelation.EQUAL) {

            @Override
            public ExampleSetMetaData modifyExampleSet(ExampleSetMetaData metaData) throws UndefinedParameterError {
                AttributeMetaData text = metaData.getAttributeByName("raw text");
                if (text != null) {
                    text.setType(Ontology.STRING);
                    text.setName(text.getName()+"_tokenized");
                    text.setValueSetRelation(SetRelation.UNKNOWN);
                }
                return metaData;
            }
        });
    }

    @Override
    public void doWork() throws OperatorException {
        // fetch example set from input port
        ExampleSet exampleSet = exampleSetInput.getData(ExampleSet.class);
        // get attributes from example set
        Attributes attributes = exampleSet.getAttributes();
//        Attribute sourceAttribute = attributes.get("raw text");
        // create a new attribute
//        String newName = sourceAttribute.getName();
        String newName = "text_tokenized";
        Attribute targetAttribute = AttributeFactory.createAttribute(newName, Ontology.REAL);
        // set the index of the attribute
        targetAttribute.setTableIndex(attributes.size());
        // add the attribute
        exampleSet.getExampleTable().addAttribute(targetAttribute);
        attributes.addRegular(targetAttribute);

        // go through the example set and set the values of the new attribute
        for (Example example : exampleSet) {
            example.setValue(targetAttribute, Math.round(Math.random()*10+0.5));
        }

        exampleSetOutput.deliver(exampleSet);
    }
}