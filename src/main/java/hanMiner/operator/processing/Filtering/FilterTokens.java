package hanMiner.operator.processing.Filtering;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeString;
import hanMiner.text.SimpleTextSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * This operator can be used to filter custom strings in text.
 *
 * @author joeyhaohao
 */
public class FilterTokens extends Operator {
    public static final String PARAMETER_CONDITION = "condition";
    public static final String PARAMETER_STRING = "string";
    public static final String[] CONDITIONS = { "matches", "contains" };
    public static final int CONDITION_MATCHES = 0;
    public static final int CONDITION_CONTAINS = 1;

    private InputPort textSetInput = getInputPorts().createPort("text");
    private OutputPort textSetOutput = getOutputPorts().createPort("text");

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

        type = new ParameterTypeString(PARAMETER_STRING,
                "The string to be compared to",
                "",
                false);
        types.add(type);

        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleTextSet textSet = textSetInput.getData(SimpleTextSet.class);
        String str = getParameterAsString(PARAMETER_STRING);
        List<String> output = new ArrayList<>();
        for (String text: textSet.getExamples()){
            List<String> wordList = Arrays.asList(text.split("\\s+"));
            switch (getParameterAsInt(PARAMETER_CONDITION)) {
                case CONDITION_MATCHES:
                    wordList = wordList.stream().filter(word -> !word.equals(str)).collect(Collectors.toList());
                case CONDITION_CONTAINS:
                    wordList = wordList.stream().filter(word -> !word.contains(str)).collect(Collectors.toList());
            }
            output.add(String.join(" ", wordList));
        }

        textSetOutput.deliver(new SimpleTextSet(output));
    }
}
