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

package hanMiner.operator.analyzing;

import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import hanMiner.text.SimpleDocumentSet;

import java.util.ArrayList;
import java.util.List;

import static hanMiner.operator.processing.Tokenize.tokenize;

/**
 * This operator recognize Chinese name/place/organization entities in text. Only the entities will be
 * kept in the output.
 *
 * @author joeyhaohao
 *
 */
public class EntityRecognition extends Operator {
    private static final String PARAMETER_RECOGNIZE = "recognize";
    private static final String[] ENTITIES = { "name", "place", "organization"};
    private static final int ENTITY_NAME = 0;
    private static final int ENTITY_PLACE = 1;
    private static final int ENTITY_ORGANIZATION = 2;
    private static final String ENTITY_TAG_NAME = "nr";
    private static final String ENTITY_TAG_PLACE = "ns";
    private static final String ENTITY_TAG_ORGANIZATION = "nt";

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public EntityRecognition(OperatorDescription description) {
        super(description);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeCategory(PARAMETER_RECOGNIZE,
                "Entity type to recognize",
                ENTITIES,
                ENTITY_NAME,
                false);
        types.add(type);

        return types;
    }

    public String getEntities(List<Term> terms, String entity_tag) {
        List<String> entities = new ArrayList<>();
        for (Term term: terms) {
            if (term.nature.toString().contains(entity_tag)) {
                entities.add(term.word);
            }
        }
        return String.join(" ", entities);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<String> output = new ArrayList<>();
        String entity_tag = null;
        List<List<Term>> termsList = null;
        switch (getParameterAsInt(PARAMETER_RECOGNIZE)) {
            case ENTITY_NAME:
                entity_tag = ENTITY_TAG_NAME;
                termsList = tokenize(documentSet);
                break;
            case ENTITY_PLACE:
                entity_tag = ENTITY_TAG_PLACE;
                termsList = tokenize(documentSet, true, false, false);
                break;
            case ENTITY_ORGANIZATION:
                entity_tag = ENTITY_TAG_ORGANIZATION;
                termsList = tokenize(documentSet, false, true, false);
                break;
        }

        for (List<Term> terms: termsList){
            output.add(getEntities(terms, entity_tag));
        }

        SimpleDocumentSet resultObject = new SimpleDocumentSet(output);
        documentSetOutput.deliver(resultObject);
    }
}
