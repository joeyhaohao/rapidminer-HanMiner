/*
 * RapidMiner HanMiner Extension
 *
 * Copyright (C) 2018-2021 by joeyhaohao and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * Author: joeyhaohao (joeyhaohao@gmail.com)
 * README: https://github.com/joeyhaohao/rapidminer-HanMiner/blob/master/README.md
 */

package operator.classification;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.documentation.OperatorDocumentation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class ClassificationTest {

    private final static OperatorDescription description = Mockito.mock(OperatorDescription.class);
    private final static OperatorDocumentation documentation = Mockito.mock(OperatorDocumentation.class);

    @Before
    public void setup() {
        when(description.getOperatorDocumentation()).thenReturn(documentation);
        when(documentation.getShortName()).thenReturn("classificationTest");
    }

    @Test
    public void testSentimentAnalysis() {
        String[] input = {"前台客房服务态度非常好！早餐很丰富，房价很干净。再接再厉。",
                "结果大失所望，灯光昏暗，空间极其狭小，床垫质量恶劣，房间还伴着一股霉味。",
                "可利用文本分类实现情感分析，效果还行。",
                "这次包装的时候没有放好，给我折坏了，5555555，气死我了",
                "书的质量和印刷都不错，字的大小也刚刚好，很清楚，喜欢。"};
        List<String> expected = Arrays.asList("正面", "负面", "正面", "负面", "正面");
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom("data/model/classification/weibo_sentiment_120k.ser");
        IClassifier classifier = new NaiveBayesClassifier(model);
        List<String> result = Arrays.stream(input).map(text -> classifier.classify(text)).collect(Collectors.toList());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDocumentClassification() {
        String[] input = {"中国“铁腰”与英超球队埃弗顿分道扬镳，闪电般转投谢联（本赛季成功升入英超），此事运作速度之快令人惊诧。",
                "中国卫生部官员24日说，截至2005年底，中国各地报告的尘肺病病人累计已超过60万例，职业病整体防治形势严峻。",
                "麻生太郎在演讲中指出，为了维护东亚地区的安全形势，以及应对恐怖主义等新出现的威协，日本将和北约进行密切合作。",
                "从2001年到2005年，全国高校毕业生平均就业率始终只有70%左右,虽然工作并不好找，但却并不妨碍大学年年扩招",
                "在政府部门明确表示汽车投资过热、产能过剩的背景下，日前新飞集团却逆流而上：专用汽车工业园在河南新乡市开发区正式开始建设。"};
        List<String> expected = Arrays.asList("体育", "健康", "军事", "教育", "金融");
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom("data/model/classification/sogou_8cat.ser");
        IClassifier classifier = new NaiveBayesClassifier(model);
        List<String> result = Arrays.stream(input).map(text -> classifier.classify(text)).collect(Collectors.toList());
        Assert.assertEquals(expected, result);
    }
}
