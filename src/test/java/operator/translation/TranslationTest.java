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

package operator.translation;

import com.hankcs.hanlp.HanLP;
import org.junit.Assert;
import org.junit.Test;

public class TranslationTest {
    private static final String string_simplified = "大卫贝克汉不仅仅是名著名球员，球场以外，其妻为前辣妹合唱团成员维多利亚·碧咸，" +
            "亦由于他拥有突出外表、百变发型及正面的形象，以至自己品牌的男士香水等商品，及长期担任运动品牌Adidas的代言人，" +
            "因此对大众传播媒介和时尚界等方面都具很大的影响力，在足球圈外所获得的认受程度可谓前所未见。";
    private static final String string_traditional = "大衛貝克漢不僅僅是名著名球員，球場以外，其妻為前辣妹合唱團成員維多利亞·碧咸，" +
            "亦由於他擁有突出外表、百變髮型及正面的形象，以至自己品牌的男士香水等商品，及長期擔任運動品牌Adidas的代言人，" +
            "因此對大眾傳播媒介和時尚界等方面都具很大的影響力，在足球圈外所獲得的認受程度可謂前所未見。";

    @Test
    public void testSimplifiedToTraditional() {
        String result = HanLP.convertToTraditionalChinese(string_simplified);
        Assert.assertEquals(string_traditional, result);
    }

    @Test
    public void testTraditionalToSimplified() {
        String result = HanLP.convertToSimplifiedChinese(string_traditional);
        Assert.assertEquals(string_simplified, result);
    }
}
