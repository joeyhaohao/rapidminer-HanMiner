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

package operator.processing;

import com.hankcs.hanlp.seg.common.Term;
import java.util.Arrays;
import java.util.List;
import com.rapidminer.extension.hanminer.operator.processing.Tokenize;
import com.rapidminer.extension.hanminer.document.SimpleDocumentSet;
import org.junit.Assert;
import org.junit.Test;

public class TokenizeTest {
    private static final String input = "当下雨天地面积水分外严重\n" +
            "买水果然后来世博园最后去世博会\n" +
            "中国的首都是北京\n" +
            "欢迎新老师生前来就餐\n" +
            "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作\n" +
            "随着页游兴起到现在的页游繁盛，依赖于存档进行逻辑判断的设计减少了，但这块也不能完全忽略掉。";
    private static final SimpleDocumentSet documentSet = new SimpleDocumentSet(Arrays.asList(input.split("\n")));

    @Test
    public void testStandardTokenizer(){
        long startTime = System.currentTimeMillis();
        List<List<Term>> termsList = Tokenize.tokenize(documentSet);
        SimpleDocumentSet result = new SimpleDocumentSet(termsList, false);
        String expected = "当 下雨天 地面 积水 分外 严重\n" +
                "买 水果 然后 来 世博园 最后 去 世博会\n" +
                "中国 的 首都 是 北京\n" +
                "欢迎 新 老 师生 前来 就餐\n" +
                "工信处 女 干事 每月 经过 下属 科室 都 要 亲口 交代 24 口 交换机 等 技术性 器件 的 安装 工作\n" +
                "随着 页游 兴起 到 现在 的 页游 繁盛 ， 依赖于 存档 进行 逻辑 判断 的 设计 减少 了 ， 但 这 块 也 不能 完全 忽略 掉 。";
        Assert.assertEquals(expected, result.getDocumentsAsString());
        double duration = (System.currentTimeMillis() - startTime) * 1.0 / 1000;
        System.out.printf("speed：%.2f word/s\n", input.length() / duration);
    }

    @Test
    public void testHighSpeedTokenizer() {
        long startTime = System.currentTimeMillis();
        List<List<Term>> termsList = Tokenize.tokenize(documentSet, true);
        SimpleDocumentSet result = new SimpleDocumentSet(termsList, false);
        String expectedOutput = "当下 雨天 地面 积水 分外 严重\n" +
                "买水 果然 后来 世博园 最后 去世 博 会\n" +
                "中国 的 首都 是 北京\n" +
                "欢迎 新 老师 生前 来 就餐\n" +
                "工信处 女 干事 每月 经过 下属 科室 都 要 亲口 交代 2 4 口交 换 机 等 技术性 器件 的 安装 工作\n" +
                "随着 页游 兴起 到 现在 的 页游 繁盛 ， 依赖于 存档 进行 逻辑 判断 的 设计 减少 了 ， 但 这块 也 不能 完全 忽略 掉 。";
        Assert.assertEquals(expectedOutput, result.getDocumentsAsString());
        double duration = (System.currentTimeMillis() - startTime) * 1.0 / 1000;
        System.out.printf("speed:%.2f word/s\n", input.length() / duration);
    }
}
