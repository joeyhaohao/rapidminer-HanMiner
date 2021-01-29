package hanMiner.operator.featureExtraction;

import com.hankcs.hanlp.HanLP;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * This operator extracts keywords from document using TextRank. Users can specify number
 * of keywords to extract. The output is an example set {@link ExampleSet} that contains
 * both the original documents and keywords.
 *
 * @author joeyhaohao
 */
public class KeywordExtraction extends Operator {
    public static final String PARAMETER_KEYWORD_NUMBER = "keyword number";

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public KeywordExtraction(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeInt(PARAMETER_KEYWORD_NUMBER,
                "The number of keywords to extract.",
                1, 10, 5, false);
        types.add(type);

        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        int keywordNum = getParameterAsInt(PARAMETER_KEYWORD_NUMBER);

        // Create a new example set with both original documents and extracted keywords
        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute docAtt = AttributeFactory.createAttribute(
                "document" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(docAtt);

        Attribute keywordAtt = AttributeFactory.createAttribute(
                "keywords" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(keywordAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (String doc: documentSet.getDocuments()) {
            String keywords = String.join(" ", HanLP.extractKeyword(doc, keywordNum));
            double[] doubleArray = new double[2];
            doubleArray[0] = docAtt.getMapping().mapString(doc);
            doubleArray[1] = keywordAtt.getMapping().mapString(keywords);
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
