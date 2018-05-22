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

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Before;
import org.junit.Test;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;

import ch.rasc.extclassgenerator.bean.Address;
import ch.rasc.extclassgenerator.bean.Author;
import ch.rasc.extclassgenerator.bean.AutoCar;
import ch.rasc.extclassgenerator.bean.Book;
import ch.rasc.extclassgenerator.bean.BookHasMany;
import ch.rasc.extclassgenerator.bean.BookWithOneAuthor;
import ch.rasc.extclassgenerator.bean.Car;
import ch.rasc.extclassgenerator.bean.Employee;
import ch.rasc.extclassgenerator.bean.EmployeeWithInstanceName;
import ch.rasc.extclassgenerator.bean.Order;
import ch.rasc.extclassgenerator.bean.Pos;
import ch.rasc.extclassgenerator.bean.ReadWrite;
import ch.rasc.extclassgenerator.bean.TwoHasOneClass;

import javax.tools.JavaFileObject;

public class ModelGeneratorBeansWithAssociationTest {

//	@Before
//	public void clearCaches() {
//		ModelGenerator.clearCaches();
//	}

	public void testBase(String javaName) {
		try {
			String path = javaName + ".java";
			TestUtil.testCompile(javaName, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBook() {
		testBase("Book");
	}
	@Test
	public void testAddress() {
		testBase("Address");
	}

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
