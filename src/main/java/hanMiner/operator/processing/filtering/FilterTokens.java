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
 * This operator filters tokens in text. Only tokens match the condition will be
 * kept in the output.
 *
 * @author joeyhaohao
 */
public class FilterTokens extends Operator {
    public static final String PARAMETER_CONDITION = "condition";
    public static final String PARAMETER_USE_REGEX = "use_regular_expression";
    public static final String PARAMETER_EXPR = "expression";
    public static final String PARAMETER_INVERSE_CONDITION = "inverse_condition";
    public static final String[] CONDITIONS = { "matches", "contains" };
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
        ParameterType type = new ParameterTypeCategory(PARAMETER_CONDITION,
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
                "The expression to be compared to",
                "",
                false);
        types.add(type);

        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        boolean inverse_condition = getParameterAsBoolean(PARAMETER_INVERSE_CONDITION);
        boolean use_regex = getParameterAsBoolean(PARAMETER_USE_REGEX);
        String expr = getParameterAsString(PARAMETER_EXPR);
        List<String> output = new ArrayList<>();
        for (String doc: documentSet.getDocuments()){
            Stream<String> wordStream = Arrays.asList(doc.split("\\s+")).stream();
            switch (getParameterAsInt(PARAMETER_CONDITION)) {
                case CONDITION_MATCHES:
                    if (use_regex) {
                        wordStream = wordStream.filter(
                                word -> word.matches(expr) ^ inverse_condition
                        );
                    } else {
                        wordStream = wordStream.filter(
                                word -> word.equals(expr) ^ inverse_condition
                        );
                    }
                case CONDITION_CONTAINS:
                    if (use_regex) {
                        wordStream = wordStream.filter(
                                word ->Pattern.compile(expr).matcher(word).find() ^ inverse_condition
                        );
                    } else {
                        wordStream = wordStream.filter(
                                word -> word.contains(expr) ^ inverse_condition
                        );
                    }
            }
            output.add(String.join(" ", wordStream.collect(Collectors.toList())));
        }

        documentSetOutput.deliver(new SimpleDocumentSet(output));
    }
}
