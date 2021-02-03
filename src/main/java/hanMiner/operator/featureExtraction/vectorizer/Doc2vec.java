package hanMiner.operator.featureExtraction.vectorizer;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.FloatArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.operator.*;
import com.rapidminer.parameter.*;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.Ontology;
import hanMiner.text.SimpleDocumentSet;
import hanMiner.text.DocumentSet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * This operator transform documents into vectors using Doc2vec. Users can
 * either load existing model or train a new model with default or custom corpus.
 * The output is an n*m example set {@link ExampleSet} (n: number of documents,
 * m: embedding size). The result can be fed into next-step NLP models.
 *
 * @author joeyhaohao
 */
public class Doc2vec extends Operator{
    private static Logger logger = Logger.getLogger("Doc2vec");
    private static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";
    private static final String PARAMETER_MODEL_FILE = "model_file";
    private static final String PARAMETER_USE_DEFAULT_MODEL = "use_default_model";
    private static final String PARAMETER_EMBEDDING_SIZE = "embedding_size";
    private static final String PARAMETER_CORPUS_FILE = "corpus_file";
    private static final String DEFAULT_MODEL_FILE = "data/model/word2vec/word2vec_100";
    private static final String PARAMETER_SAVE_MODEL_TO = "save_model_to";

    private InputPort documentSetInput = getInputPorts().createPort("document set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public Doc2vec(OperatorDescription description) {
        super(description);
        documentSetInput.addPrecondition(
                new SimplePrecondition(documentSetInput, new MetaData(DocumentSet.class)) {

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
                PARAMETER_LOAD_MODEL_FROM_FILE,
                "If set to true, load a pre-trained word2vec model from file.",
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

        type = new ParameterTypeInt(PARAMETER_EMBEDDING_SIZE,
                "Number of dimensions for output vectors",
                10,
                500,
                100,
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

        type = new ParameterTypeDirectory(PARAMETER_CORPUS_FILE,
                "Folder that contains the corpus to train a new model.",
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
                "Path to save the new word2vec model",
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

    private WordVectorModel loadWord2VecModel() throws OperatorException {
        boolean load_model_from_file = getParameterAsBoolean(PARAMETER_LOAD_MODEL_FROM_FILE);
        if (load_model_from_file) {
            // Use a pretrained model from file
            File model_file = getParameterAsFile(PARAMETER_MODEL_FILE);
            try {
                return new WordVectorModel(model_file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            boolean use_default_model = getParameterAsBoolean(PARAMETER_USE_DEFAULT_MODEL);
            if (!use_default_model) {
                // Train and save a new classifier. It can take a few minutes for the first time.
                int embedding_size = getParameterAsInt(PARAMETER_EMBEDDING_SIZE);
                String corpus_file = getParameterAsString(PARAMETER_CORPUS_FILE);
                String save_model_to = getParameterAsString(PARAMETER_SAVE_MODEL_TO);
                Word2VecTrainer trainerBuilder = new Word2VecTrainer();
                trainerBuilder.setLayerSize(embedding_size);
                return trainerBuilder.train(corpus_file, save_model_to);
            }
        }

        // Use the default model
        try {
            return new WordVectorModel(DEFAULT_MODEL_FILE);
        } catch (IOException e) {
            logger.warning(String.format("fail to load default word2vec model from %s", DEFAULT_MODEL_FILE));
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void doWork() throws OperatorException {
        DocumentSet documentSet = documentSetInput.getData(SimpleDocumentSet.class);

        WordVectorModel word2VecModel = loadWord2VecModel();
        DocVectorModel docVectorModel = new DocVectorModel(word2VecModel);

        List<Attribute> listOfAtts = new LinkedList<>();
        for (int i = 0; i < docVectorModel.dimension(); i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute(
                    "Feature_" + i,
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);
        for (String doc: documentSet.getDocuments()) {
            float[] array = docVectorModel.query(doc).getElementArray();
            table.addDataRow(new FloatArrayDataRow(array));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
