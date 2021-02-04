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

import com.hankcs.hanlp.HanLP;
import hanMiner.operator.featureExtraction.KeywordExtraction;
import hanMiner.text.SimpleDocumentSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeywordsExtractionTest {
    private static List<String> input = Arrays.asList("亚马逊公司（英语：Amazon.com, Inc.）是一家总部位于美国西雅图的跨国电子商务企业，业务起始于线上书店，不久之后商品走向多元化。目前是全球最大的互联网线上零售商之一[3][4]，也是美国《财富》杂志2016年评选的全球最大500家公司的排行榜中的第44名。亚马逊公司在2017年的财富500强企业里列第8位。",
            "文本挖掘（Text mining）有时也被称为文字探勘、文本数据挖掘等，大致相当于文字分析，一般指文本处理过程中产生高质量的信息。高质量的信息通常通过分类和预测来产生，如模式识别。文本挖掘通常涉及输入文本的处理过程（通常进行分析，同时加上一些派生语言特征以及消除杂音，随后插入到数据库中） ，产生结构化数据，并最终评价和解释输出。'高质量'的文本挖掘通常是指某种组合的相关性，新颖性和趣味性。",
            "典型的文本挖掘方法包括文本分类，文本聚类，概念/实体挖掘，生产精确分类，观点分析，文档摘要和实体关系模型（即，学习已命名实体之间的关系）。 文本分析包括了信息检索、词典分析来研究词语的频数分布、模式识别、标签\\注释、信息抽取，数据挖掘技术包括链接和关联分析、可视化和预测分析。本质上，首要的任务是，通过自然语言处理（NLP）和分析方法，将文本转化为数据进行分析。");
    private static SimpleDocumentSet documentSet = new SimpleDocumentSet(input);

    @Test
    public void testTfIdfKeywords(){
        List<String> keywords = KeywordExtraction.getTfIdfKeywords(documentSet, 5);
        List<String> expected = Arrays.asList("公司 亚马逊 全球 美国 最大", "文本 通常 产生 高质量 挖掘", "分析 文本 包括 实体 关系");
        Assert.assertEquals(expected, keywords);
    }

    @Test
    public void testTextRankKeywords(){
        List<String> keywords = new ArrayList<>();
        for (String doc: documentSet) {
            keywords.add(String.join(" ", HanLP.extractKeyword(doc, 5)));
        }
        List<String> expected = Arrays.asList("财富 美国 线 公司 全球", "文本 通常 产生 挖掘 指", "分析 文本 包括 实体 分类");
        Assert.assertEquals(expected, keywords);
    }
}
