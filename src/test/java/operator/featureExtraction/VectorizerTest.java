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

package operator.featureExtraction;

import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.rapidminer.extension.hanminer.document.SimpleDocumentSet;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.rapidminer.extension.hanminer.operator.featureExtraction.vectorizer.TfIdfVectorizer.computeTfIDF;

public class VectorizerTest {
    private final static String input = "文本 挖掘 Text mining 有时 也 被 称为 文字 探 勘 文本 数据挖掘 等 大致 相当于 文字 分析 " +
            "一般 指 文本 处理 过程 中 产生 高质量 的 信息 \n" +
            "高质量 的 信息 通常 通过 分类 和 预测 来 产生 如 模式 识别 \n" +
            "文本 挖掘 通常 涉及 输入 文本 的 处理 过程 通常 进行 分析 同时 加上 一些 派生 语言 特征 以及 消除 杂音 随后 插入 到 数据库 中 产生 结构 化 数据 并 最终 评价 和 解释 输出 \n" +
            "高质量 的 文本 挖掘 通常 是 指 某种 组合 的 相关性 新颖 性 和 趣味性 \n" +
            "典型 的 文本 挖掘 方法 包括 文本 分类 文本 聚 类 概念 实体 挖掘 生产 精确 分类 观点 分析 文档 摘要 和 实体 关系 模型 即 学习 已 命名 实体 之间 的 关系 \n" +
            "文本 分析 包括 了 信息检索 词典 分析 来 研究 词语 的 频数 分布 模式 识别 标签 注释 信息 抽取 数据挖掘 技术 包括 链接 和 关联 分析 可视化 和 预测 分析 \n" +
            "本质 上 首要 的 任务 是 通过 自然语言处理 NLP 和 分析 方法 将 文本 转化 为 数据 进行 分析";
    private static final SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));

    @Test
    public void testTfIdf() {
        TfIdfCounter tfIdfCounter = computeTfIDF(documentSet);
        Map<Object, Map<String, Double>> tfIdfMap = tfIdfCounter.compute();
        double[] tf = {3, 0, 2, 1, 3, 1, 1};
        double idf = Math.log((7.0+1) / (6.0+1)) + 1;
        for (int i = 0; i < tf.length; i++) {
            tf[i] = tf[i]*idf;
        }
        int ind = 0;
        for (Map.Entry<Object, Map<String, Double>> entry: tfIdfMap.entrySet()) {
            Map<String, Double> word2IfIdfMap = entry.getValue();
            Assert.assertEquals(word2IfIdfMap.getOrDefault("文本", 0.0), tf[ind++], 0.0001);
        }
    }

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
