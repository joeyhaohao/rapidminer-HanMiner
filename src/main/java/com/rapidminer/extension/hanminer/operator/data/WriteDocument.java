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

package com.rapidminer.extension.hanminer.operator.data;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.io.AbstractWriter;
import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.nio.file.FileOutputPortHandler;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.tools.io.Encoding;
import com.rapidminer.extension.hanminer.document.DocumentSet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * This operator writes documents to file.
 *
 * @author joeyhaohao
 */
public class WriteDocument extends AbstractWriter<DocumentSet> {
    public static final String PARAMETER_TEXT_FILE = "file_name";
    protected OutputPort fileOutputPort = getOutputPorts().createPort("file");

    // TODO: remove final connection missing warning
    public WriteDocument(OperatorDescription description) {
        super(description, DocumentSet.class);
        getTransformer().addGenerationRule(fileOutputPort, FileObject.class);
    }

    @Override
    public boolean shouldAutoConnect(OutputPort outputPort) {
        if (outputPort == fileOutputPort) {
            return false;
        } else {
            return super.shouldAutoConnect(outputPort);
        }
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList();
        ParameterType type = FileOutputPortHandler.makeFileParameterType(this,
                getFileParameterName(), () -> fileOutputPort, getFileExtensions());
        type.setPrimary(true);
        types.add(type);
        types.addAll(super.getParameterTypes());
        return types;
    }

    protected String getFileParameterName() {
        return PARAMETER_TEXT_FILE;
    }

    protected String[] getFileExtensions() {
        return new String[] { "txt" };
    }

    @Override
    protected boolean supportsEncoding() {
        return true;
    }

    @Override
    public DocumentSet write(DocumentSet tSet) throws OperatorException {
        File file = this.getParameterAsFile(PARAMETER_TEXT_FILE, true);
        Charset encoding = Encoding.getEncoding(this);
        try {
            if (file != null) {
                tSet.writeDocumentFile(file, encoding);
            }
            return tSet;
        }  catch (IOException e) {
            throw new UserError(this, e, 303, new Object[]{file, e.getMessage()});
        }
    }
}
