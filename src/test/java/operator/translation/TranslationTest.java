package operator.translation;

import com.hankcs.hanlp.HanLP;
import org.junit.Assert;
import org.junit.Test;

public class TranslationTest {
    private static final String input = "我在台湾用笔记本电脑写程序";

    @Test
    public void testSimplifiedToTraditional() {
        String result = HanLP.convertToTraditionalChinese(input);
        Assert.assertEquals("我在臺灣用筆記本電腦寫程序", result);
    }
}
