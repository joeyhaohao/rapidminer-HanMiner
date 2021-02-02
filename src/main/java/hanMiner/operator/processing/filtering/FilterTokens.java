package hanMiner.operator.processing.filtering;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeString;
import hanMiner.text.SimpleDocumentSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * This operator filters tokens in text. Only tokens match the condition will be removed.
 *
 * @author joeyhaohao
 */
public class FilterTokens extends Operator {
    private static final String PARAMETER_REMOVE_PUNCTUATION = "remove_punctuations";
    private static final String PARAMETER_REMOVE_NUMBER = "remove_numbers";
    private static final String PARAMETER_REMOVE_NON_CHINESE_CHAR = "remove_non_chinese_characters";
    private static final String PARAMETER_REMOVE_CONDITION = "remove_condition";
    private static final String PARAMETER_USE_REGEX = "use_regular_expression";
    private static final String PARAMETER_EXPR = "expression";
    private static final String PARAMETER_INVERSE_CONDITION = "inverse_condition";
    private static final String[] CONDITIONS = { "matches", "contains" };
    public static final int CONDITION_MATCHES = 0;
    public static final int CONDITION_CONTAINS = 1;

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public FilterTokens(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();

        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_REMOVE_PUNCTUATION,
                "If set to true, filter all punctuation characters",
                true,
                false);
        types.add(type);

        type = new ParameterTypeBoolean(
                PARAMETER_REMOVE_NUMBER,
                "If set to true, filter all numbers",
                false,
                false);
        types.add(type);

        type = new ParameterTypeBoolean(
                PARAMETER_REMOVE_NON_CHINESE_CHAR,
                "If set to true, filter all non-Chinese characters",
                false,
                false);
        types.add(type);

        type = new ParameterTypeCategory(PARAMETER_REMOVE_CONDITION,
                "The condition to filter tokens.",
                CONDITIONS,
                CONDITION_MATCHES,
                false);
        types.add(type);

        type = new ParameterTypeBoolean(PARAMETER_INVERSE_CONDITION,
                "If set to true, inverse condition.",
                false,
                false);
        types.add(type);

        type = new ParameterTypeBoolean(PARAMETER_USE_REGEX,
                "If set to true, match regular expression. Otherwise, match words.",
                false,
        false);
        types.add(type);

        type = new ParameterTypeString(PARAMETER_EXPR,
                "The expression to be compared to. Only work if not empty.",
                "",
                false);
        types.add(type);

        return types;
    }

    public String filterTokens(String doc, boolean remove_punctuation, boolean remove_number, boolean remove_non_chinese,
                               int condition, boolean inverse_condition, String expr, boolean use_regex) {
        if (remove_punctuation) {
            doc = doc.replaceAll("\\p{P}", "");
        }
        if (remove_number) {
            doc = doc.replaceAll("[0-9]", "");
        }
        if (remove_non_chinese) {
            doc = doc.replaceAll("[^\\u4e00-\\u9fa5|\\s]", "");
        }
        // Do nothing if the expression is empty
        if (expr.length() == 0) {
            return doc;
        }

        Stream<String> wordStream = Arrays.asList(doc.split("\\s+")).stream();
        switch (condition) {
            case CONDITION_MATCHES:
                if (use_regex) {
                    wordStream = wordStream.filter(
                            word -> word.matches(expr) == inverse_condition
                    );
                } else {
                    wordStream = wordStream.filter(
                            word -> word.equals(expr) == inverse_condition
                    );
                }
                break;
            case CONDITION_CONTAINS:
                if (use_regex) {
                    wordStream = wordStream.filter(
                            word ->Pattern.compile(expr).matcher(word).find() == inverse_condition
                    );
                } else {
                    wordStream = wordStream.filter(
                            word -> word.contains(expr) == inverse_condition
                    );
                }
                break;
        }
        return String.join(" ", wordStream.collect(Collectors.toList()));
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        boolean remove_punctuation = getParameterAsBoolean(PARAMETER_REMOVE_PUNCTUATION);
        boolean remove_number = getParameterAsBoolean(PARAMETER_REMOVE_NUMBER);
        boolean remove_non_chinese = getParameterAsBoolean(PARAMETER_REMOVE_NON_CHINESE_CHAR);
        int condition = getParameterAsInt(PARAMETER_REMOVE_CONDITION);
        boolean inverse_condition = getParameterAsBoolean(PARAMETER_INVERSE_CONDITION);
        boolean use_regex = getParameterAsBoolean(PARAMETER_USE_REGEX);
        String expr = getParameterAsString(PARAMETER_EXPR);

        List<String> output = new ArrayList<>();
        for (String doc: documentSet.getDocuments()){
            String result = filterTokens(doc, remove_punctuation, remove_number, remove_non_chinese,
                    condition, inverse_condition, expr, use_regex);
            output.add(result);
        }

        documentSetOutput.deliver(new SimpleDocumentSet(output));
    }
}
