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

import java.util.*;

/**
 *
 * This operator can vectorize a set of documents using tf-idf. The documents
 * should be separate by '\n'. The output is an n*m example set {@link ExampleSet}
 * (n is number of documents, m is number of words in all documents). The result
 * can be used for training purpose.
 *
 * @author Joeyhaohao
 */

public class TfIdf extends Operator {

    private InputPort exampleSetInput = getInputPorts().createPort("text");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public TfIdf(OperatorDescription description) {
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
        for (int i = 0; i < textList.length; i++) {
            tfIdf.add(i, textList[i]);
        }
        HashMap<String, Integer> word2dimMap = new HashMap<>();
        for (String word: tfIdf.allTf().keySet()){
            int i = word2dimMap.size();
            word2dimMap.put(word, i);
        }

        List<Attribute> listOfAtts = new LinkedList<>();
        Attribute newNominalAtt = AttributeFactory.createAttribute("text",
                Ontology.ATTRIBUTE_VALUE_TYPE.STRING);
        listOfAtts.add(newNominalAtt);
        for (int i = 0; i < word2dimMap.size(); i++) {
            Attribute newNumericalAtt = AttributeFactory.createAttribute("dim" + Integer.toString(i),
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNumericalAtt);
        }
        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);

        for (Map.Entry<Object, Map<String, Double>> entry: tfIdf.compute().entrySet()) {
            Map<String, Double> tfIdfMap = entry.getValue();
            double[] doubleArray = new double[listOfAtts.size()];
            Arrays.fill(doubleArray, 0);

            doubleArray[0] = newNominalAtt.getMapping().mapString(
                    textList[(int)entry.getKey()]);
            for (String word: tfIdfMap.keySet()){
                int index = word2dimMap.get(word);
                doubleArray[index] = tfIdfMap.get(word);
            }
            table.addDataRow(new DoubleArrayDataRow(doubleArray));
        }

        ExampleSet exampleSet = table.createExampleSet();
        exampleSetOutput.deliver(exampleSet);
    }
}
