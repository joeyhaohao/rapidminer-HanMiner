package operator.analyzing;

import com.hankcs.hanlp.seg.common.Term;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.documentation.OperatorDocumentation;
import hanMiner.operator.analyzing.EntityRecognition;
import hanMiner.text.SimpleDocumentSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static hanMiner.operator.processing.Tokenize.tokenize;
import static org.mockito.Mockito.when;

public class EntityRecognitionTest {

    private final static OperatorDescription description = Mockito.mock(OperatorDescription.class);
    private final static OperatorDocumentation documentation = Mockito.mock(OperatorDocumentation.class);
    private static EntityRecognition operator;

    @Before
    public void setup() {
        when(description.getOperatorDocumentation()).thenReturn(documentation);
        when(documentation.getShortName()).thenReturn("filterTest");
        operator = new EntityRecognition(description);
    }

    @Test
    public void testNameRecognition() {
        String input =  "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。\n" +
                "武大靖创世界纪录夺冠，中国代表团平昌首金。\n" +
                "朱立伦：两岸都希望共创双赢 习朱历史会晤在即。\n" +
                "陕西首富吴一坚被带走 与令计划妻子有交集。\n" +
                "据美国之音电台网站4月28日报道，8岁的凯瑟琳·克罗尔（凤甫娟）和很多华裔美国小朋友一样，小小年纪就开始学小提琴了。她的妈妈是位虎妈么？\n" +
                "凯瑟琳和露西（庐瑞媛）跟她们的哥哥们有一些不同。";
        SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));
        List<List<Term>> termsList = tokenize(documentSet);
        String result = termsList.stream()
                .map(terms -> operator.getEntities(terms, "nr"))
                .collect(Collectors.joining("\n"));
        String expected = "秦光荣 李纪恒 仇和\n" +
                "武大靖\n" +
                "朱立伦\n" +
                "吴一坚 令计划\n" +
                "凯瑟琳·克罗尔 凤甫娟\n" +
                "凯瑟琳 露西 庐瑞媛";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPlaceRecognition() {
        String input = "2005年8月25日，飓风以一级飓风的强度在美国佛罗里达州登陆。\n" +
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机\n";
        SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));
        List<List<Term>> termsList = tokenize(documentSet, false, true, false);
        String result = termsList.stream()
                .map(terms -> operator.getEntities(terms, "ns"))
                .collect(Collectors.joining("\n"));
        String expected = "美国 佛罗里达州\n" +
                "宁夏 固原市 彭阳县 红河镇 黑牛沟村";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOrganizationRecognition() {
        String input = "我在上海林原科技有限公司兼职工作\n" +
                "我经常在台川喜宴餐厅吃饭，\n" +
                "偶尔去开元地中海影城看电影。\n" +
                "不用词典，福哈生态工程有限公司是动态识别的结果。\n";
        SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));
        List<List<Term>> termsList = tokenize(documentSet, false, false, true);
        String result = termsList.stream()
                .map(terms -> operator.getEntities(terms, "nt"))
                .collect(Collectors.joining("\n"));
        String expected = "林原科技有限公司\n" +
                "台川喜宴餐厅\n" +
                "开元地中海影城\n" +
                "福哈生态工程有限公司";
        Assert.assertEquals(expected, result);
    }
}
