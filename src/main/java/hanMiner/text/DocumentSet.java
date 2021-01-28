package hanMiner.text;

/**
 * Copyright (C) 2001-2018 by RapidMiner and the contributors
 *
 * Complete list of developers available at our web site:
 *
 * http://rapidminer.com
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.rapidminer.operator.ResultObject;


/**
 * Interface definition for document set.
 *
 * @author joeyhaohao
 */
public interface DocumentSet extends ResultObject, Cloneable, Iterable<String> {

    /** necessary since default method was added */
    static final long serialVersionUID = 4100925167567270064L;

    // ------------- Misc -----------------------------

    /** Clones the document set. */
    public Object clone();


    /**
     * Returns the hash code. Two Documents must deliver the same hash code if they are equal.
     */
    @Override
    public int hashCode();

    /**
     * Frees unused resources, if supported by the implementation. Does nothing by default.
     *
     */
    public default void cleanup() {
        // does nothing by default
    }

    /**
     * Returns the number of documents, which is equal to the number of lines in the documents.
     */
    public int size();

    /**
     * Returns the list of documents.
     */
    public List<String> getDocuments();

    /**
     * Returns the i-th document.
     */
    public String getDocument(int index);

    // -------------------- File Writing --------------------

    /** Writes the documents to a file. */
    public void writeDocumentFile(File dataFile, Charset encoding) throws IOException;

}
