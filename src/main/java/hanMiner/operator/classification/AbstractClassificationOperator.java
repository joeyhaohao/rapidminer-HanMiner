package hanMiner.operator.classification;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;
import hanMiner.utility.TestUtility;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class for all classification operators. Users can either load a existing model or train a
 * new model with default or custom corpus. The output is an example set {@link ExampleSet} that contains
 * both original documents and classification result.
 */

public class AbstractClassificationOperator extends Operator {

    private static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";
    private static final String PARAMETER_MODEL_FILE = "model_file";
    private static final String PARAMETER_CORPUS_FILE = "corpus_file";

    private String default_corpus_file;
    private String corpus_url;
    private String default_model_file;
    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public AbstractClassificationOperator(OperatorDescription description, String corpus_file,
                                          String corpus_url, String model_file) {
        super(description);
        this.default_corpus_file = corpus_file;
        this.corpus_url = corpus_url;
        this.default_model_file = model_file;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_LOAD_MODEL_FROM_FILE,
                "If set to true, load a pre-trained classification model from file. " +
                        "Otherwise, use default model",
                false,
                false);
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_MODEL_FILE,
                "Path to the model",
                null,
                true,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_MODEL_FROM_FILE,
                        true,
                        true));
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_CORPUS_FILE,
                "If not null, use custom corpus to train a new model",
                null,
                true,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_MODEL_FROM_FILE,
                        false,
                        false));
        types.add(type);

        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);
        boolean load_model_from_file = getParameterAsBoolean(PARAMETER_LOAD_MODEL_FROM_FILE);

        NaiveBayesModel model = null;
        if (load_model_from_file) {
            // Use custom model from file
            String model_file = getParameterAsString(PARAMETER_MODEL_FILE);
            model = (NaiveBayesModel) IOUtil.readObjectFrom(model_file);
        } else {
            String corpus_file = getParameterAsString(PARAMETER_CORPUS_FILE);
            if (IOUtil.isFileExisted(default_model_file) && corpus_file == null) {
                // Load an existing model
                model = (NaiveBayesModel) IOUtil.readObjectFrom(default_model_file);
            } else {
                // Train and save a new classifier. It can take a few minutes for the first time.
                IClassifier classifier  = new NaiveBayesClassifier();
                if (corpus_file == null) {
                    corpus_file = default_corpus_file;
                }
                TestUtility.ensureTestData(corpus_file, corpus_url);
                try {
                    classifier.train(corpus_file);
                    model = (NaiveBayesModel) classifier.getModel();
                    IOUtil.saveObjectTo(model, default_model_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        IClassifier classifier  = new NaiveBayesClassifier(model);

        // Create a new example set with two columns
        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute docAtt = AttributeFactory.createAttribute(
                "Document" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(docAtt);

        Attribute sentimentAtt = AttributeFactory.createAttribute(
                "Classification" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(sentimentAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);
        for (String doc: documentSet.getDocuments()) {
            double[] doubleArray = new double[2];
            doubleArray[0] = docAtt.getMapping().mapString(doc);
            doubleArray[1] = sentimentAtt.getMapping().mapString(classifier.classify(doc));
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
