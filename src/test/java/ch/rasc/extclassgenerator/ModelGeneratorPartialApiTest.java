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

public class ModelGeneratorPartialApiTest {
	//
//	@Before
//	public void clearCaches() {
//		ModelGenerator.clearCaches();
//	}
//
	@Test
	public void testWithQuotes() {
		TestUtil.doCompileTestQ("PartialApi");
//		GeneratorTestUtil.testGenerateJavascript(PartialApi.class, "PartialApi", true,
//				IncludeValidation.NONE, true);
	}

	@Test
	public void testWithoutQuotes() {
		TestUtil.doCompileTest("PartialApi");
//		GeneratorTestUtil.testGenerateJavascript(PartialApi.class, "PartialApi", false,
//				IncludeValidation.NONE, true);
	}
//
//	@Test
//	public void testCreateModel() {
//		ModelBean modelBean = ModelGenerator.createModel(PartialApi.class);
//		assertThat(modelBean.getReadMethod()).isEqualTo("read");
//		assertThat(modelBean.getCreateMethod()).isNull();
//		assertThat(modelBean.getUpdateMethod()).isNull();
//		assertThat(modelBean.getDestroyMethod()).isEqualTo("destroy");
//		assertThat(modelBean.getIdProperty()).isEqualTo("id");
//		assertThat(modelBean.getVersionProperty()).isNull();
//		assertThat(modelBean.isPaging()).isFalse();
//		assertThat(modelBean.getName()).isEqualTo("App.PartialApi");
//		assertThat(modelBean.getFields()).hasSize(2);
//		assertThat(PartialApi.expectedFields).hasSize(2);
//		assertThat(modelBean.getValidations()).isEmpty();
//
//		for (ModelFieldBean expectedField : PartialApi.expectedFields) {
//			ModelFieldBean field = modelBean.getFields().get(expectedField.getName());
//			assertThat(field).isEqualToComparingFieldByField(expectedField);
//		}
//	}
}
