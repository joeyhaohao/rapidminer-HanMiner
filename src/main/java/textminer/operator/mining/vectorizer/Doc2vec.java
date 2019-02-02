package textminer.operator.mining.vectorizer;

import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.operator.*;
import com.rapidminer.parameter.*;

import java.util.List;


public class Doc2vec extends Operator{

    public static final String PARAMETER_MODEL_FILE = "model_file";
    public static final String PARAMETER_TRAIN_FILE = "train_file";
    public static final String PARAMETER_LOAD_MODEL_FROM_FILE = "load_model_from_file";

    private InputPort fileInputPort = getInputPorts().createPort("file");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public Doc2vec(OperatorDescription description) {
        super(description);
        fileInputPort.addPrecondition(
                new SimplePrecondition(fileInputPort, new MetaData(FileObject.class)) {

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
                "If set to true, load Doc2vec from file.",
                false);
        type.setExpert(false);
        types.add(type);

        type = new ParameterTypeFile(PARAMETER_MODEL_FILE,
                "Name of the model file", null,true);
        type.setExpert(false);
        types.add(type);
        return types;
    }


}