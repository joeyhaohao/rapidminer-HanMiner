package textminer.text;

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

import com.rapidminer.operator.ResultObject;


/**
 * Interface definition for all example sets. Usually, example sets do not contain any data but are
 * only views on an example table (for example for sampling or feature selection purposes). It
 * should be possible to create a layered view on the data, hence the name multi-layered data view.
 * ExampleSet implementation should support this view concept.
 *
 * @author Ingo Mierswa
 */
public interface Text extends ResultObject, Cloneable {

    /** necessary since default method was added */
    static final long serialVersionUID = 4100925167567270064L;

    // ------------- Misc -----------------------------

    /** Clones the Text. */
    public Object clone();


    /**
     * Returns the hash code. Two Documents must deliver the same hash code if they are equal.
     */
    @Override
    public int hashCode();

    /**
     * Frees unused resources, if supported by the implementation. Does nothing by default.
     *
     * @since 7.3
     */
    public default void cleanup() {
        // does nothing by default
    }

    /**
     * Returns the number of examples in this example set. This number should not be used to create
     * for-loops to iterate through all examples.
     */
    public int size();


    // -------------------- File Writing --------------------

    /** Writes the data and the attribute description to a file. */
    public void writeDataFile(File dataFile, int fractionDigits, boolean quoteNominal, boolean zipped, boolean append,
                              Charset encoding) throws IOException;


}
