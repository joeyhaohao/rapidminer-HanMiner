package com.xiaomi.micloud.operator.preprocessing.Filtering;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.SimpleResultObject;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
public class FilterStopwords extends Operator {

    /**
     * @param description
     */
    private InputPort exampleSetInput = getInputPorts().createPort("document");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("document");

    /**
     * The default constructor needed in exactly this signature
     *
     * @param description
     *            the operator description
     */
    public FilterStopwords(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        Set stopwordSet = new HashSet<String>();
        try {

            String pathname = "src/main/resources/files/stopwords";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = null;
            while ((line = br.readLine()) != null) {
                for (String s:line.split("\\s+")){
                    stopwordSet.add(s);
                }
            }
            System.out.print(stopwordSet.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }


        String doc = exampleSetInput.getData(SimpleResultObject.class).toString();
    }
}
