package operator;

import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.tools.Ontology;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TfIdfTest {

    @Test
    public void main(){

        String input = "商品 服务\n" +
                "下雨天 地面 积水 分外 严重\n" +
                "结婚 尚未 结婚 确实 干扰 分词\n" +
                "买 水果 世博园 最后 世博会\n" +
                "中国 首都 北京\n" +
                "欢迎 新 老 师生 前来 就餐\n" +
                "工信处 女 干事 每月 下属 科室 亲口 交代 24 口 交换机 技术性 器件 安装 工作\n" +
                "页游 兴起 现在 页游 繁盛 依赖于 存档 进行 逻辑 判断 设计 减少 块 不能 完全 忽略 掉";

        TfIdfCounter tfIdf = new TfIdfCounter();
        for (String text: input.split("\n")){
            tfIdf.add(text);
        }

        Set<String> wordSet = tfIdf.allTf().keySet();
        List<Attribute> listOfAtts = new LinkedList<>();
        for (int i = 0; i<wordSet.size(); i++){
            Attribute newNominalAtt = AttributeFactory.createAttribute("dim"+Integer.toString(i),
                    Ontology.ATTRIBUTE_VALUE_TYPE.REAL);
            listOfAtts.add(newNominalAtt);
        }

        MemoryExampleTable table = new MemoryExampleTable(listOfAtts);
        System.out.println(tfIdf.allTf());
    }
}