package operator;

import org.junit.Test;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
//import com.hankcs.hanlp.utility.TestUtility;

import java.io.IOException;
import java.util.Map;

public class Word2vecTest {

//    private static final String TRAIN_FILE_NAME = TestUtility.ensureTestData("搜狗文本分类语料库已分词.txt", "http://hanlp.linrunsoft.com/release/corpus/sogou-mini-segmented.zip");
//    private static final String MODEL_FILE_NAME = "data/test/word2vec.txt";

//    @Test
//    public void doWork() {
//        String input = "工信处 女干事 每月 下属 科室 亲口 交代 24 口 交换机 技术性 器件 安装 工作";
//        String[] wordList = input.split(" ");
//        WordVectorModel wordVectorModel = trainOrLoadModel();
//
//        System.out.println(result);
//
//    }

//    static WordVectorModel trainOrLoadModel() throws IOException
//    {
//        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
//        {
//            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
//            {
//                System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec");
//                System.exit(1);
//            }
//            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
//            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
//        }
//
//        return loadModel();
//    }
//
//    static WordVectorModel loadModel() throws IOException
//    {
//        return new WordVectorModel(MODEL_FILE_NAME);
//    }
}