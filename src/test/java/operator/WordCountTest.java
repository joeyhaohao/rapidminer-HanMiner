package operator;

import org.junit.Test;
import java.util.*;

public class WordCountTest {

    @Test
    public void main(){

        String input = "文本 挖掘 称为 文字 探 勘 文本 数据挖掘 大致 相当于 文字 分析 指 文本 处理 过程 中 产生 高质量 信息 高质量 信息 通常 分类 预测 产生 模式 识别 文本 挖掘 通常 涉及 输入 文本 处理 过程 通常 进行 分析 加上 衍生 语言 特征 消除 杂音 插入 数据库 中   产生 结构 化 数据 最终 评价 解释 输出 ' 高品质 ' 文本 挖掘 通常 指 某种 组合 相关性 新颖 性 趣味性 典型 文本 挖掘 方法 包括 文本 分类 文本 聚 类 概念 / 实体 挖掘 生产 精确 分类 观点 分析 文档 摘要 实体 关系 模型 学习 命名 实体 之间 关系     文本 分析 包括 信息检索 词典 分析 研究 词语 频数 分布 模式 识别 标签 \\ 注释 信息 抽取 数据挖掘 技术 包括 链接 关联 分析 可视化 预测 分析 本质 首要 任务 自然语言处理 NLP 分析 方法 文本 转化 数据 进行 分析";
        String[] wordList = input.split(" ");
        HashMap<String, Integer> wordMap = new HashMap<>();
        for (String word: wordList){
            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
        }
        System.out.println(wordMap);
        SortedSet<Map.Entry<String, Integer>> wordSorted = entriesSortedByValues(wordMap);

        String result = new String();
        for (Map.Entry<String, Integer> entry: wordSorted){
            result += entry.getKey()+ '\t' + entry.getValue() + '\n';
        }

        System.out.println(result);
    }

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
