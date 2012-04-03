package it.sample.parser.core.common;

import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;
import static org.springframework.util.StringUtils.capitalize;
import it.sample.parser.domain.annotation.DataEntity;
import it.sample.parser.domain.annotation.DataField;
import it.sample.parser.util.CommonsUtil;
import it.sample.parser.util.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

;

/**
 * L'implementazione del {@link DomainParser} su stringa posizionale
 * 
 * @author Massimo Ugues
 * 
 */
public class DomainStringParser<T> implements DomainParser<T, String> {
	private String NK_DATE_PATTERN = "yyyy-MM-dd-hh.mm.ss.SSS";
	private Class<T> persistentClass;

	private final static Logger logger = LoggerFactory.getLogger(DomainStringParser.class);

	public DomainStringParser() {}

	public DomainStringParser(Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
	}

	public T parse(String string, boolean isOnline) {
		T instance = null;
		try {
			instance = persistentClass.newInstance();
			instance = parse(instance, string, isOnline);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		CommonsUtil.sanitizeObject(instance);
		return instance;
	}

	public Collection<T> parseAll(String string, boolean isOnline) {
		Collection<T> elements = new ArrayList<T>();
		try {

			DataEntity entity = AnnotationUtils.findAnnotation(persistentClass, DataEntity.class);

			String[] joinSplit = {string};
			if (isOnline) {
				joinSplit = StringUtil.joinSplit(string, entity.regex());
			}

			for (String record : joinSplit) {
				elements.add(parse(persistentClass.newInstance(), record, isOnline));
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return elements;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public T parse(T instance, String string, boolean isOnline){
		try {
			DataEntity entity = AnnotationUtils.findAnnotation(persistentClass, DataEntity.class);
			Field[] fields = persistentClass.getDeclaredFields();

			String record = string;
			if (isOnline && !entity.regex().equals(DataEntity.NESTED)) {
				record = StringUtil.substringAfterFirstOccurrence(string, entity.regex());
			}

			for (Field field : fields) {
				DataField dataField = field.getAnnotation(DataField.class);

				if (dataField != null) {
					int start = 0;
					int end = 0;
					Object value = null;

					if (field.getType().getSuperclass() == Enum.class) {
						start = isOnline ? dataField.startRangeOnline() : dataField.startRangeBatch();
						end = start + (isOnline ? dataField.lengthOnline() : dataField.lenghtBatch());
						Method method = field.getType().getMethod("decode", String.class);
						if (end <= record.length()) {
							String tmpVal = record.substring(start, end);
							value = method.invoke(null, new Object[] { tmpVal });
						}
					} else if (isComplexType(field.getType())) {
						start = isOnline ? dataField.startRangeOnline() : dataField.startRangeBatch();
						end = start + (isOnline ? dataField.lengthOnline() : dataField.lenghtBatch());
						if (end <= record.length()) {
							String substring;
							try {
								if (!dataField.isAggregate()) {
									substring = record.substring(start, end);
								} else {
									substring = record;
								}
							} catch (StringIndexOutOfBoundsException e) {
								logger.debug("StringIndexOutOfBoundsException: field=[{}] string=[{}]", field.getName(), record);
								throw e;
							}
							if (substring.trim().length() != 0) {
								Class<?> type = field.getType();
								DomainStringParser domainStringParser = new DomainStringParser(type);
								value = domainStringParser.parse(substring, isOnline);
							}
						}
					} else {
						start = isOnline ? dataField.startRangeOnline() : dataField.startRangeBatch();
						end = start + (isOnline ? dataField.lengthOnline() : dataField.lenghtBatch());
						if (end <= record.length()) {
							String substring = record.substring(start, end).trim();
							if (field.getType().equals(Calendar.class)) {
								value = parseLegacyDate(substring);
							} else {
								value = substring;
							}
						}
					}					
					setField(instance, field, value);					
				} 
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return instance;
	}

	

	private void setField(T instance, Field field, Object value) {
		String setterMethodName = "set" + capitalize(field.getName());
		Method method = findMethod(instance.getClass(), setterMethodName, new Class[] { field.getType() });
		Class<?> type = field.getType();
		
		Object newInstance = value;		
		if (value != null && !value.equals("")) {
			if (Number.class.isAssignableFrom(type)) {
				try {
					Constructor<?> constructor = type.getConstructor( new Class[] {String.class});
					newInstance = constructor.newInstance(value);
				} catch (Exception e) {				
					// Swallow the exception could not happen
				}
			}
							
			invokeMethod(method, instance, new Object[] { newInstance });
		}
	}

	/**
	 * Parsifica la data sotto forma di stringa nel formato legacy in un Calendar
	 * 
	 * @param substring
	 * @return
	 */
	private Calendar parseLegacyDate(String data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NK_DATE_PATTERN);

		Calendar calendar = Calendar.getInstance();
		try {
			Date date = simpleDateFormat.parse(data);
			calendar.setTime(date);
		} catch (ParseException e) {
			// swallow the exception, use now instead
		}
		return calendar;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	protected Boolean isComplexType(Class clazz) {
		Class[] clazzArray = { String.class, Integer.class, Long.class, Calendar.class };
		Arrays.sort(clazzArray, new ArrayComparator());
		int binarySearch = Arrays.binarySearch(clazzArray, clazz, new ArrayComparator());

		return binarySearch < 0;
	}

	@SuppressWarnings("rawtypes")
	public static class ArrayComparator implements Comparator {

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			Class clazz1 = (Class) o1;
			Class clazz2 = (Class) o2;
			return clazz1.toString().compareTo(clazz2.toString());
		}
	}
	
}