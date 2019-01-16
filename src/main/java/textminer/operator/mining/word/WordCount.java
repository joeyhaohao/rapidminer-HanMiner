package textminer.operator.mining.word;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;
import textminer.operator.io.PlainText;

import java.util.*;

/**
 *
 * This operator counts word occurrence and frequency in texts. The tokens
 * should be separated by white-space.
 *
 * @author Joeyhaohao
 */

public class WordCount extends Operator {

    private InputPort exampleSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public WordCount(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = exampleSetInput.getData(PlainText.class).toString();
        String[] wordList = doc.split(" ");
        Integer size = wordList.length;
        HashMap<String, Integer> wordMap = new HashMap<>();
        for (String word: wordList){
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }
        SortedSet<Map.Entry<String, Integer>> wordSorted = entriesSortedByValues(wordMap);

//        String result = new String();
//        for (Map.Entry<String, Integer> entry: wordSorted){
//            result += entry.getKey()+ '\t' + entry.getValue() + '\n';
//        }

        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute newNominalAtt = AttributeFactory.createAttribute("word",
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(newNominalAtt);
        Attribute newNumericalAtt = AttributeFactory.createAttribute("count",
                Ontology.ATTRIBUTE_VALUE_TYPE.NUMERICAL);
        listOfAtts.add(newNumericalAtt);
        newNumericalAtt = AttributeFactory.createAttribute("frequency",
                Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
        listOfAtts.add(newNumericalAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (Map.Entry<String,Integer> entry: wordSorted) {
            double[] doubleArray = new double[listOfAtts.size()];
            doubleArray[0] = newNominalAtt.getMapping().mapString(
                    entry.getKey());
            doubleArray[1] = entry.getValue();
            doubleArray[2] = (double)entry.getValue()/size;
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
