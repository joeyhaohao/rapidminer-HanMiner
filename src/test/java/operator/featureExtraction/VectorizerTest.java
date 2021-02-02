package operator.featureExtraction;

import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.tools.Ontology;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class VectorizerTest {

    @Test
    public void testCountVectorizer(){
        String doc = "我 爱 北京 天安门\n" +
                "天安门 上 太阳 升";
        TfIdfCounter tfIdf = new TfIdfCounter();
        String[] textList = doc.split("\n");
        for (int i = 0; i < textList.length; i++) {
            tfIdf.add(i, textList[i]);
        }
        HashMap<String, Integer> word2dimMap = new HashMap<>();
        for (String word: tfIdf.allTf().keySet()){
            int i = word2dimMap.size();
            word2dimMap.put(word, i);
        }

        System.out.println(word2dimMap);
    }
}
