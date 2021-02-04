package hanMiner.operator.featureExtraction.vectorizer;

import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;
import hanMiner.text.DocumentSet;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This operator transforms documents to vectors using TF-IDF. Tokens must be separated
 * by one or more white spaces. The output is an n*m example set {@link ExampleSet}
 * (n: number of documents, m: number of features). The result can be fed into next-step NLP models.
 *
 * @author joeyhaohao
 */

public class TfIdfVectorizer extends Operator {

    private static final String PARAMETER_MAX_FEATURES = "max_features";

    private InputPort documentSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public TfIdfVectorizer(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeInt(
                PARAMETER_MAX_FEATURES,
                "This parameter specifies the max number of features in the result. " +
                        "The vocabulary will be built by top max_features ordered by term frequency " +
                        "across the corpus.",
                1,
                500,
                100,
                false);
        types.add(type);

        return types;
    }

    public static TfIdfCounter computeTfIDF(DocumentSet documentSet) {
        TfIdfCounter tfIdfCounter = new TfIdfCounter();
        for (int i = 0; i < documentSet.size(); i++) {
            String doc = documentSet.getDocument(i);
//            List<Term> termList = Arrays.stream(doc.split("\\s+"))
//                    .map(str -> new Term(str, null))
//                    .collect(Collectors.toList());
            tfIdfCounter.add(i, doc);
        }
        return tfIdfCounter;
    }

    public HashMap<String, Integer> getWordFeatureMap(TfIdfCounter tfIdfCounter, int featureNum) {
        // Clip max number of features ordered by term frequency
        List<Map.Entry<String, Double>> allTf = tfIdfCounter.sortedAllTf().subList(0, featureNum);
        HashMap<String, Integer> word2featureMap = new HashMap<>();
        int ind = 0;
        for (Map.Entry<String, Double> tf: allTf){
            word2featureMap.put(tf.getKey(), ind++);
        }
        return word2featureMap;
    }

    @Override
    public void doWork() throws OperatorException {
        DocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        int maxFeatureNum = getParameterAsInt(PARAMETER_MAX_FEATURES);

        TfIdfCounter tfIdfCounter = computeTfIDF(documentSet);
        Map<Object, Map<String, Double>> tfIdfMap = tfIdfCounter.compute();

        int featureNum = Math.min(tfIdfCounter.allTf().size(), maxFeatureNum);
        HashMap<String, Integer> word2featureMap = getWordFeatureMap(tfIdfCounter, featureNum);

        // Create new example set of vectors
        List<Attribute> listOfAtts = new LinkedList<>();
        for (int i = 0; i < featureNum; i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute(
                    "Feature_" + i,
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (Map.Entry<Object, Map<String, Double>> entry: tfIdfMap.entrySet()) {
            Map<String, Double> word2TfIdfMap = entry.getValue();
            double[] doubleArray = new double[listOfAtts.size()];
            Arrays.fill(doubleArray, 0.0);

            for (String word: word2TfIdfMap.keySet()){
                if (word2featureMap.containsKey(word)) {
                    int index = word2featureMap.get(word);
                    doubleArray[index] = word2TfIdfMap.get(word);
                }
            }
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
