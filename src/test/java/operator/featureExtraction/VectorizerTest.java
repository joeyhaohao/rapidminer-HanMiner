package operator.featureExtraction;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class VectorizerTest {

    @Test
    public void testDoc2Vec(){
        String input = "亚马逊公司（英语：Amazon.com, Inc.）是一家总部位于美国西雅图的跨国电子商务企业，业务起始于线上书店，" +
                "不久之后商品走向多元化。目前是全球最大的互联网线上零售商之一，也是美国《财富》杂志2016年评选的全球最大500家公司的" +
                "排行榜中的第44名。亚马逊公司在2017年的财富500强企业里列第8位。";
        try {
            WordVectorModel word2VecModel = new WordVectorModel("data/model/word2vec/word2vec_100");
            DocVectorModel docVectorModel = new DocVectorModel(word2VecModel);
            Vector vector = docVectorModel.query(input);
            Assert.assertEquals(vector.size(), 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
