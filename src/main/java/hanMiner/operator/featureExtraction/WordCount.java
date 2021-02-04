package hanMiner.operator.featureExtraction;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;
import hanMiner.text.DocumentSet;

import java.util.*;

/**
 *
 * This operator computes word occurrence and frequency in documents. Tokens must be separated
 * by one or more white spaces.
 *
 * @author joeyhaohao
 */

public class WordCount extends Operator {

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public WordCount(OperatorDescription description) {
        super(description);
    }


    public static Map<String, Integer> wordCount(DocumentSet documentSet) {
        List<String> wordList = Arrays.asList(documentSet.toString().split("\\s+"));
        Map<String, Integer> wordCounter = new HashMap<>();
        for (String word: wordList) {
            wordCounter.put(word, wordCounter.getOrDefault(word, 0) + 1);
        }
        return wordCounter;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        Map<String, Integer> wordCounter = wordCount(documentSet);
        SortedSet<Map.Entry<String, Integer>> sortedCounter = entriesSortedByValues(wordCounter);

        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute newNominalAtt = AttributeFactory.createAttribute("Word",
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(newNominalAtt);
        Attribute newNumericalAtt = AttributeFactory.createAttribute("Count",
                Ontology.ATTRIBUTE_VALUE_TYPE.NUMERICAL);
        listOfAtts.add(newNumericalAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (Map.Entry<String, Integer> entry: sortedCounter) {
            double[] doubleArray = new double[listOfAtts.size()];
            doubleArray[0] = newNominalAtt.getMapping().mapString(
                    entry.getKey());
            doubleArray[1] = entry.getValue();
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }

    // Sort a hash map by value
    public static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
