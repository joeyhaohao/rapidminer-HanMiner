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
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import hanMiner.text.SimpleDocumentSet;
import hanMiner.text.DocumentSet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This operator can be used to create an document set{@link SimpleDocumentSet}. Users can
 * either load text from files or create it in a text editor. Each line of the text is taken
 * as an document. Empty lines will be removed.
 *
 * @author joeyhaohao
 */
public class ReadDocument extends AbstractReader<DocumentSet> {

    public static final String PARAMETER_TEXT = "text";
    public static final String PARAMETER_FILE = "file";
    public static final String PARAMETER_IMPORT_FROM_FILE = "import_from_file";

    private InputPort fileInputPort = getInputPorts().createPort("file");
    private FileInputPortHandler filePortHandler = new FileInputPortHandler(
            this, fileInputPort, this.getFileParameterName());

    static {
        AbstractReader.registerReaderDescription(
                new AbstractReader.ReaderDescription("txt", ReadDocument.class, PARAMETER_FILE));
    }

    public ReadDocument(final OperatorDescription description) {
        super(description, DocumentSet.class);
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
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_IMPORT_FROM_FILE,
                "If set to true, import text from file. Otherwise, use text in the editor",
                false,
                false);
        types.add(type);

        type = new ParameterTypeText(
                PARAMETER_TEXT,
                "Text editor",
                TextType.PLAIN,
                false);
        type.setExpert(false);
//        type.setPrimary(true);
        type.setDefaultValue("This is a default text\n这是默认的文本");
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_IMPORT_FROM_FILE,
                        true,
                        false));
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_FILE,
                "Path to the text file",
                "txt",
                true,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_IMPORT_FROM_FILE,
                        true,
                        true));
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

    /** Read documents from file **/
    public DocumentSet readDocument() throws OperatorException{
        List<String> documentSet = new ArrayList<>();
        File file = getParameterAsFile(PARAMETER_FILE);
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                documentSet.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            documentSet.add(e.getMessage());
        }

        return new SimpleDocumentSet(documentSet);
    }

    /** Creat documents from the editor **/
    public DocumentSet createDocument() throws OperatorException {
        String text = getParameterAsString(PARAMETER_TEXT);
        return new SimpleDocumentSet(Arrays.asList(text.split("\n")));
    }

    @Override
    public DocumentSet read() throws OperatorException {
        boolean import_from_file = getParameterAsBoolean(PARAMETER_IMPORT_FROM_FILE);
        if (import_from_file) {
            return readDocument();
        } else {
            return createDocument();
        }
    }
}
