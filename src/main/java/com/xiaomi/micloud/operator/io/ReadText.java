package com.xiaomi.micloud.operator.io;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.AbstractReader;
import com.rapidminer.operator.nio.file.FileInputPortHandler;
import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeFile;
import com.xiaomi.micloud.text.Text;
import com.xiaomi.micloud.text.SimpleText;

import java.io.*;
import java.util.List;


/**
 * Super class of all operators requiring no input and creating an {@link ExampleSet}.
 *
 * @author Simon Fischer
 */
public class ReadText extends AbstractReader<Text> {

    public static final String PARAMETER_FILE = "file";

    private InputPort fileInputPort = getInputPorts().createPort("file");
    private FileInputPortHandler filePortHandler = new FileInputPortHandler(this, fileInputPort, this.getFileParameterName());

    static {
        AbstractReader.registerReaderDescription(new AbstractReader.ReaderDescription("txt", ReadText.class, PARAMETER_FILE));
    }

    public ReadText(final OperatorDescription description) {
        super(description, Text.class);
        fileInputPort.addPrecondition(new SimplePrecondition(fileInputPort, new MetaData(FileObject.class)) {

            @Override
            protected boolean isMandatory() {
                return false;
            }
        });
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeFile(PARAMETER_FILE,
                "Name of the text file", "txt", false));
        return types;
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

    /** Creates (or reads) the ExampleSet that will be returned by {@link #apply()}. */
    public Text createDocument() throws OperatorException{
        File file = getParameterAsFile(PARAMETER_FILE);
        Text result = null;
        try{
            InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String doc = br.readLine();
            if (doc!=null){
                result = new SimpleText(doc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Text read() throws OperatorException {
        return createDocument();
    }

}