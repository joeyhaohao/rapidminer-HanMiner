package hanMiner.operator.analyzing;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
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
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This operator parse dependencies among words within a sentence. Sentences must be separated by
 * Chinese period sign "。".
 *
 * @author joeyhaohao
 *
 */
public class DependencyParsing extends Operator {

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public DependencyParsing(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<String> sentences = Arrays.asList(documentSet.toString().split("。\n*"));

        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute docAtt = AttributeFactory.createAttribute(
                "Document" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(docAtt);
        Attribute classAtt = AttributeFactory.createAttribute(
                "Dependencies" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(classAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (String doc: sentences) {
            double[] doubleArray = new double[2];
            CoNLLSentence sentence = HanLP.parseDependency(doc);
            List<String> dependencies = new ArrayList<>();
            for (CoNLLWord word : sentence){
                dependencies.add(String.format("%s --(%s)--> %s", word.LEMMA, word.DEPREL, word.HEAD.LEMMA));
            }
            doubleArray[0] = docAtt.getMapping().mapString(doc);
            doubleArray[1] = classAtt.getMapping().mapString(String.join(", ", dependencies));
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
