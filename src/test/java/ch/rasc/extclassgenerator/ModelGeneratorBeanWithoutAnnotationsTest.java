/**
 * Copyright 2013-2017 Ralph Schaer <ralphschaer@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.extclassgenerator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelGeneratorBeanWithoutAnnotationsTest {
	//
//	@Before
//	public void clearCaches() {
//		ModelGenerator.clearCaches();
//	}
//
	@Test
	public void testWithoutQuotes() {
		TestUtil.doCompileTestQ("BeanWithoutAnnotations");
//		GeneratorTestUtil.testWriteModelBuiltinValidation(BeanWithoutAnnotations.class,
//				"BeanWithoutAnnotations");
//		GeneratorTestUtil.testGenerateJavascript(BeanWithoutAnnotations.class,
//				"BeanWithoutAnnotations", false, IncludeValidation.NONE, false);
	}
//
//	@Test
//	public void testCreateModel() {
//		ModelBean modelBean = ModelGenerator.createModel(BeanWithoutAnnotations.class);
//		assertThat(modelBean.getReadMethod()).isNull();
//		assertThat(modelBean.getCreateMethod()).isNull();
//		assertThat(modelBean.getUpdateMethod()).isNull();
//		assertThat(modelBean.getDestroyMethod()).isNull();
//		assertThat(modelBean.getIdProperty()).isNull();
//		assertThat(modelBean.getVersionProperty()).isNull();
//		assertThat(modelBean.isPaging()).isFalse();
//		assertThat(modelBean.getName())
//				.isEqualTo("ch.rasc.extclassgenerator.bean.BeanWithoutAnnotations");
//		assertThat(modelBean.getFields()).hasSize(29);
//		assertThat(BeanWithoutAnnotations.expectedFields).hasSize(29);
//
//		for (ModelFieldBean expectedField : BeanWithoutAnnotations.expectedFields) {
//			ModelFieldBean field = modelBean.getFields().get(expectedField.getName());
//			assertThat(field).isEqualToComparingFieldByField(expectedField);
//		}
//	}

}
