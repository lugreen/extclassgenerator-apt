package ch.rasc.extclassgenerator.validation;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonView;

import ch.rasc.extclassgenerator.IncludeValidation;
import ch.rasc.extclassgenerator.JsonViews;
import ch.rasc.extclassgenerator.ModelBean;
import ch.rasc.extclassgenerator.ModelFieldBean;
import ch.rasc.extclassgenerator.ModelValidation;
import ch.rasc.extclassgenerator.ModelValidationParameter;
import ch.rasc.extclassgenerator.ModelValidationType;
import ch.rasc.extclassgenerator.OutputConfig;
import ch.rasc.extclassgenerator.OutputFormat;
import org.springframework.core.annotation.AnnotationUtils;

import javax.lang.model.element.AnnotationMirror;

/**
 * Base class for the validation objects
 */
public abstract class AbstractValidation {
	private final String type;

	@JsonView({ JsonViews.ExtJS4.class, JsonViews.Touch2.class })
	private final String field;

	public AbstractValidation(String type, String field) {
		super();
		this.type = type;
		this.field = field;
	}

	public String getType() {
		return this.type;
	}

	public String getField() {
		return this.field;
	}

	public static void addValidationToModel(ModelBean model,
			ModelFieldBean modelFieldBean, Annotation fieldAnnotation,
			OutputConfig outputConfig) {
		String annotationClassName = fieldAnnotation.annotationType().getName();
		IncludeValidation includeValidation = outputConfig.getIncludeValidation();

		if (includeValidation == IncludeValidation.BUILTIN
				|| includeValidation == IncludeValidation.ALL) {

			if (annotationClassName.equals("javax.validation.constraints.NotNull")
					|| annotationClassName
							.equals("org.hibernate.validator.constraints.NotEmpty")) {
				model.addValidation(new PresenceValidation(modelFieldBean.getName()));
			}
			else if (annotationClassName.equals("javax.validation.constraints.Size")
					|| annotationClassName
							.equals("org.hibernate.validator.constraints.Length")) {

				Integer min = (Integer) AnnotationUtils.getValue(fieldAnnotation, "min");
				Integer max = (Integer) AnnotationUtils.getValue(fieldAnnotation, "max");
				model.addValidation(
						new LengthValidation(modelFieldBean.getName(), min, max));

			}
			else if (annotationClassName.equals("javax.validation.constraints.Pattern")) {
				String regexp = (String) AnnotationUtils.getValue(fieldAnnotation,
						"regexp");
				model.addValidation(
						new FormatValidation(modelFieldBean.getName(), regexp));
			}
			else if (annotationClassName
					.equals("org.hibernate.validator.constraints.Email")) {
				model.addValidation(new EmailValidation(modelFieldBean.getName()));
			}
		}

		if (includeValidation == IncludeValidation.BUILTIN
				&& outputConfig.getOutputFormat() == OutputFormat.EXTJS5
				|| includeValidation == IncludeValidation.ALL) {

			if (annotationClassName.equals("javax.validation.constraints.DecimalMax")) {
				String value = (String) AnnotationUtils.getValue(fieldAnnotation);
				model.addValidation(new RangeValidation(modelFieldBean.getName(), null,
						new BigDecimal(value)));
			}
			else if (annotationClassName
					.equals("javax.validation.constraints.DecimalMin")) {
				String value = (String) AnnotationUtils.getValue(fieldAnnotation);
				model.addValidation(new RangeValidation(modelFieldBean.getName(),
						new BigDecimal(value), null));
			}
			else if (annotationClassName.equals("javax.validation.constraints.Max")) {
				Long value = (Long) AnnotationUtils.getValue(fieldAnnotation);
				model.addValidation(
						new RangeValidation(modelFieldBean.getName(), null, value));
			}
			else if (annotationClassName.equals("javax.validation.constraints.Min")) {
				Long value = (Long) AnnotationUtils.getValue(fieldAnnotation);
				model.addValidation(
						new RangeValidation(modelFieldBean.getName(), value, null));
			}
			else if (annotationClassName
					.equals("org.hibernate.validator.constraints.Range")) {
				Long min = (Long) AnnotationUtils.getValue(fieldAnnotation, "min");
				Long max = (Long) AnnotationUtils.getValue(fieldAnnotation, "max");
				model.addValidation(
						new RangeValidation(modelFieldBean.getName(), min, max));
			}
		}

		if (includeValidation == IncludeValidation.ALL) {

			if (annotationClassName.equals("javax.validation.constraints.Digits")) {
				Integer integer = (Integer) AnnotationUtils.getValue(fieldAnnotation,
						"integer");
				Integer fraction = (Integer) AnnotationUtils.getValue(fieldAnnotation,
						"fraction");
				model.addValidation(new DigitsValidation(modelFieldBean.getName(),
						integer, fraction));
			}
			else if (annotationClassName.equals("javax.validation.constraints.Future")) {
				model.addValidation(new FutureValidation(modelFieldBean.getName()));
			}
			else if (annotationClassName.equals("javax.validation.constraints.Past")) {
				model.addValidation(new PastValidation(modelFieldBean.getName()));
			}
			else if (annotationClassName
					.equals("org.hibernate.validator.constraints.CreditCardNumber")) {
				model.addValidation(
						new CreditCardNumberValidation(modelFieldBean.getName()));
			}
			else if (annotationClassName
					.equals("org.hibernate.validator.constraints.NotBlank")) {
				model.addValidation(new NotBlankValidation(modelFieldBean.getName()));
			}
		}
	}

	public static AbstractValidation createValidation(String propertyName,
			ModelValidation modelValidationAnnotation,
			IncludeValidation includeValidation) {

		if (propertyName == null || propertyName.trim().isEmpty()) {
			return null;
		}

		ModelValidationType validationType = modelValidationAnnotation.value();

		if ((includeValidation == IncludeValidation.ALL
				|| includeValidation == IncludeValidation.BUILTIN
						&& validationType.isBuiltin())
				&& validationType.isValid(modelValidationAnnotation)) {
			switch (validationType) {
			case GENERIC:
				String type = getParameterValue(modelValidationAnnotation.parameters(),
						"type");
				Map<String, Object> options = new LinkedHashMap<>();
				for (ModelValidationParameter parameter : modelValidationAnnotation
						.parameters()) {
					if (!parameter.name().equals("type")) {
						options.put(parameter.name(), parameter.value());
					}
				}
				return new GenericValidation(type, propertyName, options);
			case CREDITCARDNUMBER:
				return new CreditCardNumberValidation(propertyName);
			case DIGITS:
				String integer = getParameterValue(modelValidationAnnotation.parameters(),
						"integer");
				String fraction = getParameterValue(
						modelValidationAnnotation.parameters(), "fraction");
				return new DigitsValidation(propertyName, Integer.parseInt(integer),
						Integer.parseInt(fraction));
			case EMAIL:
				return new EmailValidation(propertyName);
			case FORMAT:
				return new FormatValidation(propertyName,
						modelValidationAnnotation.parameters()[0].value());
			case FUTURE:
				return new FutureValidation(propertyName);
			case INCLUSION:
				if (modelValidationAnnotation.exclusionOrInclusionList().length > 0) {
					List<String> list = Arrays
							.asList(modelValidationAnnotation.exclusionOrInclusionList());
					return new InclusionValidationArray(propertyName, list);
				}
				else {// backward compatibility
					String list = getParameterValue(
							modelValidationAnnotation.parameters(), "list");
					return new InclusionValidation(propertyName, list);
				}
			case EXCLUSION:
				if (modelValidationAnnotation.exclusionOrInclusionList().length > 0) {
					List<String> list = Arrays
							.asList(modelValidationAnnotation.exclusionOrInclusionList());
					return new ExclusionValidationArray(propertyName, list);
				}
				else {// backward compatibility
					String list = getParameterValue(
							modelValidationAnnotation.parameters(), "list");
					return new ExclusionValidation(propertyName, list);
				}
			case LENGTH:
				String minValue = getParameterValue(
						modelValidationAnnotation.parameters(), "min");
				String maxValue = getParameterValue(
						modelValidationAnnotation.parameters(), "max");
				Long min = null;
				Long max = null;

				if (minValue != null) {
					min = Long.valueOf(minValue);
				}
				if (maxValue != null) {
					max = Long.valueOf(maxValue);
				}
				return new LengthValidation(propertyName, min, max);
			case NOTBLANK:
				return new NotBlankValidation(propertyName);
			case PAST:
				return new PastValidation(propertyName);
			case PRESENCE:
				return new PresenceValidation(propertyName);
			case RANGE:
				minValue = getParameterValue(modelValidationAnnotation.parameters(),
						"min");
				maxValue = getParameterValue(modelValidationAnnotation.parameters(),
						"max");

				if (minValue != null && minValue.indexOf(".") != -1
						|| maxValue != null && maxValue.indexOf(".") != -1) {
					BigDecimal minBD = null;
					BigDecimal maxBD = null;

					if (minValue != null) {
						minBD = new BigDecimal(minValue);
					}
					if (maxValue != null) {
						maxBD = new BigDecimal(maxValue);
					}
					return new RangeValidation(propertyName, minBD, maxBD);
				}
				min = null;
				max = null;

				if (minValue != null) {
					min = Long.valueOf(minValue);
				}
				if (maxValue != null) {
					max = Long.valueOf(maxValue);
				}
				return new RangeValidation(propertyName, min, max);

			default:
				return null;
			}
		}

		return null;
	}

	private static String getParameterValue(ModelValidationParameter[] parameters,
			String param) {
		for (ModelValidationParameter modelValidationParameter : parameters) {
			if (param.equals(modelValidationParameter.name())) {
				return modelValidationParameter.value();
			}
		}
		return null;
	}

}
