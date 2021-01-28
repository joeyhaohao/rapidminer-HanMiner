package hanMiner.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObjectAdapter;


/**
 * Implements wrapper methods of DocumentSet. Implements all ResultObject methods.<br>
 *
 * Apart from the interface methods the implementing classes must have a public single argument
 * clone constructor. This constructor is invoked by reflection from the clone method. Do not forget
 * to call the superclass method.
 *
 * @author joeyhaohao
 */
public class SimpleDocumentSet extends ResultObjectAdapter implements DocumentSet {

    private static final long serialVersionUID = 8596141056047402798L;

    /** List of documents. */
    private List<String> documents;

    /**
     * Constructs a new SimpleDocSet backed by the given document.
     */
    public SimpleDocumentSet(List<String> docs) {
        // remove empty line
        this.documents = docs.stream().filter(line -> line.length() > 0).collect(Collectors.toList());
    }

    /** This method overrides the implementation of ResultObjectAdapter and returns "DocumentSet". */
    @Override
    public String getName() {
        return "DocumentSet";
    }

    /** Counts the length of document. */
    @Override
    public int size() {
        return documents.size();
    }

    /**
     * Returns the list of documents.
     */
    public List<String> getDocuments() {
        return documents;
    }

    /**
     * Returns the i-th document.
     */
    public String getDocument(int index) {
        return documents.get(index);
    };

    // --- Visualisation and toString() methods ---

    @Override
    public String toString() {
        return String.join("\n", documents);
    }

    // -------------------- File Writing --------------------
    @Override
    public void writeDocumentFile(File dataFile, Charset encoding) throws IOException {
        try (OutputStream outStream = new FileOutputStream(dataFile);
             OutputStreamWriter osw = new OutputStreamWriter(outStream, encoding);
             PrintWriter out = new PrintWriter(osw)) {
            for (String doc : documents) {
                out.println(doc);
            }
        }
    }

    public String getExtension() {
        return "txt";
    }

    /**
     * Returns true, if all documents are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DocumentSet)) {
            return false;
        }
        DocumentSet ts = (DocumentSet) o;
        return toString().equals(ts.toString());
    }

    /** Returns the hash code of the documents. */
    @Override
    public int hashCode() {
        return documents.hashCode();
    }

    @Override
    public IOObject copy() {
        return clone();
    }

    /**
     * Clones the text by invoking a single argument clone constructor.
     */
    @Override
    public DocumentSet clone() {
        try {
            Class<? extends SimpleDocumentSet> clazz = getClass();
            Constructor<? extends SimpleDocumentSet> cloneConstructor = clazz.getConstructor(new Class[] { clazz });
            SimpleDocumentSet result = cloneConstructor.newInstance(new Object[] { this });
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot clone Text: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("'" + getClass().getName() + "' does not implement clone constructor!");
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException("Cannot clone " + getClass().getName() + ": " + e + ". Target: "
                    + e.getTargetException() + ". Cause: " + e.getCause() + ".");
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot clone " + getClass().getName() + ": " + e);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return documents.iterator();
    }


    public boolean isThreadSafeView() {
        return false;
    }
}
