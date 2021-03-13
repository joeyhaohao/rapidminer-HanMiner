/*
 * RapidMiner HanMiner Extension
 *
 * Copyright (C) 2018-2021 by joeyhaohao and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * Author: joeyhaohao (joeyhaohao@gmail.com)
 * README: https://github.com/joeyhaohao/rapidminer-HanMiner/blob/master/README.md
 */

package hanminer.operator;

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
import com.rapidminer.tools.Ontology;

/**
 * This is a test operator.
 *
 * @author joeyhaohao
 *
 */
@Deprecated
public class TestOperator extends Operator {

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
    public TestOperator(OperatorDescription description) {
        super(description);
        // checks whether a compatible IOObject is delivered
        exampleSetInput.addPrecondition(
                new SimplePrecondition(exampleSetInput, new MetaData(ExampleSet.class) ));

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
