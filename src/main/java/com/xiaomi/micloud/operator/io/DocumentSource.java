package com.xiaomi.micloud.operator.io;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.AbstractReader;
import com.rapidminer.operator.nio.CSVExampleSourceConfigurationWizardCreator;
import com.rapidminer.operator.nio.file.FileInputPortHandler;
import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeConfiguration;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypeString;
import com.xiaomi.micloud.document.RMDocument;

import java.util.Iterator;
import java.util.List;


/**
 * Super class of all operators requiring no input and creating an {@link ExampleSet}.
 *
 * @author Simon Fischer
 */
public abstract class DocumentSource extends AbstractReader<RMDocument> {

    public static final String PARAMETER_FILE = "file";
    private InputPort fileInputPort = getInputPorts().createPort("file");
    private FileInputPortHandler filePortHandler = new FileInputPortHandler(this, fileInputPort, this.getFileParameterName());

    static {
        AbstractReader.registerReaderDescription(new AbstractReader.ReaderDescription("txt", DocumentSource.class, PARAMETER_FILE));
    }

    public DocumentSource(final OperatorDescription description) {
        super(description, RMDocument.class);
        fileInputPort.addPrecondition(new SimplePrecondition(fileInputPort, new MetaData(FileObject.class)) {

            @Override
            protected boolean isMandatory() {
                return false;
            }
        });
    }

    protected String getFileExtension() {
        return "txt";
    }

    protected String getFileParameterName() {
        return PARAMETER_FILE;
    }

    @Override
    protected boolean supportsEncoding() {
        return true;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeFile(PARAMETER_FILE,
                "Name of the document file", "txt", false));
        return types;
    }

}