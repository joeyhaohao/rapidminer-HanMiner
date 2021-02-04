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

package hanMiner.operator.classification;

import com.rapidminer.operator.OperatorDescription;

/**
 *
 * This operator classify documents according to topics using a Naive Bayes classifier.
 * The default model was trained on mini Sogou news dataset (12k, 8 categories):
 * https://github.com/lijqhs/text-classification-cn
 *
 * @author joeyhaohao
 */

public class DocumentClassification extends AbstractClassificationOperator {

    public DocumentClassification(OperatorDescription description) {
        super(description, "data/model/classification/sogou_8cat.ser");
    }

}
