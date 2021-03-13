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

package hanminer.operator.analyzing;

import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import hanminer.document.SimpleDocumentSet;
import java.util.List;

import static hanminer.operator.processing.Tokenize.tokenize;

/**
 * This operator performs part-of-speech (POS) tagging. Please see the meaning of each tag on:
 * http://www.hankcs.com/nlp/part-of-speech-tagging.html#h2-8
 *
 * @author joeyhaohao
 *
 */
public class PartOfSpeechTagging extends Operator {

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public PartOfSpeechTagging(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<List<Term>> termsList = tokenize(documentSet);
        SimpleDocumentSet output = new SimpleDocumentSet(termsList, true);

        documentSetOutput.deliver(output);
    }
}
