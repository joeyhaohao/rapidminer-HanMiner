package hanMiner.operator.featureExtraction.vectorizer;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
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
import hanMiner.text.SimpleTextSet;
import hanMiner.text.TextSet;

import javax.xml.soap.Text;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

    private static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";
    private static final String PARAMETER_MODEL_FILE = "model_file";
    private static final String PARAMETER_EMBEDDING_SIZE = "embedding_size";
    private static final String PARAMETER_CORPUS_FILE = "corpus_file";
    private static final String DEFAULT_MODEL_FILE_PREFIX = "data/model/word2vec/word2vec_";
    private static final String DEFAULT_CORPUS_FILE = "data/corpus/msr_training.utf8";

    private InputPort textSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public Doc2vec(OperatorDescription description) {
        super(description);
        textSetInput.addPrecondition(
                new SimplePrecondition(textSetInput, new MetaData(TextSet.class)) {

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
                "If set to true, load a word2vec model from file. Otherwise, use default model",
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

        type = new ParameterTypeInt(PARAMETER_EMBEDDING_SIZE,
                "Number of dimensions for output vectors",
                10,
                500,
                100,
                false);
        type.registerDependencyCondition(
                new BooleanParameterCondition(this,
                        PARAMETER_LOAD_MODEL_FROM_FILE,
                        true,
                        false));
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_CORPUS_FILE,
                "If not null, use custom corpus to train a new word2vec model",
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
        TextSet textSet = textSetInput.getData(SimpleTextSet.class);
        boolean load_model_from_file = getParameterAsBoolean(PARAMETER_LOAD_MODEL_FROM_FILE);
        int embedding_size = getParameterAsInt(PARAMETER_EMBEDDING_SIZE);

        List<Attribute> listOfAtts = new LinkedList<>();
        for (int i = 0; i < embedding_size; i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute(
                    "feature_" + i,
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        WordVectorModel wordVectorModel = null;
        if (load_model_from_file) {
            // use custom model from file
            File model_file = getParameterAsFile(PARAMETER_MODEL_FILE);
            try {
                 wordVectorModel = new WordVectorModel(model_file.getAbsolutePath());
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            String model_file = DEFAULT_MODEL_FILE_PREFIX + embedding_size;
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            trainerBuilder.setLayerSize(embedding_size);
            String corpus_file = getParameterAsString(PARAMETER_CORPUS_FILE);

            if (IOUtil.isFileExisted(model_file) && corpus_file == null) {
                // load existing model
                try {
                    wordVectorModel = new WordVectorModel(model_file);
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                // train and save a new model
                if (corpus_file == null) {
                    corpus_file = DEFAULT_CORPUS_FILE;
                }
                wordVectorModel = trainerBuilder.train(corpus_file, model_file);
            }
        }

        DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
        for (String text: textSet.getExamples()) {
            float[] array = docVectorModel.query(text).getElementArray();
            table.addDataRow(new FloatArrayDataRow(array));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
