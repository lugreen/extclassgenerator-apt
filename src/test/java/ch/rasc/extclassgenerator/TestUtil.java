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

import com.google.common.io.Files;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.misc.IOUtils;


import javax.tools.JavaFileObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class TestUtil {
	static String code = "";
//	static String getGeneratedSourcePath(String modelName, boolean debug,boolean apiWithQuotes){
//		String path = GeneratorTestUtil.class.getResource("/generator_/"
//				+ modelName + "ExtJs5" + (apiWithQuotes ? "Q" : "") + ".json").getPath();
//		return  path;
//	}
	static String getGeneratedSourcePath(String modelName, boolean debug,boolean apiWithQuotes){
		String path = GeneratorTestUtil.class.getResource("/generator/"
				+ modelName + ".js").getPath();
		return  path;
	}
	private static void compareModelString(String expectedValue, String value,
										   boolean debug) {
		if (debug) {
			assertThat(value.replaceAll("\\r?\\n", "\n"))
					.isEqualTo(expectedValue.replaceAll("\\r?\\n", "\n"));
		}
		else {
			assertThat(value.replace(" ", ""))
					.isEqualTo(expectedValue.replaceAll("\\r?\\n", "").replace(" ", ""));
		}
	}
	static void testCompile(String modelName, String javaSourcePath) throws IOException {
		JavaFileObject javaSource = JavaFileObjects.forResource(javaSourcePath);
		Object[] options = {"-Agone=nowhere"};
		JavaFileObject[] files = {javaSource};
		Compilation compilation  =javac()
						.withOptions(options)
						.withProcessors(new ClassAnnotationProcessor())
						.compile(files);
		CompilationSubject.assertThat(compilation).succeeded(); //编译通过
		//TODO 获取生成的模型代码
		String generatedModelSource=ModelGenerator.aaa;

		//比较生成的源码和
		String expectFile = getGeneratedSourcePath(modelName, true, false);
		File f = new File(expectFile);
		List<String> expectedValue = Files.readLines(f, Charset.forName("UTF-8"));
		StringBuilder sb=new StringBuilder();
		expectedValue.forEach(
				line->
				sb.append(line+"\n")
		);
		String s=sb.toString();
		if (generatedModelSource.equals("")) {
			return;
		}
		generatedModelSource += "\n";
		compareModelString(s, generatedModelSource, true);
	}

}
