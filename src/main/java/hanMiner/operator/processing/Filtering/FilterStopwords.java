package hanMiner.operator.processing.Filtering;

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
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import hanMiner.text.SimpleTextSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This operator can be used to filter stopwords in text. The tokens must be separated
 * by white-space. The user can either use default stopwords dictionary, or load custom
 * stopwords from file.
 *
 * @author joeyhaohao
 */

public class FilterStopwords extends Operator {

    private static final String PARAMETER_LOAD_STOPWORDS_FROM_FILE = "load_stopwords_from_file";
    private static final String PARAMETER_FILTER_PUNCTUATION = "filter_punctuation";
    private static final String PARAMETER_FILTER_NUMBER = "filter_number";
    private static final String PARAMETER_FILTER_NON_CHINESE_CHAR = "filter_non_chinese_character";
    private static final String PARAMETER_STOPWORDS_FILE = "stopwords_file";

    private InputPort textSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("text");

    public FilterStopwords(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_LOAD_STOPWORDS_FROM_FILE,
                "If set to true, use custom stopwords dictionary from file." +
                        "Otherwise, use default stopwords",
                false);
        type.setExpert(false);
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_STOPWORDS_FILE,
                "Path to the stopwords file",
                null,
                true,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_STOPWORDS_FROM_FILE,
                        true,
                        true));
        types.add(type);

        type = new ParameterTypeBoolean(
                PARAMETER_FILTER_PUNCTUATION,
                "If set to true, filter all punctuation characters",
                true,
                false);
        types.add(type);

        type = new ParameterTypeBoolean(
                PARAMETER_FILTER_NUMBER,
                "If set to true, filter all numbers",
                false,
                false);
        types.add(type);

        type = new ParameterTypeBoolean(
                PARAMETER_FILTER_NON_CHINESE_CHAR,
                "If set to true, filter all non-Chinese characters",
                false,
                false);
        types.add(type);

        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleTextSet textSet = textSetInput.getData(SimpleTextSet.class);
        List<String> output = new ArrayList<>();
        boolean use_custom = getParameterAsBoolean(PARAMETER_LOAD_STOPWORDS_FROM_FILE);
        boolean filter_punctuation = getParameterAsBoolean(PARAMETER_FILTER_PUNCTUATION);
        boolean filter_number = getParameterAsBoolean(PARAMETER_FILTER_NUMBER);
        boolean filter_non_chinese = getParameterAsBoolean(PARAMETER_FILTER_NON_CHINESE_CHAR);
        for (String text: textSet.getExamples()){
            if (filter_punctuation) {
                text = text.replaceAll("\\p{P}", "");
            }
            if (filter_number) {
                text = text.replaceAll("[0-9]", "");
            }
            if (filter_non_chinese) {
                text = text.replaceAll("[^\\u4e00-\\u9fa5|\\s]", "");
            }
            List<String> wordList = Arrays.asList(text.split("\\s+"));
            if (!use_custom){
                // filter default stopwords
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
