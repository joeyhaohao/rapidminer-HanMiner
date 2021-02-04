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

package hanMiner.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.hankcs.hanlp.seg.common.Term;
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
     * Constructs a new SimpleDocumentSet backed by the given document.
     */
    public SimpleDocumentSet(List<String> docs) {
        // remove empty line and extra spaces
        this.documents = docs.stream()
                .filter(line -> line.length() > 0)
                .map(str -> str.replaceAll("\\s+", " "))
                .collect(Collectors.toList());
    }

    /**
     * Update documents with a list of terms.
     */
    public SimpleDocumentSet(Collection<List<Term>> termsList, boolean keep_nature) {
        this.documents = new ArrayList<>();
        for (List<Term> terms: termsList) {
            List<String> words;
            if (keep_nature) {
                words = terms.stream().map(term -> term.toString()).collect(Collectors.toList());
            } else {
                words = terms.stream().map(term -> term.word).collect(Collectors.toList());
            }
            documents.add(String.join(" ", words));
        }
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
     * Returns documents as a string.
     */
    public String getDocumentsAsString() {
        return String.join("\n", documents);
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
