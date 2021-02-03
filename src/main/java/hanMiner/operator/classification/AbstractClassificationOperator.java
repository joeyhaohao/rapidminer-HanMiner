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
import com.rapidminer.parameter.ParameterTypeDirectory;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class for all classification operators. Users can either load a pretrained model or
 * use the default classification model. To train a new model, users can load a custom corpus from
 * file. The output is an example set {@link ExampleSet} that contains both original documents and
 * the result of classification.
 */

public class AbstractClassificationOperator extends Operator {

    private static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";
    private static final String PARAMETER_MODEL_FILE = "model_file";
    private static final String PARAMETER_USE_DEFAULT_MODEL = "use_default_model";
    private static final String PARAMETER_CORPUS_FILE = "corpus_file";
    private static final String PARAMETER_SAVE_MODEL_TO = "save_model_to";

    private String default_model_file;
    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public AbstractClassificationOperator(OperatorDescription description, String model_file) {
        super(description);
        this.default_model_file = model_file;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeBoolean(
                PARAMETER_LOAD_MODEL_FROM_FILE,
                "If set to true, load a pre-trained classification model from file.",
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

        type = new ParameterTypeBoolean(
                PARAMETER_USE_DEFAULT_MODEL,
                "If set to true, use the default model",
                true,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_MODEL_FROM_FILE,
                        true,
                        false));
        types.add(type);

        type = new ParameterTypeDirectory(PARAMETER_CORPUS_FILE,
                "Folder that contains a classified corpus to train a new model." +
                        "Text must be encoded in utf-8",
                true);
        type.setExpert(false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_MODEL_FROM_FILE,
                        false,
                        false));
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_USE_DEFAULT_MODEL,
                        true,
                        false));
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_SAVE_MODEL_TO,
                "Path to save the new model",
                "ser",
                false,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_MODEL_FROM_FILE,
                        false,
                        false));
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_USE_DEFAULT_MODEL,
                        true,
                        false));
        types.add(type);

        return types;
    }

    private NaiveBayesModel loadModel() throws OperatorException{
        boolean load_model_from_file = getParameterAsBoolean(PARAMETER_LOAD_MODEL_FROM_FILE);
        if (load_model_from_file) {
            // Use a pretrained model from file
            String model_file = getParameterAsString(PARAMETER_MODEL_FILE);
            return (NaiveBayesModel) IOUtil.readObjectFrom(model_file);
        } else {
            boolean use_default_model = getParameterAsBoolean(PARAMETER_USE_DEFAULT_MODEL);
            if (!use_default_model) {
                // Train and save a new classifier. It can take a few minutes for the first time.
                String corpus_file = getParameterAsString(PARAMETER_CORPUS_FILE);
                String save_model_to = getParameterAsString(PARAMETER_SAVE_MODEL_TO);
                IClassifier classifier  = new NaiveBayesClassifier();
                try {
                    classifier.train(corpus_file);
                    NaiveBayesModel model = (NaiveBayesModel) classifier.getModel();
                    IOUtil.saveObjectTo(model, save_model_to);
                    return model;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Use the default model
        return (NaiveBayesModel) IOUtil.readObjectFrom(default_model_file);
    }

    @Override
    public void doWork() throws OperatorException {
        SimpleDocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);

        NaiveBayesModel model = loadModel();
        IClassifier classifier  = new NaiveBayesClassifier(model);

        // Create a new example set with two columns
        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute docAtt = AttributeFactory.createAttribute(
                "Document" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(docAtt);
        Attribute classAtt = AttributeFactory.createAttribute(
                "Classification" ,
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(classAtt);
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (String doc: documentSet.getDocuments()) {
            double[] doubleArray = new double[2];
            doubleArray[0] = docAtt.getMapping().mapString(doc);
            doubleArray[1] = classAtt.getMapping().mapString(classifier.classify(doc));
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
