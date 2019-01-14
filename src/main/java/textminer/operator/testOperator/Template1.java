package textminer.operator.testOperator;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;

@Deprecated
public class Template1 extends Operator {

    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    /**
     * @param description
     */
    public Template1(OperatorDescription description) {
        super(description);
        getTransformer().addGenerationRule(exampleSetOutput, ExampleSet.class);
    }

    @Override
    public void doWork() throws OperatorException {
        // create the needed attributes
        List<Attribute> listOfAtts = new LinkedList<>();
        ExampleSet exampleSet;

        Attribute newNominalAtt = AttributeFactory.createAttribute("ID",
                Ontology.ATTRIBUTE_VALUE_TYPE.NOMINAL);
        listOfAtts.add(newNominalAtt);

        Attribute newNumericalAtt = AttributeFactory.createAttribute("random number",
                Ontology.ATTRIBUTE_VALUE_TYPE.NUMERICAL);
        listOfAtts.add(newNumericalAtt);

        // basis is a MemoryExampleTable, so create one and pass it the
        // list of attributes it should contain
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (int i = 0; i < 10; i++) {
            // every row is a double array internally; create and fill in data
            double[] doubleArray = new double[listOfAtts.size()];
            doubleArray[0] = newNominalAtt.getMapping().mapString(
                    UUID.randomUUID().toString());
            doubleArray[1] = Math.random();
            // create an example
            // create a DataRow from our double array and add it to our table
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        // finally create the ExampleSet from the table
        exampleSet = table.createExampleSet();

        exampleSetOutput.deliver(exampleSet);
    }
}