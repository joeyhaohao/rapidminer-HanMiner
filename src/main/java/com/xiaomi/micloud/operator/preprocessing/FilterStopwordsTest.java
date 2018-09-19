package com.xiaomi.micloud.operator.preprocessing;
import com.rapidminer.operator.SimpleResultObject;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class FilterStopwordsTest {

    @Test
    public void doWork() {
        try {
            Set stopwordSet = new HashSet<String>();
            String pathname = "src/main/resources/files/stopwords"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = null;
            while ((line = br.readLine()) != null) {
                for (String s:line.split(",")){
                    stopwordSet.add(s);
                }
            }

            List wordList = new ArrayList<String>();
            String doc = "我, 爱, 北京, 的, 一个, 天安门";
            for (String word: doc.replace(" ","").split(",")){
                if (!stopwordSet.contains(word)){
                    wordList.add(word);
                }
            }
            String result = wordList.toString().replaceAll("\\[|\\]","");
//            SimpleResultObject resultObject = new SimpleResultObject("Document",result);
//            exampleSetOutput.deliver(resultObject);

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}