package hanMiner.operator.data;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.io.AbstractWriter;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.tools.io.Encoding;
import hanMiner.text.DocumentSet;

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

    public WriteDocument(OperatorDescription description) {
        super(description, DocumentSet.class);
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList();
        types.add(new ParameterTypeFile(PARAMETER_TEXT_FILE,
                "File to save text to.",
                "txt",
                false));
        types.addAll(super.getParameterTypes());
        return types;
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
