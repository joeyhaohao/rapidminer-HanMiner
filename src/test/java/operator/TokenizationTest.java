package operator;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import java.util.stream.Collectors;
import java.util.List;
import org.junit.Test;

public class TokenizationTest {

    @Test
    public void main(){
        String input = "商品和服务\n" +
                "当下雨天地面积水分外严重\n" +
                "结婚的和尚未结婚的确实在干扰分词啊\n" +
                "买水果然后来世博园最后去世博会\n" +
                "中国的首都是北京\n" +
                "欢迎新老师生前来就餐\n" +
                "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作\n" +
                "随着页游兴起到现在的页游繁盛，依赖于存档进行逻辑判断的设计减少了，但这块也不能完全忽略掉。";
        String[] textList = input.split("\n");
        String result = new String();
        for (String text: textList){
            List<Term> segments = HanLP.segment(text);
            result = result + segments.toString().replaceAll("\\[|\\]|,","") + '\n';
        }
        System.out.println(result);

    }
}
