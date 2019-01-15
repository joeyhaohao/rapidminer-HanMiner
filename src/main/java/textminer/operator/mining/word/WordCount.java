package textminer.operator.mining.word;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import textminer.text.PlainText;

import java.util.*;

/**
 *
 * This operator counts word occurrence in texts. The input words should be
 * separated by white-space.
 *
 * @author Joeyhaohao
 */

public class WordCount extends Operator {

    private InputPort exampleSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    public WordCount(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = exampleSetInput.getData(PlainText.class).toString();
        String[] wordList = doc.split(" ");
        HashMap<String, Integer> wordMap = new HashMap<>();
        for (String word: wordList){
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }
        SortedSet<Map.Entry<String, Integer>> wordSorted = entriesSortedByValues(wordMap);

        String result = new String();
        for (Map.Entry<String, Integer> entry: wordSorted){
            result += entry.getKey()+ '\t' + entry.getValue() + '\n';
        }
        PlainText resultObject = new PlainText(result);
        exampleSetOutput.deliver(resultObject);
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
