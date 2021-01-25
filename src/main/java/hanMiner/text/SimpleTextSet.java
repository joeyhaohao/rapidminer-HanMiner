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
import java.util.zip.GZIPOutputStream;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObjectAdapter;


/**
 * Implements wrapper methods of abstract text. Implements all ResultObject methods.<br>
 *
 * Apart from the interface methods the implementing classes must have a public single argument
 * clone constructor. This constructor is invoked by reflection from the clone method. Do not forget
 * to call the superclass method.
 *
 * @author joeyhaohao
 */
public class SimpleTextSet extends ResultObjectAdapter implements TextSet {

    private static final long serialVersionUID = 8596141056047402798L;

    /** The document used for reading the texts from. */
    private List<String> docs;

    /**
     * Constructs a new Plaintext backed by the given document.
     */
    public SimpleTextSet(List<String> docs) {
        this.docs = docs;
    }

    /** This method overrides the implementation of ResultObjectAdapter and returns "Text". */
    @Override
    public String getName() {
        return "TextSet";
    }

    /** Counts the length of document. */
    @Override
    public int size() {
        return docs.size();
    }

    /**
     * Returns the list of text examples.
     */
    public List<String> getExamples() {
        return docs;
    }

    /**
     * Returns the i-th example.
     */
    public String getExample(int index) {
        return docs.get(index);
    };

    // --- Visualisation and toString() methods ---

    @Override
    public String toString() {
        return String.join("\n", docs);
    }

    // -------------------- File Writing --------------------
    @Override
    public void writeDataFile(File dataFile, Charset encoding) throws IOException {
        try (OutputStream outStream = new FileOutputStream(dataFile);
             OutputStreamWriter osw = new OutputStreamWriter(outStream, encoding);
             PrintWriter out = new PrintWriter(osw)) {
            for (String text : docs) {
                out.println(text);
            }
        }
    }

    public String getExtension() {
        return "txt";
    }

    public String getFileDescription() {
        return "attribute description file";
    }

    /**
     * Returns true, if all documents are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TextSet)) {
            return false;
        }
        TextSet ts = (TextSet) o;
        return toString().equals(ts.toString());
    }

    /** Returns the hash code of the text. */
    @Override
    public int hashCode() {
        return docs.hashCode();
    }

    @Override
    public IOObject copy() {
        return clone();
    }

    /**
     * Clones the text by invoking a single argument clone constructor.
     */
    @Override
    public TextSet clone() {
        try {
            Class<? extends SimpleTextSet> clazz = getClass();
            Constructor<? extends SimpleTextSet> cloneConstructor = clazz.getConstructor(new Class[] { clazz });
            SimpleTextSet result = cloneConstructor.newInstance(new Object[] { this });
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
        return docs.iterator();
    }


    public boolean isThreadSafeView() {
        return false;
    }
}
