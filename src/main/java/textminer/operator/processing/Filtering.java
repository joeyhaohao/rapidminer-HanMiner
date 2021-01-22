package textminer.operator.processing;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import textminer.operator.io.PlainText;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This operator can be used to filter stopwords in text. The tokens should
 * be separated by white-space. The user can either use default stopwords list,
 * or load customized list from the file system.
 *
 * @author Joeyhaohao
 */

public class Filtering extends Operator {

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    public Filtering(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = textInput.getData(PlainText.class).toString();
        String[] textList = doc.split("\n");
        String result = new String();
        for (String text: textList){
            List<String> wordList = Arrays.asList(text.split(" "));
            List<Term> termList = wordList.stream().map(word -> new Term(word, null)).collect(Collectors.toList());
            CoreStopWordDictionary.apply(termList);
            result += termList.toString().replaceAll("\\[|\\]|,", "") + '\n';
        }

        PlainText resultObject = new PlainText(result);
        exampleSetOutput.deliver(resultObject);
    }
}
