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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import ch.rasc.extclassgenerator.ModelBean;
import ch.rasc.extclassgenerator.OutputConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.rasc.extclassgenerator.association.AbstractAssociation;
import ch.rasc.extclassgenerator.validation.AbstractValidation;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Generator for creating ExtJS and Touch Model objects (JS code) based on a provided
 * class or {@link ModelBean}.
 */
public abstract class ModelGenerator {
	static String aaa = "";


	public static String generateJavascript(TypeElement typeElement, Elements elementUtil, Types types, OutputConfig outputConfig) {
		ModelBean model = createModel(typeElement, elementUtil, types, outputConfig);
		String a = generateJavascript(model, outputConfig);
		aaa = a;
		return a;
	}

	public static ModelBean createModel(
			TypeElement typeElement, Elements elementUtil, Types types, final OutputConfig outputConfig) {
		Model modelAnnotation = typeElement.getAnnotation(Model.class);
		final ModelBean model = new ModelBean();
		if (modelAnnotation != null && Util.hasText(modelAnnotation.value())) {
			model.setName(modelAnnotation.value());
		} else {
			model.setName(elementUtil.getPackageOf(typeElement).toString() + "." + typeElement.getSimpleName().toString());
		}
		if (modelAnnotation != null) {
			model.setAutodetectTypes(modelAnnotation.autodetectTypes());
			model.setExtend(modelAnnotation.extend());
			model.setIdProperty(modelAnnotation.idProperty());
			model.setVersionProperty(trimToNull(modelAnnotation.versionProperty()));
			model.setPaging(modelAnnotation.paging());
			model.setDisablePagingParameters(modelAnnotation.disablePagingParameters());
			model.setCreateMethod(trimToNull(modelAnnotation.createMethod()));
			model.setReadMethod(trimToNull(modelAnnotation.readMethod()));
			model.setUpdateMethod(trimToNull(modelAnnotation.updateMethod()));
			model.setDestroyMethod(trimToNull(modelAnnotation.destroyMethod()));
			model.setMessageProperty(trimToNull(modelAnnotation.messageProperty()));
			model.setWriter(trimToNull(modelAnnotation.writer()));
			model.setReader(trimToNull(modelAnnotation.reader()));
			model.setSuccessProperty(trimToNull(modelAnnotation.successProperty()));
			model.setTotalProperty(trimToNull(modelAnnotation.totalProperty()));
			model.setRootProperty(trimToNull(modelAnnotation.rootProperty()));
			model.setWriteAllFields(modelAnnotation.writeAllFields());
			model.setAllDataOptions(new AllDataOptionsBean(modelAnnotation.allDataOptions()));
			model.setPartialDataOptions(new PartialDataOptionsBean(modelAnnotation.partialDataOptions()));
			model.setIdentifier(trimToNull(modelAnnotation.identifier()));
			String clientIdProperty = trimToNull(modelAnnotation.clientIdProperty());
			if (Util.hasText(clientIdProperty)) {
				model.setClientIdProperty(clientIdProperty);
				model.setClientIdPropertyAddToWriter(true);
			} else {
				model.setClientIdProperty(null);
				model.setClientIdPropertyAddToWriter(false);
			}
			if (modelAnnotation.hasMany() != null && modelAnnotation.hasMany().length > 0
					&& Util.hasText(modelAnnotation.hasMany()[0])) {
				model.setHasMany(modelAnnotation.hasMany());
			}
		}
		final Set<String> fields = new HashSet<>();
		final Set<String> readMethods = new HashSet<>();
		Set<ModelField> modelFieldsOnType = new HashSet<ModelField>();
		List<Element> list = (List<Element>) elementUtil.getAllMembers(typeElement);
		for (Element e : list) {
			if (e.getKind() == ElementKind.FIELD) {
				VariableElement field = (VariableElement) e;
//				if (!fields.contains(field.getSimpleName()) && (field
//						.getAnnotation(ModelField.class) != null
//						|| field.getAnnotation(ModelAssociation.class) != null
//						|| (field.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)
//						|| readMethods.contains(field.getSimpleName()))
//						&& field.getAnnotation(JsonIgnore.class) == null)) {
//					fields.add(field.getSimpleName() + "");
//					createModelBean(model, field, outputConfig, typeElement, elementUtil, types);
//				}
				if (!fields.contains(field.getSimpleName()) && (field
						.getAnnotation(ModelField.class) != null
						|| field.getAnnotation(ModelAssociation.class) != null
						|| (field.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)
						|| readMethods.contains(field.getSimpleName()))
						&& field.getAnnotation(JsonIgnore.class) == null)) {
					fields.add(field.getSimpleName() + "");
					createModelBean(model, field, outputConfig, typeElement, elementUtil, types);
				}
			}else if (e.getKind() == ElementKind.METHOD) {
				Element method = e;
//				if ((method.getAnnotation(ModelField.class) != null
//						|| method.getAnnotation(ModelAssociation.class) != null)
//						&& method.getAnnotation(JsonIgnore.class) == null) {
//					createModelBean(model, method, outputConfig, typeElement, elementUtil, types);
//				}
				if ((method.getAnnotation(ModelField.class) != null
						|| method.getAnnotation(ModelAssociation.class) != null)
						&& method.getAnnotation(JsonIgnore.class) == null) {
					createModelBean(model, method, outputConfig, typeElement, elementUtil, types);
				}
			}
		}

//		Collections.sort(candidateMethods, new Comparator<Element>() {
//			@Override
//			public int compare(Element o1, Element o2) {
//				return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
//			}
//		});
//
//		for (Element method : candidateMethods) {
//			createModelBean(model, method, outputConfig, typeElement, elementUtil, types);
//		}

		ModelField[] modelFields = typeElement.getAnnotationsByType(ModelField.class);
		for (ModelField m : modelFields) {
			modelFieldsOnType.add(m);
		}
		for (ModelField modelField : modelFieldsOnType) {
			if (Util.hasText(modelField.value())) {
				ModelFieldBean modelFieldBean;
				if (Util.hasText(modelField.customType())) {
					modelFieldBean = new ModelFieldBean(modelField.value(), modelField.customType());
				} else {
					modelFieldBean = new ModelFieldBean(modelField.value(), modelField.type());
				}
				updateModelFieldBean(modelFieldBean, modelField);
				model.addField(modelFieldBean);
			}
		}

		ModelAssociation[] modelAssociationsOnType = typeElement.getAnnotationsByType(ModelAssociation.class);
		for (ModelAssociation modelAssociationAnnotation : modelAssociationsOnType) {
			AbstractAssociation modelAssociation = AbstractAssociation.createAssociation(modelAssociationAnnotation);
			if (modelAssociation != null) {
				model.addAssociation(modelAssociation);
			}
		}

		ModelValidation[] modelValidationsOnType = typeElement.getAnnotationsByType(ModelValidation.class);
		for (ModelValidation modelValidationAnnotation : modelValidationsOnType) {
			AbstractValidation modelValidation = AbstractValidation.createValidation(
					modelValidationAnnotation.propertyName(), modelValidationAnnotation, outputConfig.getIncludeValidation());
			if (modelValidation != null) {
				model.addValidation(modelValidation);
			}
		}

		return model;
	}

	private static void createModelBean(ModelBean model,
										Method method, OutputConfig outputConfig) {
	}

	private static void createModelBean(ModelBean model,
										Field field, OutputConfig outputConfig) {
	}


//	private static void createModelBean_(ModelBean model, Element element, OutputConfig outputConfig) {
//		Class<?> javaType = null;
//		String name = null;
//		Class<?> declaringClass = null;
//
//		if (element.getKind() == ElementKind.FIELD) {
//			VariableElement field = (VariableElement) element;
//			try {
//				javaType = Class.forName(field.asType() + "");
//			} catch (ClassNotFoundException e1) {
//				String t = field.asType() + "";
//				switch (t) {
//					case "int":
//						javaType = int.class;
//						break;
//					case "boolean":
//						javaType = boolean.class;
//						break;
//					case "float":
//						javaType = float.class;
//						break;
//					case "double":
//						javaType = double.class;
//						break;
//					case "byte":
//						javaType = byte.class;
//						break;
//					case "long":
//						javaType = long.class;
//						break;
//					case "char":
//						javaType = char.class;
//						break;
//					case "short":
//						javaType = short.class;
//						break;
//					default:
//						if (t.endsWith(">")) {
//							t = t.substring(0, t.indexOf("<"));
//						}
//						try {
//							javaType = Class.forName(t);
//						} catch (ClassNotFoundException e) {
//							javaType = String.class;
//						}
//				}
//
//			}
//			name = field.getSimpleName() + "";
////			declaringClass = field.getDeclaringClass();
//		} else if (element.getKind() == ElementKind.METHOD) {
////			Method method = (Method) accessibleObject;
////
////			javaType = method.getReturnType();
////			if (javaType.equals(Void.TYPE)) {
////				return;
////			}
////
////			if (method.getName().startsWith("get")) {
////				name = Util.uncapitalize(method.getName().substring(3));
////			} else if (method.getName().startsWith("is")) {
////				name = Util.uncapitalize(method.getName().substring(2));
////			} else {
////				name = method.getName();
////			}
////
////			declaringClass = method.getDeclaringClass();
//		}
//
//		ModelType modelType = null;
//		if (model.isAutodetectTypes()) {
//			for (ModelType mt : ModelType.values()) {
//				if (mt.supports(javaType)) {
//					modelType = mt;
//					break;
//				}
//			}
//		} else {
//			modelType = ModelType.AUTO;
//		}
//
//		ModelFieldBean modelFieldBean = null;
//
//		ModelField modelFieldAnnotation = element.getAnnotation(ModelField.class);
//		if (modelFieldAnnotation != null) {
//
//			if (Util.hasText(modelFieldAnnotation.value())) {
//				name = modelFieldAnnotation.value();
//			}
//
//			if (Util.hasText(modelFieldAnnotation.customType())) {
//				modelFieldBean = new ModelFieldBean(name, modelFieldAnnotation.customType());
//			} else {
//				ModelType type = null;
//				if (modelFieldAnnotation.type() != ModelType.NOT_SPECIFIED) {
//					type = modelFieldAnnotation.type();
//				} else {
//					type = modelType;
//				}
//
//				modelFieldBean = new ModelFieldBean(name, type);
//			}
//
//			updateModelFieldBean(modelFieldBean, modelFieldAnnotation);
//			model.addField(modelFieldBean);
//		} else {
//			if (modelType != null) {
//				modelFieldBean = new ModelFieldBean(name, modelType);
//				model.addField(modelFieldBean);
//			}
//		}
//
//		ModelId modelIdAnnotation = element.getAnnotation(ModelId.class);
//		if (modelIdAnnotation != null) {
//			model.setIdProperty(name);
//		}
//
//		ModelClientId modelClientId = element.getAnnotation(ModelClientId.class);
//		if (modelClientId != null) {
//			model.setClientIdProperty(name);
//			model.setClientIdPropertyAddToWriter(modelClientId.configureWriter());
//		}
//
//		ModelVersion modelVersion = element.getAnnotation(ModelVersion.class);
//		if (modelVersion != null) {
//			model.setVersionProperty(name);
//		}
//
//		ModelAssociation modelAssociationAnnotation = element.getAnnotation(ModelAssociation.class);
//		if (modelAssociationAnnotation != null) {
//			model.addAssociation(AbstractAssociation.createAssociation(modelAssociationAnnotation, model, javaType, declaringClass, name));
//		}
//
//		if (modelFieldBean != null
//				&& outputConfig.getIncludeValidation() != IncludeValidation.NONE) {
//
////			Set<ModelValidation> modelValidationAnnotations = AnnotationUtils
////					.getRepeatableAnnotations(accessibleObject, ModelValidation.class,
////							ModelValidation.class);
////			if (!modelValidationAnnotations.isEmpty()) {
////				for (ModelValidation modelValidationAnnotation : modelValidationAnnotations) {
////					AbstractValidation modelValidation = AbstractValidation
////							.createValidation(name, modelValidationAnnotation,
////									outputConfig.getIncludeValidation());
////					if (modelValidation != null) {
////						model.addValidation(modelValidation);
////					}
////				}
////			} else {
////				Annotation[] fieldAnnotations = accessibleObject.getAnnotations();
////
////				for (Annotation fieldAnnotation : fieldAnnotations) {
////					AbstractValidation.addValidationToModel(model, modelFieldBean,
////							fieldAnnotation, outputConfig);
////				}
////
////				if (accessibleObject instanceof Field) {
////					PropertyDescriptor pd = BeanUtils
////							.getPropertyDescriptor(declaringClass, name);
////					if (pd != null) {
////						if (pd.getReadMethod() != null) {
////							for (Annotation readMethodAnnotation : pd.getReadMethod()
////									.getAnnotations()) {
////								AbstractValidation.addValidationToModel(model,
////										modelFieldBean, readMethodAnnotation,
////										outputConfig);
////							}
////						}
////
////						if (pd.getWriteMethod() != null) {
////							for (Annotation writeMethodAnnotation : pd.getWriteMethod()
////									.getAnnotations()) {
////								AbstractValidation.addValidationToModel(model,
////										modelFieldBean, writeMethodAnnotation,
////										outputConfig);
////							}
////						}
////					}
////				}
////			}
//		}
//
//	}


	private static void createModelBean(ModelBean model, Element element, OutputConfig outputConfig,
										Element typeElement, Elements elementUtil, Types types) {
//		Element javaType = null,field = null;
		TypeMirror javaType = null;Element field = null;
		String name = null;
		Element declaringClass = null;
		boolean isField = false;
		if (element.getKind() == ElementKind.FIELD) {
			field = (VariableElement) element;
			isField = true;
//			javaType = types.asElement(field.asType());
			javaType = field.asType();
			name = field.getSimpleName() + "";
			declaringClass = typeElement;
		}else if (element.getKind() == ElementKind.METHOD) {
			isField = false;
			field = element;
//			javaType = types.asElement(((Type.MethodType)field.asType()).restype);
			javaType = ((Type.MethodType)field.asType()).restype;
			if (javaType == null) {
				return;
			}
			if ((field.getSimpleName() + "").startsWith("get")) {
				name = StringUtils.uncapitalize((field.getSimpleName() + "").substring(3));
			} else if ((field.getSimpleName() + "").startsWith("is")) {
				name = StringUtils.uncapitalize((field.getSimpleName() + "").substring(2));
			} else {
				name = field.getSimpleName() + "";
			}
			declaringClass = typeElement;
		}

		ModelType modelType = null;
		if (model.isAutodetectTypes()) {
			String fieldStr = "";
			if (isField) {
				if (javaType!=null) {
					fieldStr = javaType + "";
				}else {
					fieldStr = field.asType() + "";
					if (fieldStr.endsWith(")")) {
						fieldStr = fieldStr.substring((fieldStr.lastIndexOf("::") + 3), fieldStr.length() - 1);
					}
				}
			}else {
				fieldStr = javaType + "";
			}
			String type = ModelType_.getType(fieldStr);
			switch (type) {
				case "int":
					modelType = ModelType.INTEGER;
					break;
				case "string":
					modelType = ModelType.STRING;
					break;
				case "boolean":
					modelType = ModelType.BOOLEAN;
					break;
				case "date":
					modelType = ModelType.DATE;
					break;
				case "float":
					modelType = ModelType.FLOAT;
					break;
			}
		} else {
			modelType = ModelType.AUTO;
		}

		ModelFieldBean modelFieldBean = null;

		ModelField modelFieldAnnotation = element.getAnnotation(ModelField.class);
		if (modelFieldAnnotation != null) {

			if (Util.hasText(modelFieldAnnotation.value())) {
				name = modelFieldAnnotation.value();
			}

			if (Util.hasText(modelFieldAnnotation.customType())) {
				modelFieldBean = new ModelFieldBean(name, modelFieldAnnotation.customType());
			} else {
				ModelType type = null;
				if (modelFieldAnnotation.type() != ModelType.NOT_SPECIFIED) {
					type = modelFieldAnnotation.type();
				} else {
					type = modelType;
				}

				modelFieldBean = new ModelFieldBean(name, type);
			}

			updateModelFieldBean(modelFieldBean, modelFieldAnnotation);
			model.addField(modelFieldBean);
		} else {
			if (modelType != null) {
				modelFieldBean = new ModelFieldBean(name, modelType);
				model.addField(modelFieldBean);
			}
		}

		ModelId modelIdAnnotation = element.getAnnotation(ModelId.class);
		if (modelIdAnnotation != null) {
			model.setIdProperty(name);
		}

		ModelClientId modelClientId = element.getAnnotation(ModelClientId.class);
		if (modelClientId != null) {
			model.setClientIdProperty(name);
			model.setClientIdPropertyAddToWriter(modelClientId.configureWriter());
		}

		ModelVersion modelVersion = element.getAnnotation(ModelVersion.class);
		if (modelVersion != null) {
			model.setVersionProperty(name);
		}

		ModelAssociation modelAssociationAnnotation = element.getAnnotation(ModelAssociation.class);
		if (modelAssociationAnnotation != null) {
			model.addAssociation(AbstractAssociation.createAssociation(modelAssociationAnnotation, model, javaType, declaringClass, name, types,elementUtil));
		}

		if (modelFieldBean != null
				&& outputConfig.getIncludeValidation() != IncludeValidation.NONE) {

//			Set<ModelValidation> modelValidationAnnotations = AnnotationUtils
//					.getRepeatableAnnotations(accessibleObject, ModelValidation.class,
//							ModelValidation.class);
			ModelValidation[] modelValidationAnnotations = element.getAnnotationsByType(ModelValidation.class);
			if (modelValidationAnnotations.length > 0) {
				for (ModelValidation modelValidationAnnotation : modelValidationAnnotations) {
					AbstractValidation modelValidation = AbstractValidation
							.createValidation(name, modelValidationAnnotation,
									outputConfig.getIncludeValidation());
					if (modelValidation != null) {
						model.addValidation(modelValidation);
					}
				}
			}
 			else {
				List<AnnotationMirror> list = (List<AnnotationMirror>) element.getAnnotationMirrors();
				List<Annotation> fieldAnnotations = new ArrayList<>();

				for (AnnotationMirror annotationMirror : list) {
					try {
						Class c = Class.forName(annotationMirror.getAnnotationType().toString());
						fieldAnnotations.add(element.getAnnotation(c));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				for (Annotation annotation: fieldAnnotations) {
					AbstractValidation.addValidationToModel(model, modelFieldBean, annotation, outputConfig);
				}
				if (element.getKind() == ElementKind.FIELD) {
//					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(element, name);
//					if (pd != null) {
//						if (pd.getReadMethod() != null) {
//							for (Annotation readMethodAnnotation : pd.getReadMethod()
//									.getAnnotations()) {
//								AbstractValidation.addValidationToModel(model,
//										modelFieldBean, readMethodAnnotation,
//										outputConfig);
//							}
//						}
//
//						if (pd.getWriteMethod() != null) {
//							for (Annotation writeMethodAnnotation : pd.getWriteMethod()
//									.getAnnotations()) {
//								AbstractValidation.addValidationToModel(model,
//										modelFieldBean, writeMethodAnnotation,
//										outputConfig);
//							}
//						}
//					}
				}
			}
		}

	}


	private static void updateModelFieldBean(ModelFieldBean modelFieldBean,
											 ModelField modelFieldAnnotation) {

		ModelType type = modelFieldBean.getModelType();

		if (Util.hasText(modelFieldAnnotation.dateFormat())
				&& type == ModelType.DATE) {
			modelFieldBean.setDateFormat(modelFieldAnnotation.dateFormat());
		}

		String defaultValue = modelFieldAnnotation.defaultValue();
		if (Util.hasText(defaultValue)) {
			if (ModelField.DEFAULTVALUE_UNDEFINED.equals(defaultValue)) {
				modelFieldBean.setDefaultValue(ModelField.DEFAULTVALUE_UNDEFINED);
			} else {
				if (type == ModelType.BOOLEAN) {
					modelFieldBean.setDefaultValue(Boolean.valueOf(defaultValue));
				} else if (type == ModelType.INTEGER) {
					modelFieldBean.setDefaultValue(Long.valueOf(defaultValue));
				} else if (type == ModelType.FLOAT || type == ModelType.NUMBER) {
					modelFieldBean.setDefaultValue(Double.valueOf(defaultValue));
				} else {
					modelFieldBean.setDefaultValue("\"" + defaultValue + "\"");
				}
			}
		}

		if ((modelFieldAnnotation.useNull() || modelFieldAnnotation.allowNull())
				&& (type == ModelType.INTEGER || type == ModelType.FLOAT
				|| type == ModelType.NUMBER || type == ModelType.STRING
				|| type == ModelType.BOOLEAN)) {
			modelFieldBean.setAllowNull(Boolean.TRUE);
		}

		if (!modelFieldAnnotation.allowBlank()) {
			modelFieldBean.setAllowBlank(Boolean.FALSE);
		}

		if (modelFieldAnnotation.unique()) {
			modelFieldBean.setUnique(Boolean.TRUE);
		}

		modelFieldBean.setMapping(trimToNull(modelFieldAnnotation.mapping()));

		if (!modelFieldAnnotation.persist()) {
			modelFieldBean.setPersist(Boolean.FALSE);
		}

		if (modelFieldAnnotation.critical()) {
			modelFieldBean.setCritical(Boolean.TRUE);
		}

		modelFieldBean.setConvert(trimToNull(modelFieldAnnotation.convert()));

		modelFieldBean.setCalculate(trimToNull(modelFieldAnnotation.calculate()));

		List<String> depends = Arrays.asList(modelFieldAnnotation.depends());
		if (!depends.isEmpty()) {
			modelFieldBean.setDepends(depends);
		} else {
			modelFieldBean.setDepends(null);
		}

		ReferenceBean reference = new ReferenceBean(modelFieldAnnotation.reference());
		if (reference.hasAnyProperties()) {
			if (reference.typeOnly()) {
				modelFieldBean.setReference(reference.getType());
			} else {
				modelFieldBean.setReference(reference);
			}
		}
	}

	public static String generateJavascript(ModelBean model, OutputConfig outputConfig) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

		if (!outputConfig.isSurroundApiWithQuotes()) {
			if (outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
				mapper.addMixInAnnotations(ProxyObject.class,
						ProxyObjectWithoutApiQuotesExtJs5Mixin.class);
			} else {
				mapper.addMixInAnnotations(ProxyObject.class,
						ProxyObjectWithoutApiQuotesMixin.class);
			}
			mapper.addMixInAnnotations(ApiObject.class, ApiObjectMixin.class);
		} else {
			if (outputConfig.getOutputFormat() != OutputFormat.EXTJS5) {
				mapper.addMixInAnnotations(ProxyObject.class,
						ProxyObjectWithApiQuotesMixin.class);
			}
		}

		Map<String, Object> modelObject = new LinkedHashMap<>();
		modelObject.put("extend", model.getExtend());

		if (!model.getAssociations().isEmpty()) {
			Set<String> usesClasses = new HashSet<>();
			for (AbstractAssociation association : model.getAssociations()) {
				usesClasses.add(association.getModel());
			}

			usesClasses.remove(model.getName());

			if (!usesClasses.isEmpty()) {
				modelObject.put("uses", usesClasses);
			}
		}

		Map<String, Object> configObject = new LinkedHashMap<>();
		ProxyObject proxyObject = new ProxyObject(model, outputConfig);

		Map<String, ModelFieldBean> fields = model.getFields();
		Set<String> requires = new HashSet<>();

		if (!model.getValidations().isEmpty()
				&& outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
			requires = addValidatorsToField(fields, model.getValidations());
		}

		if (proxyObject.hasContent()
				&& outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
			requires.add("Ext.data.proxy.Direct");
		}

		if (Util.hasText(model.getIdentifier())
				&& outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
			if ("sequential".equals(model.getIdentifier())) {
				requires.add("Ext.data.identifier.Sequential");
			} else if ("uuid".equals(model.getIdentifier())) {
				requires.add("Ext.data.identifier.Uuid");
			} else if ("negative".equals(model.getIdentifier())) {
				requires.add("Ext.data.identifier.Negative");
			}
		}

		if (requires != null && !requires.isEmpty()) {
			configObject.put("requires", requires);
		}

		if (Util.hasText(model.getIdentifier())) {
			if (outputConfig.getOutputFormat() == OutputFormat.EXTJS5
					|| outputConfig.getOutputFormat() == OutputFormat.TOUCH2) {
				configObject.put("identifier", model.getIdentifier());
			} else {
				configObject.put("idgen", model.getIdentifier());
			}
		}

		if (Util.hasText(model.getIdProperty())
				&& !model.getIdProperty().equals("id")) {
			configObject.put("idProperty", model.getIdProperty());
		}

		if (outputConfig.getOutputFormat() == OutputFormat.EXTJS5
				&& Util.hasText(model.getVersionProperty())) {
			configObject.put("versionProperty", model.getVersionProperty());
		}

		if (Util.hasText(model.getClientIdProperty())) {

			if (outputConfig.getOutputFormat() == OutputFormat.EXTJS5
					|| outputConfig.getOutputFormat() == OutputFormat.EXTJS4) {
				configObject.put("clientIdProperty", model.getClientIdProperty());
			} else if (outputConfig.getOutputFormat() == OutputFormat.TOUCH2
					&& !"clientId".equals(model.getClientIdProperty())) {
				configObject.put("clientIdProperty", model.getClientIdProperty());
			}
		}

		for (ModelFieldBean field : fields.values()) {
			field.updateTypes(outputConfig);
		}

		List<Object> fieldConfigObjects = new ArrayList<>();
		for (ModelFieldBean field : fields.values()) {
			if (field.hasOnlyName(outputConfig)) {
				fieldConfigObjects.add(field.getName());
			} else {
				fieldConfigObjects.add(field);
			}
		}
		configObject.put("fields", fieldConfigObjects);

		if (model.getHasMany() != null) {
			configObject.put("hasMany", model.getHasMany());
		}

		if (!model.getAssociations().isEmpty()) {
			configObject.put("associations", model.getAssociations());
//			List<AbstractAssociation > hasOne = new ArrayList<>();
//			List<AbstractAssociation > hasMany = new ArrayList<>();
//			List<AbstractAssociation > belongsTo = new ArrayList<>();
//			for (AbstractAssociation abstractAssociation : model.getAssociations()) {
//				switch (abstractAssociation.getType()) {
//					case "hasOne":
//						hasOne.add(abstractAssociation);
//						break;
//					case "hasMany":
//						hasMany.add(abstractAssociation);
//						break;
//					case "belongsTo":
//						belongsTo.add(abstractAssociation);
//						break;
//				}
//			}
//			configObject.put("hasOne", hasOne);
//			configObject.put("hasMany", hasMany);
//			configObject.put("belongsTo", belongsTo);
		}
		if (!model.getValidations().isEmpty()
				&& !(outputConfig.getOutputFormat() == OutputFormat.EXTJS5)) {
			configObject.put("validations", model.getValidations());
		}

		if (proxyObject.hasContent()) {
			configObject.put("proxy", proxyObject);
		}

		if (outputConfig.getOutputFormat() == OutputFormat.EXTJS4
				|| outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
			modelObject.putAll(configObject);
		} else {
			modelObject.put("config", configObject);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Ext.define(\"").append(model.getName()).append("\",");
		if (outputConfig.isDebug()) {
			sb.append("\r\n");
		}

		String configObjectString;
		Class<?> jsonView = JsonViews.ExtJS4.class;
		if (outputConfig.getOutputFormat() == OutputFormat.TOUCH2) {
			jsonView = JsonViews.Touch2.class;
		} else if (outputConfig.getOutputFormat() == OutputFormat.EXTJS5) {
			jsonView = JsonViews.ExtJS5.class;
		}

		try {
			if (outputConfig.isDebug()) {
				configObjectString = mapper.writerWithDefaultPrettyPrinter()
						.withView(jsonView).writeValueAsString(modelObject);
			} else {
				configObjectString = mapper.writerWithView(jsonView)
						.writeValueAsString(modelObject);
			}

		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		sb.append(configObjectString);
		sb.append(");");

		String result = sb.toString();

		if (outputConfig.isUseSingleQuotes()) {
			result = result.replace('"', '\'');
		}

		if (outputConfig.getLineEnding() == LineEnding.CRLF) {
			result = result.replaceAll("\r?\n", "\r\n");
		} else if (outputConfig.getLineEnding() == LineEnding.LF) {
			result = result.replaceAll("\r?\n", "\n");
		} else if (outputConfig.getLineEnding() == LineEnding.SYSTEM) {
			String lineSeparator = System.getProperty("line.separator");
			result = result.replaceAll("\r?\n", lineSeparator);
		}

		return result;
	}

	private static Set<String> addValidatorsToField(Map<String, ModelFieldBean> fields,
													List<AbstractValidation> validations) {

		Set<String> requires = new TreeSet<>();

		for (ModelFieldBean field : fields.values()) {
			for (AbstractValidation validation : validations) {
				if (field.getName().equals(validation.getField())) {
					List<AbstractValidation> validators = field.getValidators();
					if (validators == null) {
						validators = new ArrayList<>();
						field.setValidators(validators);
					}

					String validatorClass = getValidatorClass(validation.getType());
					if (validatorClass != null) {
						requires.add(validatorClass);
					}

					boolean alreadyExists = false;
					for (AbstractValidation validator : validators) {
						if (validation.getType().equals(validator.getType())) {
							alreadyExists = true;
							break;
						}
					}

					if (!alreadyExists) {
						validators.add(validation);
					}
				}
			}
		}

		return requires;
	}

	private static String getValidatorClass(String type) {
		if (type.equals("email")) {
			return "Ext.data.validator.Email";
		} else if (type.equals("exclusion")) {
			return "Ext.data.validator.Exclusion";
		} else if (type.equals("format")) {
			return "Ext.data.validator.Format";
		} else if (type.equals("inclusion")) {
			return "Ext.data.validator.Inclusion";
		} else if (type.equals("length")) {
			return "Ext.data.validator.Length";
		} else if (type.equals("presence")) {
			return "Ext.data.validator.Presence";
		} else if (type.equals("range")) {
			return "Ext.data.validator.Range";
		}
		return null;
	}

	static String trimToNull(String str) {
		String trimmedStr = Util.trimWhitespace(str);
		if (Util.hasLength(trimmedStr)) {
			return trimmedStr;
		}
		return null;
	}


}