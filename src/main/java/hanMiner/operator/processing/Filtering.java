package hanMiner.operator.processing;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeFile;
import hanMiner.text.SimpleTextSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This operator can be used to filter stopwords in text. The tokens should
 * be separated by white-space. The user can either use default stopwords list,
 * or load customized list from the file system.
 *
 * @author joeyhaohao
 */

public class Filtering extends Operator {

    private static final String PARAMETER_LOAD_STOPWORDS_FROM_FILE = "load_stopwords_from_file";
    private static final String PARAMETER_STOPWORDS_FILE = "stopwords_file";

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    public Filtering(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_LOAD_STOPWORDS_FROM_FILE,
                "If set to true, use custom stopwords dictionary.",
                false);
        type.setExpert(false);
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_STOPWORDS_FILE,
                "Path to the stopwords dictionary", null, true);
        type.setExpert(false);
        types.add(type);
        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleTextSet textSet = textInput.getData(SimpleTextSet.class);
        List<String> output = new ArrayList<>();
        boolean use_custom = getParameterAsBoolean(PARAMETER_LOAD_STOPWORDS_FROM_FILE);
        for (String text: textSet.getExamples()){
            List<String> wordList = Arrays.asList(text.split("\\s+"));
            if (!use_custom){
                List<Term> termList = wordList.stream().map(word -> new Term(word, null)).collect(Collectors.toList());
                CoreStopWordDictionary.apply(termList);
                output.add(termList.toString().replaceAll("\\[|\\]|,",""));
            } else {
                // read custom stopwords from file
                File file = getParameterAsFile(PARAMETER_STOPWORDS_FILE);
                HashSet<String> stopwords = new HashSet<>();
                try {
                    InputStreamReader reader = new InputStreamReader(
                            new FileInputStream(file));
                    BufferedReader br = new BufferedReader(reader);
                    String line;
                    while ((line = br.readLine()) != null) {
                        stopwords.addAll(Arrays.asList(line.split("\\s+")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // filter stopwords
                wordList = wordList.stream().filter(word -> !stopwords.contains(word)).collect(Collectors.toList());
                output.add(String.join(" ", wordList));
            }
        }

        exampleSetOutput.deliver(new SimpleTextSet(output));
    }
}
