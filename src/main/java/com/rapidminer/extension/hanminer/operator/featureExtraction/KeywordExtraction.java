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

package com.rapidminer.extension.hanminer.operator.featureExtraction;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.tools.Ontology;
import com.rapidminer.extension.hanminer.document.SimpleDocumentSet;

import java.util.*;
import java.util.stream.Collectors;

import static com.rapidminer.extension.hanminer.operator.featureExtraction.vectorizer.TfIdfVectorizer.computeTfIDF;

/**
 *
 * This operator extracts keywords from document using TextRank. Users can specify number
 * of keywords to extract. The output is an example set {@link ExampleSet} that contains
 * both the original documents and keywords.
 *
 * @author joeyhaohao
 */
public class KeywordExtraction extends Operator {
    private static final String PARAMETER_KEYWORD_NUMBER = "keyword number";
    private static final String PARAMETER_CRITERION = "criterion";
    private static final String[] CRITERIA = { "TextRank", "TF-IDF" };
    public static final int CRITERION_TEXTRANK = 0;
    public static final int CRITERION_TFIDF = 1;


    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public KeywordExtraction(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeInt(PARAMETER_KEYWORD_NUMBER,
                "The number of keywords to extract.",
                1, 10, 5, false);
        types.add(type);

        type = new ParameterTypeCategory(PARAMETER_CRITERION,
                "The criteria to select keywords",
                CRITERIA,
                CRITERION_TFIDF,
                false);
        types.add(type);
        return types;
    }

    public static List<String> getTfIdfKeywords(SimpleDocumentSet documentSet, int keywordNum) {
        TfIdfCounter tfIdfCounter = computeTfIDF(documentSet);
        Map<Object, Map<String, Double>> tfIdfMap = tfIdfCounter.compute();

        List<String> result = new ArrayList<>();
        for (Map.Entry<Object, Map<String, Double>> entry: tfIdfMap.entrySet()) {
            Map<String, Double> word2TfIdfMap = entry.getValue();
            List<Map.Entry<String, Double>> list = new LinkedList<>(word2TfIdfMap.entrySet());
            // Sort the entry list
            Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

            int size = Math.min(keywordNum, list.size());
            List<String> keywordList = list.subList(0, size)
                    .stream()
                    .map(item -> item.getKey())
                    .collect(Collectors.toList());
            String keywords = String.join(" ", keywordList);
            result.add(keywords);
        }
        return result;
    }


    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        int keywordNum = getParameterAsInt(PARAMETER_KEYWORD_NUMBER);

        // Create a example set with both original documents and extracted keywords
        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute docAtt = AttributeFactory.createAttribute(
                "document" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(docAtt);
        Attribute keywordAtt = AttributeFactory.createAttribute(
                "keywords" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(keywordAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        List<String> keywords = new ArrayList<>();
        switch (getParameterAsInt(PARAMETER_CRITERION)) {
            case CRITERION_TEXTRANK:
                for (String doc : documentSet.getDocuments()) {
                    keywords.add(String.join(" ", HanLP.extractKeyword(doc, keywordNum)));
                }
                break;
            case CRITERION_TFIDF:
                keywords = getTfIdfKeywords(documentSet, keywordNum);
                break;
        }

        for (int i = 0; i < keywords.size(); i++) {
            double[] doubleArray = new double[2];
            doubleArray[0] = docAtt.getMapping().mapString(documentSet.getDocument(i));
            doubleArray[1] = keywordAtt.getMapping().mapString(keywords.get(i));
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
