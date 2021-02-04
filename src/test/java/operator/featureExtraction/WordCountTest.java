package operator.featureExtraction;

import hanMiner.operator.featureExtraction.WordCount;
import hanMiner.text.SimpleDocumentSet;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class WordCountTest {
    private static String input = "文本 挖掘 Text mining 有时 也 被 称为 文字 探 勘 文本 数据挖掘 等 大致 相当于 文字 分析 一般 " +
            "指 文本 处理 过程 中 产生 高质量 的 信息 高质量 的 信息 通常 通过 分类 和 预测 来 产生 如 模式 识别 文本 挖掘 通常 涉及 " +
            "输入 文本 的 处理 过程 通常 进行 分析 同时 加上 一些 派生 语言 特征 以及 消除 杂音 随后 插入 到 数据库 中 产生 结构 化 " +
            "数据 并 最终 评价 和 解释 输出 高质量 的 文本 挖掘 通常 是 指 某种 组合 的 相关性 新颖 性 和 趣味性 典型 的 文本 挖掘 " +
            "方法 包括 文本 分类 文本 聚 类 概念 实体 挖掘 生产 精确 分类 观点 分析 文档 摘要 和 实体 关系 模型 即 学习 已 命名 实体 " +
            "之间 的 关系 文本 分析 包括 了 信息检索 词典 分析 来 研究 词语 的 频数 分布 模式 识别 标签 注释 信息 抽取 数据挖掘 技术 " +
            "包括 链接 和 关联 分析 可视化 和 预测 分析 本质 上 首要 的 任务 是 通过 自然语言处理 NLP 和 分析 方法 将 文本 转化 为 " +
            "数据 进行 分析";
    private static final SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));

    @Test
    public void testWordCount(){
        Map<String, Integer> counter = WordCount.wordCount(documentSet);
        Assert.assertEquals(11, (int) counter.get("文本"));
    }

}
