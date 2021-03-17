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

package com.rapidminer.extension.hanminer.operator.translation;

import com.hankcs.hanlp.HanLP;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.extension.hanminer.document.SimpleDocumentSet;
import com.rapidminer.extension.hanminer.document.DocumentSet;

import java.util.*;

/**
 *
 * This operator transforms simplified Chinese to traditional Chinese.
 *
 * @author joeyhaohao
 */

public class SimplifiedToTraditional extends Operator {
    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort documentSetOutput = getOutputPorts().createPort("document set");

    public SimplifiedToTraditional(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        DocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        List<String> output = new ArrayList<>();

        for (String doc: documentSet.getDocuments()) {
            output.add(HanLP.convertToTraditionalChinese(doc));
        }

        documentSetOutput.deliver(new SimpleDocumentSet(output));
    }
}
