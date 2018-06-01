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

import com.google.common.io.Files;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class TestUtil {

	public static void doCompileTest(String javaName, boolean apiWithQuotes) {
		try {
			String path = javaName;
			if (apiWithQuotes) {
				TestUtil.testCompileQ(javaName, path);
			} else {
				TestUtil.testCompile(javaName, path);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void doCompileTestQ(String javaName) {
		doCompileTest(javaName, true);
	}

	public static void doCompileTest(String javaName) {
		doCompileTest(javaName, false);
	}

	static String getGeneratedSourcePath(String modelName, boolean apiWithQuotes, IncludeValidation includeValidation) {
		String name = "/generator/" + modelName +
				"-validation_" + includeValidation +
				(apiWithQuotes ? "-Q" : "") +
				".js";
		String path = GeneratorTestUtil.class.getResource(name).getPath();
		return path;
	}

	private static void compareModelString(String expectedValue, String value,
										   boolean debug) {
		if (debug) {
			assertThat(value.replaceAll("\\r?\\n", "\n"))
					.isEqualTo(expectedValue.replaceAll("\\r?\\n", "\n"));
		} else {
			assertThat(value.replace(" ", ""))
					.isEqualTo(expectedValue.replaceAll("\\r?\\n", "").replace(" ", ""));
		}
	}

	static void testCompileQ(String modelName, String javaSourcePath) throws IOException {
//		testCompile(modelName, javaSourcePath, IncludeValidation.NONE, true);
		testCompile(modelName, javaSourcePath, IncludeValidation.ALL, true);
//		testCompile(modelName, javaSourcePath, IncludeValidation.BUILTIN, true);
	}

	static void testCompile(String modelName, String javaSourcePath) throws IOException {
//		testCompile(modelName, javaSourcePath, IncludeValidation.NONE, false);
		testCompile(modelName, javaSourcePath, IncludeValidation.ALL, false);
//		testCompile(modelName, javaSourcePath, IncludeValidation.BUILTIN, false);
	}

	static void testCompile(String modelName, String javaSourcePath,
							IncludeValidation includeValidation, boolean apiWithQuotes) throws IOException {
		String p = TestUtil.class.getResource("/").getPath();
		String pathName = p + "../../../src/test/java/ch/rasc/extclassgenerator/bean/" + javaSourcePath + ".java";
		File file = new File(pathName);
		JavaFileObject javaSource = null;
		try {
			javaSource = JavaFileObjects.forResource(file.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
//		F:/pzz/extclassgenerator-apt/src/test/resources/generator
		Object[] options = {
				"-AoutputDirectory=D:/src",
				"-AincludeValidation=" + includeValidation,
				"-AsurroundApiWithQuotes=" + apiWithQuotes
		};
		JavaFileObject[] files = {javaSource};
		Compilation compilation = javac()
				.withOptions(options)
				.withProcessors(new ClassAnnotationProcessor())
				.compile(files);
		CompilationSubject.assertThat(compilation).succeeded(); //编译通过
		//TODO 获取生成的模型代码
		String generatedModelSource = ModelGenerator.code;

		//比较生成的源码和
		String expectFile = getGeneratedSourcePath(modelName, apiWithQuotes, includeValidation);
		File f = new File(expectFile);
		List<String> expectedValue = Files.readLines(f, Charset.forName("UTF-8"));
		StringBuilder sb = new StringBuilder();
		expectedValue.forEach(
				line ->
						sb.append(line + "\n")
		);
		String s = sb.toString();
		if (generatedModelSource.equals("")) {
			return;
		}
		generatedModelSource += "\n";
		generatedModelSource = generatedModelSource.substring(generatedModelSource.indexOf("*/") + 3);
		s = s.substring(s.indexOf("*/") + 3);
		compareModelString(s, generatedModelSource, true);
	}

}
