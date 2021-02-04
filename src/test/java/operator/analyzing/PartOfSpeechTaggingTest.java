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
