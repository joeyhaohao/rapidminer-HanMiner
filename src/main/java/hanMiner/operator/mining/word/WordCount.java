package hanMiner.operator.mining.word;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleTextSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * This operator counts word occurrence and frequency in texts. The tokens
 * should be separated by white-space.
 *
 * @author joeyhaohao
 */

public class WordCount extends Operator {

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public WordCount(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleTextSet textSet = textInput.getData(SimpleTextSet.class);
        List<String> wordList = Arrays.asList(textSet.toString().split("\\s+"));
        Map<String, Integer> counter = new HashMap<>();
        for (String word: wordList) {
            counter.put(word, counter.getOrDefault(word, 0) + 1);
        }
        SortedSet<Map.Entry<String, Integer>> sortedCounter = entriesSortedByValues(counter);

        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute newNominalAtt = AttributeFactory.createAttribute("Word",
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(newNominalAtt);
        Attribute newNumericalAtt = AttributeFactory.createAttribute("Count",
                Ontology.ATTRIBUTE_VALUE_TYPE.NUMERICAL);
        listOfAtts.add(newNumericalAtt);
        newNumericalAtt = AttributeFactory.createAttribute("Frequency",
                Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
        listOfAtts.add(newNumericalAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (Map.Entry<String, Integer> entry: sortedCounter) {
            double[] doubleArray = new double[listOfAtts.size()];
            doubleArray[0] = newNominalAtt.getMapping().mapString(
                    entry.getKey());
            doubleArray[1] = entry.getValue();
            doubleArray[2] = entry.getValue()*1.0 / sortedCounter.size();
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }

    static <K,V extends Comparable<? super V>>
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
