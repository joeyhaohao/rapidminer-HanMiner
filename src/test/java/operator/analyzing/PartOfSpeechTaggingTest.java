package operator.analyzing;

import com.hankcs.hanlp.seg.common.Term;
import hanMiner.operator.processing.Tokenize;
import hanMiner.text.SimpleDocumentSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PartOfSpeechTaggingTest {
    private static final String input = "总统普京与特朗普通电话讨论太空探索技术公司";
    private static final SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));

    @Test
    public void testPOSTagging(){
        List<List<Term>> termsList = Tokenize.tokenize(documentSet);
        SimpleDocumentSet result = new SimpleDocumentSet(termsList, true);
        String expected = "总统/nnt 普京/nrf 与/cc 特朗普/nrf 通电话/vi 讨论/v 太空/s 探索/v 技术/n 公司/nis";
        Assert.assertEquals(expected, result.getDocumentsAsString());
    }
}
