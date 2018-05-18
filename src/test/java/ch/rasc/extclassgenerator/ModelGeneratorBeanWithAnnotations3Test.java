/**
 * Copyright 2013-2017 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.extclassgenerator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import ch.rasc.extclassgenerator.bean.BeanWithAnnotations3;

public class ModelGeneratorBeanWithAnnotations3Test {

	@Before
	public void clearCaches() {
		ModelGenerator.clearCaches();
	}

	@Test
	public void testWithQuotes() {
		GeneratorTestUtil.testGenerateJavascript(BeanWithAnnotations3.class,
				"BeanWithAnnotations3", true, IncludeValidation.NONE, false);
	}

	@Test
	public void testWithoutQuotes() {
		GeneratorTestUtil.testWriteModel(BeanWithAnnotations3.class,
				"BeanWithAnnotations3");
		GeneratorTestUtil.testGenerateJavascript(BeanWithAnnotations3.class,
				"BeanWithAnnotations3", false, IncludeValidation.NONE, false);
	}

	@Test
	public void testCreateModel() {
		ModelBean modelBean = ModelGenerator.createModel(BeanWithAnnotations3.class);
		assertThat(modelBean.getReadMethod()).isEqualTo("read");
		assertThat(modelBean.getCreateMethod()).isNull();
		assertThat(modelBean.getUpdateMethod()).isNull();
		assertThat(modelBean.getDestroyMethod()).isNull();
		assertThat(modelBean.getIdProperty()).isEqualTo("id");
		assertThat(modelBean.getVersionProperty()).isNull();
		assertThat(modelBean.isDisablePagingParameters()).isFalse();
		assertThat(modelBean.isPaging()).isFalse();
		assertThat(modelBean.getMessageProperty()).isEqualTo("theMessageProperty");
		assertThat(modelBean.getRootProperty()).isEqualTo("theRootProperty");
		assertThat(modelBean.getTotalProperty()).isEqualTo("theTotalProperty");
		assertThat(modelBean.getSuccessProperty()).isEqualTo("theSuccessProperty");
		assertThat(modelBean.getName()).isEqualTo("Sch.Bean3");
		assertThat(modelBean.getFields()).hasSize(2);
		assertThat(BeanWithAnnotations3.expectedFields).hasSize(2);

		for (ModelFieldBean expectedField : BeanWithAnnotations3.expectedFields) {
			ModelFieldBean field = modelBean.getFields().get(expectedField.getName());
			assertThat(field).isEqualToComparingFieldByField(expectedField);
		}
	}

}