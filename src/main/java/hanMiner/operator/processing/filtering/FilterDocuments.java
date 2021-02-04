/*
 * RapidMiner HanMiner Extension
 *
 * Copyright (C) 2018-2021 by joeyhaohao and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * Author: joeyhaohao (joeyhaohao@gmail.com)
 * README: https://github.com/joeyhaohao/rapidminer-HanMiner/blob/master/README.md
 */

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
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * This operator filters documents by conditions. Only documents match the condition will be removed.
 *
 * @author joeyhaohao
 */
public class FilterDocuments extends Operator {
    public static final String PARAMETER_REMOVE_CONDITION = "remove_condition";
    public static final String PARAMETER_USE_REGEX = "use_regular_expression";
    public static final String PARAMETER_EXPR = "expression";
    public static final String PARAMETER_INVERSE_CONDITION = "inverse_condition";
    public static final String[] CONDITIONS = { "matches", "contains" };
    public static final int CONDITION_MATCHES = 0;
    public static final int CONDITION_CONTAINS = 1;

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public FilterDocuments(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeCategory(PARAMETER_REMOVE_CONDITION,
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

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        boolean inverse_condition = getParameterAsBoolean(PARAMETER_INVERSE_CONDITION);
        boolean use_regex = getParameterAsBoolean(PARAMETER_USE_REGEX);
        String expr = getParameterAsString(PARAMETER_EXPR);

        // Do nothing if the expression is empty
        if (expr.length() == 0) {
            documentSetOutput.deliver(documentSet);
            return;
        }

        List<String> output = new ArrayList<>();
        boolean condition_satisfied = false;
        for (String doc: documentSet.getDocuments()){
            switch (getParameterAsInt(PARAMETER_REMOVE_CONDITION)) {
                case CONDITION_MATCHES:
                    if (use_regex) {
                        condition_satisfied = doc.matches(expr);
                    } else {
                        condition_satisfied = doc.equals(expr);
                    }
                    break;
                case CONDITION_CONTAINS:
                    if (use_regex) {
                        condition_satisfied = Pattern.compile(expr).matcher(doc).find();
                    } else {
                        condition_satisfied = doc.contains(expr);
                    }
                    break;
            }
            if (condition_satisfied == inverse_condition) {
                output.add(doc);
            }
        }

        documentSetOutput.deliver(new SimpleDocumentSet(output));
    }
}
