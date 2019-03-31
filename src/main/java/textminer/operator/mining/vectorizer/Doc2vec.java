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

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * This operator can vectorize a set of documents using Doc2vec. The documents
 * should be separate by '\n'. Stopwords will be automatically removed.
 * Output is an n*m example set {@link ExampleSet} (n is number of documents,
 * m is number of words in all documents). The result can be used for
 * training models.
 *
 * @author Joeyhaohao
 */
public class Doc2vec extends Operator{

    private static final String PARAMETER_EMBEDDING_SIZE = "embedding_size";
    private static final String PARAMETER_MODEL_FILE = "model_file";
    private static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";

    private InputPort textInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public Doc2vec(OperatorDescription description) {
        super(description);
        textInput.addPrecondition(
                new SimplePrecondition(textInput, new MetaData(FileObject.class)) {

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
                "If set to true, load a word2vec model from file.",
                false);
        types.add(type);
        type = new ParameterTypeFile(PARAMETER_MODEL_FILE,
                "Path to the model", null,true);
        types.add(type);
        type = new ParameterTypeInt(PARAMETER_EMBEDDING_SIZE,
                "Number of dimensions for output vectors",
                0,500, 200);
        types.add(type);
        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        String doc = textInput.getData(PlainText.class).toString();
        int embedding_size = getParameterAsInt(PARAMETER_EMBEDDING_SIZE);
        boolean load_model_from_file = getParameterAsBoolean(PARAMETER_LOAD_MODEL_FROM_FILE);
        String[] textList = doc.split("\n");

        List<Attribute> listOfAtts = new LinkedList<>();
        for (int i = 0; i < embedding_size; i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute("dim" + Integer.toString(i),
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        if (load_model_from_file){
            try {
                File file = getParameterAsFile(PARAMETER_MODEL_FILE);
                WordVectorModel wordVectorModel = new WordVectorModel(file.getAbsolutePath());
                DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
                for (int i = 0; i < textList.length; i++){
                    Vector vector = docVectorModel.query(textList[i]);
                    float[] array = vector.getElementArray();
                    table.addDataRow(new FloatArrayDataRow(array));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
//            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
//            WordVectorModel wordVectorModel = trainerBuilder.train(CORPUS_FILE_NAME, MODEL_FILE_NAME);
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}