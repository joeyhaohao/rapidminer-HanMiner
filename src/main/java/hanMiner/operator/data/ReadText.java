package hanMiner.operator.data;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.AbstractReader;
import com.rapidminer.operator.nio.file.FileInputPortHandler;
import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.parameter.*;
import hanMiner.text.SimpleTextSet;
import hanMiner.text.TextSet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This operator can be used to create an {@link SimpleTextSet}. The user can load text
 * from .txt files, or create it in a text editor.
 *
 * @author joeyhaohao
 */
public class ReadText extends AbstractReader<TextSet> {

    public static final String PARAMETER_TEXT = "text";
    public static final String PARAMETER_FILE = "file";
    public static final String PARAMETER_IMPORT_FROM_FILE = "import_from_file";

    private InputPort fileInputPort = getInputPorts().createPort("file");
    private FileInputPortHandler filePortHandler = new FileInputPortHandler(
            this, fileInputPort, this.getFileParameterName());

    static {
        AbstractReader.registerReaderDescription(
                new AbstractReader.ReaderDescription("txt", ReadText.class, PARAMETER_FILE));
    }

    public ReadText(final OperatorDescription description) {
        super(description, TextSet.class);
        fileInputPort.addPrecondition(
                new SimplePrecondition(fileInputPort, new MetaData(FileObject.class)) {

            @Override
            protected boolean isMandatory() {
                return false;
            }
        });
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeText(
                PARAMETER_TEXT,
                "Text editor",
                TextType.PLAIN,
                false);
        type.setExpert(false);
//        type.setPrimary(true);
        type.setDefaultValue("This is a default text\n这是默认的文本");
        types.add(type);

        type = new ParameterTypeBoolean(
                PARAMETER_IMPORT_FROM_FILE,
                "If set to true, import text from file.",
                false);
        type.setExpert(false);
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_FILE,
                "Path to the text file", "txt", true);
        type.setExpert(false);
        types.add(type);
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

    /** Read document from file **/
    public TextSet readDocument() throws OperatorException{
        List<String> textSet = new ArrayList<>();
        File file = getParameterAsFile(PARAMETER_FILE);
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                textSet.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            textSet.add(e.getMessage());
        }

        return new SimpleTextSet(textSet);
    }

    /** Creat document from the editor **/
    public TextSet createDocument() throws OperatorException {
        String text = getParameterAsString(PARAMETER_TEXT);
        return new SimpleTextSet(Arrays.asList(text.split("\n")));
    }

    @Override
    public TextSet read() throws OperatorException {
        boolean import_from_file = getParameterAsBoolean(PARAMETER_IMPORT_FROM_FILE);
        if (import_from_file) {
            return readDocument();
        } else {
            return createDocument();
        }
    }
}
