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
package ch.rasc.extclassgenerator.association;

import java.awt.*;
import java.awt.print.Book;
import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelAssociation;
import ch.rasc.extclassgenerator.ModelAssociationType;
import ch.rasc.extclassgenerator.ModelBean;
import ch.rasc.extclassgenerator.ModelGenerator;
import ch.rasc.extclassgenerator.ModelId;
import ch.rasc.extclassgenerator.Util;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;

/**
 * Base class for the association objects
 */
@JsonInclude(Include.NON_NULL)
public abstract class AbstractAssociation {

//	private static ProcessingEnvironment processingEnv;
//
//	@Override
//	public synchronized void init(ProcessingEnvironment processingEnv) {
//		AbstractAssociation.processingEnv = processingEnv;
//		super.init(processingEnv);
//	}

	private final String type;

	private final String model;

	private String associationKey;

	private String foreignKey;

	private String primaryKey;

	private String instanceName;

	/**
	 * Creates an instance of the AbstractAssociation. Sets {@link #getType()} and
	 * {@link #getModel()} to the provided parameters.
	 *
	 * @param type The type of the association.
	 * @param model The name of the model that is being associated with.
	 */
	public AbstractAssociation(String type, String model) {
		this.type = type;
		this.model = model;
	}

	public String getAssociationKey() {
		return this.associationKey;
	}

	/**
	 * The name of the property in the data to read the association from. Defaults to the
	 * name of the associated model.
	 * <p>
	 * Corresponds to the <a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.association.Association-cfg-associationKey"
	 * >associationKey</a> config property.
	 *
	 * @param associationKey name of the property in the json data
	 */
	public void setAssociationKey(String associationKey) {
		this.associationKey = associationKey;
	}

	public String getForeignKey() {
		return this.foreignKey;
	}

	/**
	 * The name of the foreign key on the associated model that links it to the owner
	 * model. Defaults to the lowercase name of the owner model + "_id" (HAS_MANY) or to
	 * the field name (BELONGS_TO, HAS_ONE) + "_id".
	 * <p>
	 * Corresponds to the <a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.association.HasMany-cfg-foreignKey"
	 * >foreignKey</a> config property.
	 *
	 * @param foreignKey the new name for the foreignKey
	 */
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * The name of the primary key on the associated model. <br>
	 * In general this will be the value of {@link Model#idProperty()}.
	 * <p>
	 * Corresponds to the <a href=
	 * "http://docs.sencha.com/ext-js/4-2/#!/api/Ext.data.association.Association-cfg-primaryKey"
	 * >primaryKey</a> config property.
	 *
	 * @param primaryKey the new name for the primaryKey
	 */
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getType() {
		return this.type;
	}

	public String getModel() {
		return this.model;
	}

	public String getInstanceName() {
		return this.instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	protected static String getModelName(Class<?> model) {
		Model modelAnnotation = model.getAnnotation(Model.class);

		if (modelAnnotation != null && Util.hasText(modelAnnotation.value())) {
			return modelAnnotation.value();
		}
		return model.getName();
	}

	protected static String getModelName(Element typeMirror) {
		Model modelAnnotation = typeMirror.getAnnotation(Model.class);

		if (modelAnnotation != null && Util.hasText(modelAnnotation.value())) {
			return modelAnnotation.value();
		}
//		return typeMirror.toString().substring(typeMirror.toString().lastIndexOf(".") + 1).toLowerCase();
		return typeMirror.toString();
	}


	public static AbstractAssociation createAssociation(
			ModelAssociation associationAnnotation) {
		ModelAssociationType type = associationAnnotation.value();
		String name = associationAnnotation.propertyName();

		if (!Util.hasText(name)) {
			return null;
		}

		Class<?> associationClass = associationAnnotation.model();
		if (associationClass == Object.class) {
			return null;
		}

		AbstractAssociation association;

		if (type == ModelAssociationType.HAS_MANY) {
			association = new HasManyAssociation(associationClass);
		}
		else if (type == ModelAssociationType.BELONGS_TO) {
			association = new BelongsToAssociation(associationClass);
		}
		else {
			association = new HasOneAssociation(associationClass);
		}

		association.setAssociationKey(name);

		if (Util.hasText(associationAnnotation.foreignKey())) {
			association.setForeignKey(associationAnnotation.foreignKey());
		}

		if (Util.hasText(associationAnnotation.primaryKey())) {
			association.setPrimaryKey(associationAnnotation.primaryKey());
		}
		else if (type == ModelAssociationType.BELONGS_TO
				|| type == ModelAssociationType.HAS_ONE) {
			Model associationModelAnnotation = associationClass
					.getAnnotation(Model.class);
			if (associationModelAnnotation != null
					&& Util.hasText(associationModelAnnotation.idProperty())
					&& !associationModelAnnotation.idProperty().equals("id")) {
				association.setPrimaryKey(associationModelAnnotation.idProperty());
			}
		}

		if (type == ModelAssociationType.HAS_MANY) {
			HasManyAssociation hasManyAssociation = (HasManyAssociation) association;

			if (Util.hasText(associationAnnotation.setterName())) {
				LogFactory.getLog(ModelGenerator.class).warn(
						getWarningText(null, name, association.getType(), "setterName"));
			}

			if (Util.hasText(associationAnnotation.getterName())) {
//				processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
//						getWarningText(null, name, association.getType(), "getterName"));
			}

			if (associationAnnotation.autoLoad()) {
				hasManyAssociation.setAutoLoad(Boolean.TRUE);
			}
			if (Util.hasText(associationAnnotation.name())) {
				hasManyAssociation.setName(associationAnnotation.name());
			}
			else {
				hasManyAssociation.setName(name);
			}

		}
		else if (type == ModelAssociationType.BELONGS_TO) {
			BelongsToAssociation belongsToAssociation = (BelongsToAssociation) association;

			if (Util.hasText(associationAnnotation.setterName())) {
				belongsToAssociation.setSetterName(associationAnnotation.setterName());
			}
			else {
				belongsToAssociation.setSetterName("set" + Util.capitalize(name));
			}

			if (Util.hasText(associationAnnotation.getterName())) {
				belongsToAssociation.setGetterName(associationAnnotation.getterName());
			}
			else {
				belongsToAssociation.setGetterName("get" + Util.capitalize(name));
			}

			if (associationAnnotation.autoLoad()) {
				LogFactory.getLog(ModelGenerator.class).warn(
						getWarningText(null, name, association.getType(), "autoLoad"));
			}
			if (Util.hasText(associationAnnotation.name())) {
				LogFactory.getLog(ModelGenerator.class)
						.warn(getWarningText(null, name, association.getType(), "name"));
			}
		}
		else {
			HasOneAssociation hasOneAssociation = (HasOneAssociation) association;

			if (Util.hasText(associationAnnotation.setterName())) {
				hasOneAssociation.setSetterName(associationAnnotation.setterName());
			}
			else {
				hasOneAssociation.setSetterName("set" + Util.capitalize(name));
			}

			if (Util.hasText(associationAnnotation.getterName())) {
				hasOneAssociation.setGetterName(associationAnnotation.getterName());
			}
			else {
				hasOneAssociation.setGetterName("get" + Util.capitalize(name));
			}

			if (associationAnnotation.autoLoad()) {
				LogFactory.getLog(ModelGenerator.class).warn(
						getWarningText(null, name, association.getType(), "autoLoad"));
			}
			if (Util.hasText(associationAnnotation.name())) {
				hasOneAssociation.setName(associationAnnotation.name());
			}
		}

		if (Util.hasText(associationAnnotation.instanceName())) {
			association.setInstanceName(associationAnnotation.instanceName());
		}

		return association;
	}

//	public static AbstractAssociation createAssociation(
//			ModelAssociation associationAnnotation, ModelBean model,
//			Class<?> typeOfFieldOrReturnValue, Class<?> declaringClass, String name) {
//		ModelAssociationType type = associationAnnotation.value();
//		Class<?> associationClass = null;
//		TypeMirror classTypeMirror = null;
//		try {
//			associationClass = associationAnnotation.model();
//		}catch (MirroredTypeException mte) {
//			classTypeMirror =  mte.getTypeMirror();
////			System.out.println(classTypeMirror);
////			try {
////				associationClass = Class.forName(classTypeMirror.toString());
////			} catch (ClassNotFoundException e) {
////				e.printStackTrace();
////			}
////			TypeElement classTypeElement = (TypeElement)Types.asElement(classTypeMirror);
////			获取canonicalName
////			String canonicalName= classTypeElement.getQualifiedName().toString();
////			获取simple name
////			String simpleName = classTypeElement.getSimpleName().toString();
//		}
//		if (classTypeMirror.toString().equals("java.lang.Object")) {
//			associationClass = typeOfFieldOrReturnValue;
//		}
////		if (associationClass == Object.class) {
////			associationClass = typeOfFieldOrReturnValue;
////		}
//
//		final AbstractAssociation association;
//
//		if (type == ModelAssociationType.HAS_MANY) {
////			association = new HasManyAssociation(associationClass);
//			association = new HasManyAssociation(classTypeMirror);
//		}
//		else if (type == ModelAssociationType.BELONGS_TO) {
////			association = new BelongsToAssociation(associationClass);
//			association = new BelongsToAssociation(classTypeMirror);
//		}
//		else {
////			association = new HasOneAssociation(associationClass);
//			association = new HasOneAssociation(classTypeMirror);
//		}
//
//		association.setAssociationKey(name);
//
//		if (Util.hasText(associationAnnotation.foreignKey())) {
//			association.setForeignKey(associationAnnotation.foreignKey());
//		}
//		else if (type == ModelAssociationType.HAS_MANY) {
//			association.setForeignKey(
//					Util.uncapitalize(declaringClass.getSimpleName()) + "_id");
//		}
//		else if (type == ModelAssociationType.BELONGS_TO
//				|| type == ModelAssociationType.HAS_ONE) {
//			association.setForeignKey(name + "_id");
//		}
//
//		if (Util.hasText(associationAnnotation.primaryKey())) {
//			association.setPrimaryKey(associationAnnotation.primaryKey());
//		}
//		else if (type == ModelAssociationType.HAS_MANY
//				&& Util.hasText(model.getIdProperty())
//				&& !model.getIdProperty().equals("id")) {
//			association.setPrimaryKey(model.getIdProperty());
//		}
//		else if (type == ModelAssociationType.BELONGS_TO
//				|| type == ModelAssociationType.HAS_ONE) {
////			Model associationModelAnnotation = associationClass
////					.getAnnotation(Model.class);
//			Model associationModelAnnotation = classTypeMirror.getAnnotation(Model.class);
//
//			if (associationModelAnnotation != null
//					&& Util.hasText(associationModelAnnotation.idProperty())
//					&& !associationModelAnnotation.idProperty().equals("id")) {
//				association.setPrimaryKey(associationModelAnnotation.idProperty());
//			}
//
//			ReflectionUtils.doWithFields(associationClass, new ReflectionUtils.FieldCallback() {
//
//				@Override
//				public void doWith(Field field)
//						throws IllegalArgumentException, IllegalAccessException {
//					if (field.getAnnotation(ModelId.class) != null
//							&& !"id".equals(field.getName())) {
//						association.setPrimaryKey(field.getName());
//					}
//				}
//
//			});
//		}
//
//		if (type == ModelAssociationType.HAS_MANY) {
//			HasManyAssociation hasManyAssociation = (HasManyAssociation) association;
//
//			if (Util.hasText(associationAnnotation.setterName())) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "setterName"));
//			}
//
//			if (Util.hasText(associationAnnotation.getterName())) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "getterName"));
//			}
//
//			if (associationAnnotation.autoLoad()) {
//				hasManyAssociation.setAutoLoad(Boolean.TRUE);
//			}
//			if (Util.hasText(associationAnnotation.name())) {
//				hasManyAssociation.setName(associationAnnotation.name());
//			}
//			else {
//				hasManyAssociation.setName(name);
//			}
//
//		}
//		else if (type == ModelAssociationType.BELONGS_TO) {
//			BelongsToAssociation belongsToAssociation = (BelongsToAssociation) association;
//
//			if (Util.hasText(associationAnnotation.setterName())) {
//				belongsToAssociation.setSetterName(associationAnnotation.setterName());
//			}
//			else {
//				belongsToAssociation.setSetterName("set" + Util.capitalize(name));
//			}
//
//			if (Util.hasText(associationAnnotation.getterName())) {
//				belongsToAssociation.setGetterName(associationAnnotation.getterName());
//			}
//			else {
//				belongsToAssociation.setGetterName("get" + Util.capitalize(name));
//			}
//
//			if (associationAnnotation.autoLoad()) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "autoLoad"));
//			}
//			if (Util.hasText(associationAnnotation.name())) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "name"));
//			}
//		}
//		else {
//			HasOneAssociation hasOneAssociation = (HasOneAssociation) association;
//
//			if (Util.hasText(associationAnnotation.setterName())) {
//				hasOneAssociation.setSetterName(associationAnnotation.setterName());
//			}
//			else {
//				hasOneAssociation.setSetterName("set" + Util.capitalize(name));
//			}
//
//			if (Util.hasText(associationAnnotation.getterName())) {
//				hasOneAssociation.setGetterName(associationAnnotation.getterName());
//			}
//			else {
//				hasOneAssociation.setGetterName("get" + Util.capitalize(name));
//			}
//
//			if (associationAnnotation.autoLoad()) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "autoLoad"));
//			}
//
//			if (Util.hasText(associationAnnotation.name())) {
//				hasOneAssociation.setName(associationAnnotation.name());
//			}
//		}
//
//		if (Util.hasText(associationAnnotation.instanceName())) {
//			association.setInstanceName(associationAnnotation.instanceName());
//		}
//
//		return association;
//	}

	public static AbstractAssociation createAssociation(ModelAssociation associationAnnotation, ModelBean model,
														TypeMirror typeOfFieldOrReturnValue, Element declaringClass, String name, Types types, Elements elementUtil) {
		ModelAssociationType type = associationAnnotation.value();
		Element classTypeMirror = null;
		try {
			associationAnnotation.model();
		}catch (MirroredTypeException mte) {
			classTypeMirror = types.asElement(mte.getTypeMirror());
		}
		if (classTypeMirror.toString().equals("java.lang.Object")) {
			classTypeMirror = types.asElement(typeOfFieldOrReturnValue);
		}
		final AbstractAssociation association;
		if (type == ModelAssociationType.HAS_MANY) {
			association = new HasManyAssociation(classTypeMirror);
		}
		else if (type == ModelAssociationType.BELONGS_TO) {
			association = new BelongsToAssociation(classTypeMirror);
		}
		else {
			association = new HasOneAssociation(classTypeMirror);
		}
		association.setAssociationKey(name);

		if (Util.hasText(associationAnnotation.foreignKey())) {
			association.setForeignKey(associationAnnotation.foreignKey());
		}
		else if (type == ModelAssociationType.HAS_MANY) {
			association.setForeignKey(
					Util.uncapitalize(declaringClass.toString().substring(declaringClass.toString().lastIndexOf(".") + 1).toLowerCase()) + "_id");
		}
		else if (type == ModelAssociationType.BELONGS_TO
				|| type == ModelAssociationType.HAS_ONE) {
			association.setForeignKey(name + "_id");
		}

		if (Util.hasText(associationAnnotation.primaryKey())) {
			association.setPrimaryKey(associationAnnotation.primaryKey());
		}
		else if (type == ModelAssociationType.HAS_MANY
				&& Util.hasText(model.getIdProperty())
				&& !model.getIdProperty().equals("id")) {
			association.setPrimaryKey(model.getIdProperty());
		}
		else if (type == ModelAssociationType.BELONGS_TO
				|| type == ModelAssociationType.HAS_ONE) {
			Model associationModelAnnotation = classTypeMirror.getAnnotation(Model.class);

			if (associationModelAnnotation != null
					&& Util.hasText(associationModelAnnotation.idProperty())
					&& !associationModelAnnotation.idProperty().equals("id")) {
				association.setPrimaryKey(associationModelAnnotation.idProperty());
			}

			if (classTypeMirror.getAnnotation(ModelId.class) != null
					&& !"id".equals(classTypeMirror.getSimpleName() + "")) {
				association.setPrimaryKey(classTypeMirror.getSimpleName() + "");
			}
			List<Element> list = (List<Element>) elementUtil.getAllMembers((TypeElement) classTypeMirror);
			for (Element element: list) {
				if (element.getKind() == ElementKind.FIELD) {
					if (element.getAnnotation(ModelId.class) != null
							&& !"id".equals(element.getSimpleName() + "")) {
						association.setPrimaryKey(element.getSimpleName() + "");
					}
				}

			}
		}

		if (type == ModelAssociationType.HAS_MANY) {
			HasManyAssociation hasManyAssociation = (HasManyAssociation) association;

			if (Util.hasText(associationAnnotation.setterName())) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "setterName"));
			}

			if (Util.hasText(associationAnnotation.getterName())) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "getterName"));
			}

			if (associationAnnotation.autoLoad()) {
				hasManyAssociation.setAutoLoad(Boolean.TRUE);
			}
			if (Util.hasText(associationAnnotation.name())) {
				hasManyAssociation.setName(associationAnnotation.name());
			}
			else {
				hasManyAssociation.setName(name);
			}

		}
		else if (type == ModelAssociationType.BELONGS_TO) {
			BelongsToAssociation belongsToAssociation = (BelongsToAssociation) association;

			if (Util.hasText(associationAnnotation.setterName())) {
				belongsToAssociation.setSetterName(associationAnnotation.setterName());
			}
			else {
				belongsToAssociation.setSetterName("set" + Util.capitalize(name));
			}

			if (Util.hasText(associationAnnotation.getterName())) {
				belongsToAssociation.setGetterName(associationAnnotation.getterName());
			}
			else {
				belongsToAssociation.setGetterName("get" + Util.capitalize(name));
			}

			if (associationAnnotation.autoLoad()) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "autoLoad"));
			}
			if (Util.hasText(associationAnnotation.name())) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "name"));
			}
		}
		else {
			HasOneAssociation hasOneAssociation = (HasOneAssociation) association;

			if (Util.hasText(associationAnnotation.setterName())) {
				hasOneAssociation.setSetterName(associationAnnotation.setterName());
			}
			else {
				hasOneAssociation.setSetterName("set" + Util.capitalize(name));
			}

			if (Util.hasText(associationAnnotation.getterName())) {
				hasOneAssociation.setGetterName(associationAnnotation.getterName());
			}
			else {
				hasOneAssociation.setGetterName("get" + Util.capitalize(name));
			}

			if (associationAnnotation.autoLoad()) {
//				LogFactory.getLog(ModelGenerator.class).warn(getWarningText(
//						declaringClass, name, association.getType(), "autoLoad"));
			}

			if (Util.hasText(associationAnnotation.name())) {
				hasOneAssociation.setName(associationAnnotation.name());
			}
		}

		if (Util.hasText(associationAnnotation.instanceName())) {
			association.setInstanceName(associationAnnotation.instanceName());
		}

		return association;
	}



	private static String getWarningText(Class<?> declaringClass, String name,
			String type, String propertyName) {
		String warning = "Field ";
		if (declaringClass != null) {
			warning += declaringClass.getName();
			warning += ".";
		}
		warning += name;
		return warning + ": A '" + type + "' association does not support property '"
				+ propertyName + "'. Property will be ignored.";
	}

}
