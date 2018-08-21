package com.xiaomi.micloud.operator.io;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.AbstractReader;
import com.rapidminer.operator.nio.CSVExampleSource;
import com.rapidminer.operator.nio.ExcelExampleSourceConfigurationWizardCreator;
import com.rapidminer.operator.nio.model.AbstractDataResultSetReader;
import com.rapidminer.operator.nio.model.CSVResultSetConfiguration;
import com.rapidminer.operator.nio.model.DataResultSetFactory;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.*;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeConfiguration;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.Ontology;

import static com.rapidminer.operator.FileEchoOperator.PARAMETER_TEXT;

/**
 * Describe what your operator does.
 *
 * @author yunhao
 *
 */
public class TXTExampleSource extends AbstractDataResultSetReader {

    public static final String PARAMETER_TXT_FILE = "txt_file";
    static {
        AbstractReader.registerReaderDescription(new ReaderDescription("txt", TXTExampleSource.class, PARAMETER_TXT_FILE));
    }

    public TXTExampleSource(final OperatorDescription description) {
        super(description);
    }

    @Override
    protected DataResultSetFactory getDataResultSetFactory() throws OperatorException {
//        return new TXTResultConfiguration(this);
        return null;
    }

    @Override
    protected NumberFormat getNumberFormat() throws OperatorException {
        return null;
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList<>();

        types.add(new ParameterTypeString(
                PARAMETER_TEXT,
                "This parameter defines which text is logged to the console when this operator is executed.",
                "This is a default text",
                false));
        return types;
    }

    @Override
    protected String getFileExtension() {
        return "txt";
    }

    @Override
    protected String getFileParameterName() {
        return PARAMETER_TXT_FILE;
    }

}