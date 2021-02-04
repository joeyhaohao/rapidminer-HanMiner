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
import hanMiner.text.SimpleDocumentSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * This operator can be used to remove stopwords in text. Tokens must be separated
 * by one or more whitespaces. Users can either use default stopwords dictionary, or
 * load custom stopwords from file.
 *
 * @author joeyhaohao
 */

public class FilterStopwords extends Operator {
    private static final String PARAMETER_LOAD_STOPWORDS_FROM_FILE = "load_stopwords_from_file";
    private static final String PARAMETER_STOPWORDS_FILE = "stopwords_file";

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public FilterStopwords(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_LOAD_STOPWORDS_FROM_FILE,
                "If set to true, use custom stopwords dictionary from file. " +
                        "Otherwise, use default stopwords",
                false,
                false);
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

        return types;
    }

    // Use default stopwords
    public String filterStopwords(String doc) {
        List<String> wordList = Arrays.asList(doc.split("\\s+"));
        List<Term> termList = wordList.stream().map(word -> new Term(word, null)).collect(Collectors.toList());
        CoreStopWordDictionary.apply(termList);
        return termList.stream().map(term -> term.word).collect(Collectors.joining(" "));
    }

    // Read custom stopwords from file
    public String filterStopwords(String doc, File file) {
        List<String> wordList = Arrays.asList(doc.split("\\s+"));
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

        wordList = wordList.stream().filter(word -> !stopwords.contains(word)).collect(Collectors.toList());
        return String.join(" ", wordList);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        boolean use_custom = getParameterAsBoolean(PARAMETER_LOAD_STOPWORDS_FROM_FILE);
        File file = getParameterAsFile(PARAMETER_STOPWORDS_FILE);

        List<String> output = new ArrayList<>();
        for (String doc: documentSet.getDocuments()) {
            if (use_custom) {
                doc = filterStopwords(doc, file);
            } else {
                doc = filterStopwords(doc);
            }
            output.add(doc);
        }

        documentSetOutput.deliver(new SimpleDocumentSet(output));
    }
}
