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

public class ModelGeneratorBeansWithAssociationTest {


	@Test
	public void testBook() {
		TestUtil.doCompileTest("Book");
	}

	@Test
	public void testAuthor() {
		TestUtil.doCompileTest("Author");
	}

	@Test
	public void testCar() {
		TestUtil.doCompileTest("Car");
	}

	@Test
	public void testAutoCar() {
		TestUtil.doCompileTest("AutoCar");
	}

	@Test
	public void testBeanWithValidation() {
		TestUtil.doCompileTest("BeanWithValidation");
	}


//	@Test
//	public void testAddress() {
//		ModelGeneratorBeansWithAssociationTest.doCompileTest("Address");
//	}
//
//	@Test
//	public void testAssociatedClass() {
//		doCompileTest("AssociatedClass");
//	}
//
//	@Test
//	public void doCompileTest() {
//		doCompileTest("Base");
//	}
//
//	@Test
//	public void testBeanWithAnnotations() {
//		doCompileTest("BeanWithAnnotations");
//	}
//
//	@Test
//	public void testBeanWithAnnotations2() {
//		doCompileTest("BeanWithAnnotations2");
//	}
//
//	@Test
//	public void testBeanWithAnnotations3() {
//		doCompileTest("BeanWithAnnotations3");
//	}
//
//	@Test
//	public void testBeanWithAnnotationsDisablePaging() {
//		doCompileTest("BeanWithAnnotationsDisablePaging");
//	}
//
//	@Test
//	public void testBeanWithCustomType() {
//		doCompileTest("BeanWithCustomType");
//	}
//
//	@Test
//	public void testBeanWithGenericValidation() {
//		doCompileTest("BeanWithGenericValidation");
//	}
//
//	@Test
//	public void testBeanWithoutAnnotations() {
//		doCompileTest("BeanWithoutAnnotations");
//	}
//
//	@Test
//	public void testBookHasMany() {
//		doCompileTest("BookHasMany");
//	}
//
//	@Test
//	public void testBookWithOneAuthor() {
//		doCompileTest("BookWithOneAuthor");
//	}
//
//	@Test
//	public void testEmployee() {
//		doCompileTest("Employee");
//	}
//
//	@Test
//	public void testEmployeeWithInstanceName() {
//		doCompileTest("EmployeeWithInstanceName");
//	}
//
//	@Test
//	public void testOrder() {
//		doCompileTest("Order");
//	}
//
//	@Test
//	public void testPartialApi() {
//		doCompileTest("PartialApi");
//	}
//
//	@Test
//	public void testPos() {
//		doCompileTest("Pos");
//	}
//
//	@Test
//	public void testReadWrite() {
//		doCompileTest("ReadWrite");
//	}
//
//	@Test
//	public void testTwoHasOneClass() {
//		doCompileTest("TwoHasOneClass");
//	}
//
//	@Test
//	public void testUser() {
//		doCompileTest("User");
//	}
//
//	@Test
//	public void testUserClass() {
//		doCompileTest("UserClass");
//	}
//
//	@Test
//	public void testUserDataOptionsAll() {
//		doCompileTest("UserDataOptionsAll");
//	}
//
//	@Test
//	public void testUserDataOptionsAllAndPartial() {
//		doCompileTest("UserDataOptionsAllAndPartial");
//	}
//
//	@Test
//	public void testUserDataOptionsPartial() {
//		doCompileTest("UserDataOptionsPartial");
//	}
//

//	@Test
//	public void testAuthor() {
//		GeneratorTestUtil.testWriteModel(Author.class, "Author");
//	}
//
//	@Test
//	public void testCar() {
//		GeneratorTestUtil.testWriteModel(Car.class, "Car");
//	}
//
//	@Test
//	public void testAutoCar() {
//		GeneratorTestUtil.testWriteModel(AutoCar.class, "AutoCar");
//	}
//
//	@Test
//	public void testReadWrite() {
//		GeneratorTestUtil.testWriteModel(ReadWrite.class, "ReadWrite");
//	}
//
//	@Test
//	public void testBookWithOneAuthor() {
//		GeneratorTestUtil.testWriteModel(BookWithOneAuthor.class, "BookWithOneAuthor");
//	}
//
//	@Test
//	public void testBookHasMany() throws IOException {
//		MockHttpServletResponse response = new MockHttpServletResponse();
//		ModelGenerator.writeModel(new MockHttpServletRequest(), response,
//				BookHasMany.class, OutputFormat.EXTJS5, false);
//		GeneratorTestUtil.compareExtJs5Code("BookHasMany", response.getContentAsString(),
//				false, false);
//	}
//
//	@Test
//	public void testOrder() {
//		GeneratorTestUtil.testWriteModel(Order.class, "Order");
//	}
//
//	@Test
//	public void testPos() {
//		GeneratorTestUtil.testWriteModel(Pos.class, "Pos");
//	}
//
//	@Test
//	public void testEmployee() {
//		GeneratorTestUtil.testWriteModel(Employee.class, "Employee");
//	}
//
//	@Test
//	public void testEmployeeWithInstanceName() {
//		GeneratorTestUtil.testWriteModel(EmployeeWithInstanceName.class,
//				"EmployeeWithInstanceName");
//	}
//
//	@Test
//	public void testAddress() {
//		GeneratorTestUtil.testWriteModel(Address.class, "Address");
//	}
//
//	@Test
//	public void testTwoHasOneClass() {
//		GeneratorTestUtil.testWriteModel(TwoHasOneClass.class, "TwoHasOneClass");
//	}

}
