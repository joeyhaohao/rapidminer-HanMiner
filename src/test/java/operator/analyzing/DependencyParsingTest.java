package operator.analyzing;

import hanMiner.operator.analyzing.DependencyParsing;
import org.junit.Assert;
import org.junit.Test;

public class DependencyParsingTest {
    private static String input = "总统普京与特朗普通电话讨论太空探索技术公司";

    @Test
    public void testDependencyParsing() {
        String result = DependencyParsing.parseDependency(input);
        String expected = "总统 --(定中关系)--> 普京, 普京 --(主谓关系)--> 讨论, 与 --(左附加关系)--> 特朗普, 特朗普 --(并列关系)--> 普京, 通电话 --(状中结构)--> 讨论, 讨论 --(核心关系)--> ##核心##, 太空 --(状中结构)--> 探索, 探索 --(动宾关系)--> 讨论, 技术 --(定中关系)--> 公司, 公司 --(动宾关系)--> 探索";
        Assert.assertEquals(expected, result);
    }
}
