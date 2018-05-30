package ch.rasc.extclassgenerator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ModelType_ {
	private static Set<String> intSet = new HashSet<String>();
	private static Set<String> floatSet = new HashSet<String>();
	private static Set<String> booleanSet = new HashSet<String>();
	private static Set<String> stringSet = new HashSet<String>();
	private static Set<String> dateSet = new HashSet<String>();

	static {
		intSet.addAll(Arrays.asList(new String[]{"java.lang.Integer", "int", "java.lang.Byte", "byte",
				"java.lang.Short", "short", "java.math.BigInteger", "java.lang.Long", "long"}));

		floatSet.addAll(Arrays.asList(new String[]{"java.lang.Float", "float", "java.lang.Double", "double",
				"java.math.BigDecimal"}));

		booleanSet.addAll(Arrays.asList(new String[]{"java.lang.Boolean", "boolean"}));

		dateSet.addAll(Arrays.asList(new String[]{"java.util.Date","java.sql.Date","java.sql.Timestamp","org.joda.time.DateTime",
				"org.joda.time.LocalDate","org.joda.time.ReadableDateTime","java.time.LocalDate","java.time.LocalDateTime",
				"java.time.ZonedDateTime","java.time.OffsetDateTime"}));

		stringSet.addAll(Arrays.asList(new String[]{"java.lang.String","char"}));
	}

	static ModelType getType(String type) {
		if (intSet.contains(type)) {
			return ModelType.INTEGER;
		}else if (floatSet.contains(type)) {
			return ModelType.FLOAT;
		}else if (booleanSet.contains(type)) {
			return ModelType.BOOLEAN;
		}else if (dateSet.contains(type)) {
			return ModelType.DATE;
		}else if (stringSet.contains(type)) {
			return ModelType.STRING;
		}
		return null;
	}
}
