package textminer.operator.mining.vectorizer;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.FloatArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.operator.*;
import com.rapidminer.parameter.*;
import com.rapidminer.tools.Ontology;
import textminer.operator.io.PlainText;
import textminer.operator.io.Text;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * This operator can vectorize a set of documents using Doc2vec. Users can
 * either load existing model or load corpus from file and train it manually.
 * The input documents should be separate by '\n'. Output is an n*m example
 * set {@link ExampleSet} (n: number of documents, m: embedding size). The
 * result can be used for training models.
 *
 * @author Joeyhaohao
 */
public class Doc2vec extends Operator{

    private static final String PARAMETER_EMBEDDING_SIZE = "embedding_size";
    private static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";
    private static final String PARAMETER_MODEL_FILE = "model_file";
    private static final String PARAMETER_CORPUS_FILE = "corpus_file";

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public Doc2vec(OperatorDescription description) {
        super(description);
        textInput.addPrecondition(
                new SimplePrecondition(textInput, new MetaData(Text.class)) {

                    @Override
                    protected boolean isMandatory() {
                        return false;
                    }
                });
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type = new ParameterTypeInt(PARAMETER_EMBEDDING_SIZE,
                "Number of dimensions for output vectors",
                10,500, 200);
        type.setExpert(false);
        types.add(type);
        type = new ParameterTypeBoolean(
                PARAMETER_LOAD_MODEL_FROM_FILE,
                "If set to true, load a word2vec model from file.",
                false);
        type.setExpert(false);
        types.add(type);
        type = new ParameterTypeFile(PARAMETER_MODEL_FILE,
                "Path to the model", null,true);
        type.setExpert(false);
        types.add(type);
        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = textInput.getData(PlainText.class).toString();
        String[] textList = doc.split("\n");
        int embedding_size = getParameterAsInt(PARAMETER_EMBEDDING_SIZE);
        boolean load_model_from_file = getParameterAsBoolean(PARAMETER_LOAD_MODEL_FROM_FILE);

        List<Attribute> listOfAtts = new LinkedList<>();
        for (int i = 0; i < embedding_size; i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute(
                    "dim" + Integer.toString(i),
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        WordVectorModel wordVectorModel = null;
        try {
            if (load_model_from_file) {
                File file = getParameterAsFile(PARAMETER_MODEL_FILE);
                wordVectorModel = new WordVectorModel(
                        file.getAbsolutePath());
            }
        else{
                File file = getParameterAsFile(PARAMETER_CORPUS_FILE);
                Word2VecTrainer trainerBuilder = new Word2VecTrainer();
                wordVectorModel = trainerBuilder.train(
                        file.getAbsolutePath(), "/tmp/model/word2vec.txt");
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
        for (int i = 0; i < textList.length; i++) {
            Vector vector = docVectorModel.query(textList[i]);
            float[] array = vector.getElementArray();
            table.addDataRow(new FloatArrayDataRow(array));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}