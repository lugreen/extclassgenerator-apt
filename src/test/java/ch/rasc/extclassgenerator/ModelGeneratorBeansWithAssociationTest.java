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
	public void testReadWrite() {
		TestUtil.doCompileTest("ReadWrite");
	}

	@Test
	public void testBookWithOneAuthor() {
		TestUtil.doCompileTest("BookWithOneAuthor");
	}

	@Test
	public void testBookHasMany() {
		TestUtil.doCompileTest("BookWithOneAuthor");
	}

	@Test
	public void testOrder() {
		TestUtil.doCompileTest("Order");
	}

	@Test
	public void testPos() {
		TestUtil.doCompileTest("Pos");
	}

	@Test
	public void testEmployee() {
		TestUtil.doCompileTest("Employee");
	}

	@Test
	public void testEmployeeWithInstanceName() {
		TestUtil.doCompileTest("EmployeeWithInstanceName");
	}

	@Test
	public void testAddress() {
		TestUtil.doCompileTest("Address");
	}

	@Test
	public void testTwoHasOneClass() {
		TestUtil.doCompileTest("TwoHasOneClass");
	}

}
