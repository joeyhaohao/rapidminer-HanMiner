package operator.processing;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.documentation.OperatorDocumentation;
import hanMiner.operator.processing.filtering.FilterStopwords;
import hanMiner.operator.processing.filtering.FilterTokens;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static hanMiner.operator.processing.filtering.FilterTokens.CONDITION_MATCHES;
import static org.mockito.Mockito.when;


public class FilterTest {

    private final static OperatorDescription description = Mockito.mock(OperatorDescription.class);
    private final static OperatorDocumentation documentation = Mockito.mock(OperatorDocumentation.class);
    private final static String input = "亚马逊 公司 （ 英语 ： Amazon . com , Inc . ） 是 一家 总部 位于 美国 西雅图 的 " +
            "跨国 电子商务 企业 ， 业务 起始 于 线 上 书店 ， 不久 之后 商品 走向 多元化 。 目前 是 全球 最大 的 互联网 线 上 零售商 " +
            "之一 ， 也 是 美国 《 财富 》 杂志 2016 年 评选 的 全球 最大 500 家 公司 的 排行榜 中的 第 44 名 。 亚马逊 公司 在 " +
            "2017 年 的 财富 500 强 企业 里 列 第 8 位 。";

    @Before
    public void setup() {
        when(description.getOperatorDocumentation()).thenReturn(documentation);
        when(documentation.getShortName()).thenReturn("filterTest");
    }

    @Test
    public void testFilterStopwords() {
        FilterStopwords operator = new FilterStopwords(description);
        String result = operator.filterStopwords(input);
        String expected = "亚马逊 公司 （ 英语 ： Amazon . com , Inc . ） 一家 总部 位于 美国 西雅图 跨国 电子商务 企业 ， " +
                "业务 起始 线 书店 ， 不久 之后 商品 走向 多元化 。 目前 全球 最大 互联网 线 零售商 ， 美国 《 财富 》 杂志 " +
                "2016 年 评选 全球 最大 500 家 公司 排行榜 中的 44 名 。 亚马逊 公司 2017 年 财富 500 强 企业 里 列 8 位 。";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFilterTokens() {
        FilterTokens operator = new FilterTokens(description);
        String result = operator.filterTokens(input, true, false, false,
                CONDITION_MATCHES, false, "公司", false);
        String expected = "亚马逊 英语 Amazon com Inc 是 一家 总部 位于 美国 西雅图 的 跨国 电子商务 企业 业务 起始 于 线 上 " +
                "书店 不久 之后 商品 走向 多元化 目前 是 全球 最大 的 互联网 线 上 零售商 之一 也 是 美国 财富 杂志 2016 年 评选 " +
                "的 全球 最大 500 家 的 排行榜 中的 第 44 名 亚马逊 在 2017 年 的 财富 500 强 企业 里 列 第 8 位";
        Assert.assertEquals(expected, result);

        // Remove all non-chinese characters
        result = operator.filterTokens(input, true, false, true,
                0, false, "", false);
        expected = "亚马逊 公司 英语 是 一家 总部 位于 美国 西雅图 的 跨国 电子商务 企业 业务 起始 于 线 上 书店 不久 之后 商品 " +
                "走向 多元化 目前 是 全球 最大 的 互联网 线 上 零售商 之一 也 是 美国 财富 杂志 年 评选 的 全球 最大 家 公司 的 " +
                "排行榜 中的 第 名 亚马逊 公司 在 年 的 财富 强 企业 里 列 第 位";
        Assert.assertEquals(expected, result);
    }
}
