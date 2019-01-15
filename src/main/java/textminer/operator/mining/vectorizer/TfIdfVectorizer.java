package textminer.operator.mining.vectorizer;

import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.Ontology;
import textminer.operator.io.PlainText;

import java.util.*;

/**
 *
 * This operator counts word occurrence and frequency in texts.
 * The input should be separated by white-space.
 *
 * @author Joeyhaohao
 */

public class TfIdfVectorizer extends Operator {

    private InputPort exampleSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");
    private int dim_num;

    public TfIdfVectorizer(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        String input = "商品 服务\n" +
                "下雨天 地面 积水 分外 严重\n" +
                "结婚 尚未 结婚 确实 干扰 分词\n" +
                "买 水果 世博园 最后 世博会\n" +
                "中国 首都 北京\n" +
                "欢迎 新 老 师生 前来 就餐\n" +
                "工信处 女 干事 每月 下属 科室 亲口 交代 24 口 交换机 技术性 器件 安装 工作\n" +
                "页游 兴起 现在 页游 繁盛 依赖于 存档 进行 逻辑 判断 设计 减少 块 不能 完全 忽略 掉";

        TfIdfCounter tfIdf = new TfIdfCounter();
        String[] textList = input.split("\n");
        for (int i=0; i<textList.length; i++) {
            tfIdf.add(i, textList[i]);
        }
        Map<Object, Map<String, Double>> tfIdfMap = tfIdf.compute();

        Set<String> wordSet = tfIdf.allTf().keySet();
        dim_num = wordSet.size();
        HashMap<String, Double> dimMap = new HashMap<>();
        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute newNominalAtt = AttributeFactory.createAttribute("text",
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(newNominalAtt);
        for (int i = 0; i < dim_num; i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute("dim" + Integer.toString(i),
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (Object o: tfIdfMap.keySet()) {
            int index = (int) o;
            double[] doubleArray = new double[listOfAtts.size()];

            doubleArray[0] = newNominalAtt.getMapping().mapString(
                    textList[index]);
            for (int i = 1; i < dim_num+1; i++){
            }

            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
