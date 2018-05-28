/**
 * Copyright 2013-2017 the original author or authors.
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

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import org.springframework.util.StringUtils;

//@SupportedAnnotationTypes({"ch.rasc.extclassgenerator.Model", "ch.rasc.extclassgenerator.ModelField"})
@SupportedAnnotationTypes({"ch.rasc.extclassgenerator.Model"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"outputFormat", "debug", "includeValidation", "createBaseAndSubclass",
		"useSingleQuotes", "surroundApiWithQuotes", "lineEnding"})
@AutoService(Processor.class)
public class ClassAnnotationProcessor extends AbstractProcessor {

	private static final boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = false;

	private static final String OPTION_OUTPUTFORMAT = "outputFormat";
	private static final String OPTION_OUTPUTDIRECTORY= "outputDirectory";

	private static final String OPTION_DEBUG = "debug";

	private static final String OPTION_INCLUDEVALIDATION = "includeValidation";

	private static final String OPTION_CREATEBASEANDSUBCLASS = "createBaseAndSubclass";

	private static final String OPTION_USESINGLEQUOTES = "useSingleQuotes";

	private static final String OPTION_SURROUNDAPIWITHQUOTES = "surroundApiWithQuotes";

	private static final String OPTION_LINEENDING = "lineEnding";


	@Override
	public boolean process(Set<? extends TypeElement> annotations,
						   RoundEnvironment roundEnv) {

		this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
				"Running " + getClass().getSimpleName());
		this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
				"Running " + getClass().getSimpleName());

		if (roundEnv.processingOver() || annotations.size() == 0) {
			return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
		}

		if (roundEnv.getRootElements() == null || roundEnv.getRootElements().isEmpty()) {
			this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
					"No sources to process");
			return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
		}

		OutputConfig outputConfig = new OutputConfig();
		Map<String,String> optionsMap = this.processingEnv.getOptions();

		outputConfig.setDebug(!"false".equals(optionsMap.get(OPTION_DEBUG)));
		boolean createBaseAndSubclass = "true".equals(
				optionsMap.get(OPTION_CREATEBASEANDSUBCLASS));

//		String outputFormatString = optionsMap.get(OPTION_OUTPUTFORMAT);
		outputConfig.setOutputFormat(OutputFormat.EXTJS5);

		String includeValidationString = optionsMap.get(OPTION_INCLUDEVALIDATION);
		outputConfig.setIncludeValidation(IncludeValidation.BUILTIN);
		if (includeValidationString != null
				&& !includeValidationString.trim().isEmpty()) {
			if (IncludeValidation.ALL.name().equalsIgnoreCase(includeValidationString)) {
				outputConfig.setIncludeValidation(IncludeValidation.ALL);
			} else if (IncludeValidation.BUILTIN.name()
					.equalsIgnoreCase(includeValidationString)) {
				outputConfig.setIncludeValidation(IncludeValidation.BUILTIN);
			}
		}

		outputConfig.setUseSingleQuotes("true"
				.equals(optionsMap.get(OPTION_USESINGLEQUOTES)));
		outputConfig.setSurroundApiWithQuotes("true".equals(
				optionsMap.get(OPTION_SURROUNDAPIWITHQUOTES)));

		outputConfig.setLineEnding(LineEnding.SYSTEM);
		String lineEndingOption = optionsMap.get(OPTION_LINEENDING);
		if (lineEndingOption != null) {
			try {
				LineEnding lineEnding = LineEnding
						.valueOf(lineEndingOption.toUpperCase());
				outputConfig.setLineEnding(lineEnding);
			} catch (Exception e) {
				// ignore an invalid value
			}
		}

		for (TypeElement annotation : annotations) {
			Set<? extends Element> elements = roundEnv
					.getElementsAnnotatedWith(annotation);

			for (Element element : elements) {

				try {
					TypeElement typeElement = (TypeElement) element;
					Elements elementsUtil = this.processingEnv.getElementUtils();
					Types types = this.processingEnv.getTypeUtils();
					String packageName = elementsUtil.getPackageOf(typeElement).getQualifiedName().toString();
					String code = ModelGenerator.generateJavascript(typeElement, elementsUtil, types, outputConfig);
					System.out.println(code);
					Model modelAnnotation = element.getAnnotation(Model.class);
					String modelName = modelAnnotation.value();
					String fileName;
					String outPackageName = "";
//					if (modelName != null && !modelName.trim().isEmpty()) {
//						int lastDot = modelName.lastIndexOf('.');
//						if (lastDot != -1) {
//							fileName = modelName.substring(lastDot + 1);
//							int firstDot = modelName.indexOf('.');
//							if (firstDot < lastDot) {
//								outPackageName = modelName.substring(firstDot + 1,
//										lastDot);
//							}
//						} else {
//							fileName = modelName;
//						}
//					} else {
//					}
					fileName = typeElement.getSimpleName().toString();
					String outDirectory = optionsMap.get(OPTION_OUTPUTDIRECTORY);

					if(outDirectory!=null&&!outDirectory.isEmpty()){
						outPackageName = "";
						String outPackagePath = outPackageName.replaceAll("\\.", "/");
						Path outPath = Paths.get(outDirectory, outPackagePath);
						if(!outPath.toFile().exists()){
							outPath.toFile().mkdirs();
						}
						if (createBaseAndSubclass) {
							code = code.replaceFirst("(Ext.define\\([\"'].+?)([\"'],)",
									"$1Base$2");
							String baseFileName=fileName + "Base.js";
							File outBaseFile = new File(outPath.toString(), baseFileName);
							try (FileOutputStream outFileStream = new FileOutputStream(outBaseFile)){
								outFileStream.write(code.getBytes(StandardCharsets.UTF_8));
							}
							File outFile = new File(outPath.toString(), fileName+".js");
							String subClassCode = generateSubclassCode(typeElement,outputConfig);
							try (FileOutputStream outFileStream = new FileOutputStream(outFile)){
								outFileStream.write(subClassCode.getBytes(StandardCharsets.UTF_8));
							}
						} else {
							String n = fileName +
									"-validation_" + optionsMap.get(OPTION_INCLUDEVALIDATION) +
									(outputConfig.isSurroundApiWithQuotes() ? "-Q" : "") +
									".js";
							File outFile = new File(outPath.toString(), n);
							try (FileOutputStream outFileStream = new FileOutputStream(outFile)){
								outFileStream.write(code.getBytes(StandardCharsets.UTF_8));
							}
						}
					}else {
						fileName = typeElement.toString();
						FileObject fo = this.processingEnv.getFiler().createResource(
								StandardLocation.SOURCE_OUTPUT, outPackageName,
								fileName + ".js");
						try (OutputStream os = fo.openOutputStream()) {
							os.write(code.getBytes(StandardCharsets.UTF_8));
						}
//						this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
//								"unset option:"+OPTION_OUTPUTDIRECTORY+",gneerated model will not to write");
					}
				} catch (Exception e) {
					this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
							e.getMessage());
				}

			}
		}

		return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
	}
	private static String generateSubclassCode(TypeElement typeElement,OutputConfig outputConfig) {
		Model modelAnnotation = typeElement.getAnnotation(Model.class);

		String name;
		if (modelAnnotation != null && StringUtils.hasText(modelAnnotation.value())) {
			name = modelAnnotation.value();
		}
		else {
			name = typeElement.getQualifiedName().toString();
		}

		Map<String, Object> modelObject = new LinkedHashMap<String, Object>();
		modelObject.put("extend", name + "Base");

		StringBuilder sb = new StringBuilder(100);
		sb.append("Ext.define(\"").append(name).append("\",");
		if (outputConfig.isDebug()) {
			sb.append("\n");
		}

		String configObjectString;
		try {

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

			if (!outputConfig.isSurroundApiWithQuotes()) {
				if (outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
					mapper.addMixInAnnotations(ProxyObject.class,
							ProxyObjectWithoutApiQuotesExtJs5Mixin.class);
				}
				else {
					mapper.addMixInAnnotations(ProxyObject.class,
							ProxyObjectWithoutApiQuotesMixin.class);
				}
				mapper.addMixInAnnotations(ApiObject.class, ApiObjectMixin.class);
			}
			else {
				if (outputConfig.getOutputFormat() != OutputFormat.EXTJS5) {
					mapper.addMixInAnnotations(ProxyObject.class,
							ProxyObjectWithApiQuotesMixin.class);
				}
			}

			if (outputConfig.isDebug()) {
				configObjectString = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(modelObject);
			}
			else {
				configObjectString = mapper.writeValueAsString(modelObject);
			}

		}
		catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		}
		catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		sb.append(configObjectString);
		sb.append(");");

		if (outputConfig.isUseSingleQuotes()) {
			return sb.toString().replace('"', '\'');
		}

		return sb.toString();

	}
}
