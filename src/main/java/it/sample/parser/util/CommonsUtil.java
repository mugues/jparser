/**
 *	09/feb/2011
 *
 * Copyright (c) 2010 Alten Italia, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Alten Italia ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered 
 * into with Alten Italia.
 *
 * Alten Italia - MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY 
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * OR NON-INFRINGEMENT. ALTEN ITALIA SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR 
 * ITS DERIVATIVES.
 */
package it.sample.parser.util;

import it.sample.parser.domain.annotation.IgnoreField;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author Damiano Masillo
 * 
 */
public class CommonsUtil {
	private static final String PREFISSO_CANALE = "64";

	/**
	 * Metodo di utilita' per ricavare da un multichannelId un abi
	 * 
	 * @param multichannelId
	 * @return la stringa rappresentante l'abi
	 * @throws IllegalArgumentException
	 *             se la stringa del multichannel non e' valida
	 */
	public static String getAbi(String multichannelId) {
		if (multichannelId == null || multichannelId.length() != 17)
			throw new IllegalArgumentException("Illegal multichannelID string lenght");

		return multichannelId.substring(0, 5);

	}

	/**
	 * Metodo di utilita' per ricavare da un multichannelId un bt
	 * 
	 * @param multichannelId
	 * @return la stringa rappresentante il bt
	 * @throws IllegalArgumentException
	 *             se la stringa del multichannel non e' valida
	 */
	public static String getBt(String multichannelId) {
		if (multichannelId == null || multichannelId.length() != 17)
			throw new IllegalArgumentException("Illegal multichannelID string lenght");

		return multichannelId.substring(9, multichannelId.length());
	}

	/**
	 * Metodo di utilita per la composizione di un multichannelId a partire da un abi, un canale e un bt
	 * 
	 * @param abi
	 * @param canale
	 * @param userid
	 * @return multichannelId
	 * @throws IllegalArgumentException
	 *             se l'abi, il canale o il bt non sono validi
	 */
	public static String getMultichannelId(String abi, String canale, String userid) {

		if (abi == null || abi.length() > 5)
			throw new IllegalArgumentException("Stringa ABI troppo lunga");

		if (canale == null || canale.length() > 2)
			throw new IllegalArgumentException("Stringa canale troppo lunga");

		if (userid == null || userid.length() > 8)
			throw new IllegalArgumentException("Stringa userid troppo lunga");

		StringBuffer buf = new StringBuffer(17);
		buf.append(StringUtils.leftPad(abi, 5, "0"));
		buf.append(PREFISSO_CANALE);
		buf.append(StringUtils.leftPad(canale, 2, "0"));
		buf.append(StringUtils.leftPad(userid, 8, "0"));
		return buf.toString();
	}

	/**
	 * Restituisce il codice filiale a partire dal codice rapporto host
	 * 
	 * 
	 * @param codiceRapportoHost
	 * @return
	 */
	public static String getCodiceFiliale(String codiceRapportoHost) {
		if (codiceRapportoHost == null || codiceRapportoHost.length() != 17) {
			throw new IllegalArgumentException("Illegal codiceRapportoHost string lenght");
		}

		return codiceRapportoHost.substring(0, 5);
	}

	/**
	 * Restituisce la categoria a partire dal codice rapporto host
	 * 
	 * @param codiceRapportoHost
	 * @return
	 */
	public static String getCategoria(String codiceRapportoHost) {
		if (codiceRapportoHost == null || codiceRapportoHost.length() != 17) {
			throw new IllegalArgumentException("Illegal codiceRapportoHost string lenght");
		}

		return codiceRapportoHost.substring(5, 9);
	}

	/**
	 * Restituisce il numero rapporto a partire dal codice rapporto host
	 * 
	 * @param codiceRapportoHost
	 * @return
	 */
	public static String getNumeroRapporto(String codiceRapportoHost) {
		if (codiceRapportoHost == null || codiceRapportoHost.length() != 17) {
			throw new IllegalArgumentException("Illegal codiceRapportoHost string lenght");
		}

		return codiceRapportoHost.substring(9);
	}

	/**
	 * Metodo di utilita' che copia i campi non nulli della classe sorgente in quelli della classe di destinazione
	 * 
	 * @param source
	 * @param destination
	 */
	public static <K, T> void copyNotNullProperties(K source, T destination) {
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(source);
		if (descriptors != null) {
			for (PropertyDescriptor descriptor : descriptors) {
				try {
					String propertyName = descriptor.getName();
					Field field = getDeclaredField(propertyName, source.getClass());
					
					if (field != null && field.getAnnotation(IgnoreField.class) == null) {						
						boolean wasAccessible = field.isAccessible();
						field.setAccessible(true);
						
						if (PropertyUtils.getReadMethod(descriptor) != null) {
							Object val = PropertyUtils.getSimpleProperty(source, propertyName);
							if (val != null && descriptor.getWriteMethod() != null) {
								PropertyUtils.setProperty(destination, propertyName, val);
							}
						}
						field.setAccessible(wasAccessible);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
		
	/**
	 * 
	 * @param propertyName
	 * @param type
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	private static Field getDeclaredField(String propertyName, Class<?> type) throws SecurityException {
		Field declaredField = null;

		if (!propertyName.equals("class")) {
			try {
				declaredField = type.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				if (type.getSuperclass() != null) {
					declaredField = getDeclaredField(propertyName, type.getSuperclass());
				}
			}			
		}		
		return declaredField;
	}

	/**
	 * Metodo di utilita' per rendere null un'istanza di una classe che e' istanziata ma che ha tutti i campi null
	 * 
	 * @param instance
	 */
	public static <T> void sanitizeObject(T instance) {
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(instance);
		if (descriptors != null) {
			for (PropertyDescriptor descriptor : descriptors) {
				try {
					String propertyName = descriptor.getName();
					if (descriptor.getReadMethod() != null) {
						Object val = PropertyUtils.getProperty(instance, propertyName);
						if (val != null && !BeanUtils.isSimpleProperty(val.getClass()) && descriptor.getWriteMethod() != null && !isFilled(val)
								&& !val.getClass().getName().startsWith("java.util")) {
							PropertyUtils.setProperty(instance, propertyName, null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Metodo di utilita' per controllare che un'istanza di una classe abbia almeno un campo valorizzato
	 * 
	 * @param instance
	 * @return true se l'istanza ha almeno un campo valorizzato, false altrimenti
	 */
	public static <T> boolean isFilled(T instance) {
		boolean isFilled = false;
		Method[] methods = instance != null ? instance.getClass().getMethods() : new Method[] {};
		for (Method method : methods) {
			if (isGetter(method)) {
				Class<?> returnType = method.getReturnType();
				Object obj = null;
				try {
					obj = method.invoke(instance, new Object[] {});
					if (obj != null && (returnType.getSimpleName().equals("String") && obj.toString().length() > 0)) {
						isFilled = true;
						break;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return isFilled;
	}

	/**
	 * metodo di utilita' per controllare che un metodo sia un getter
	 * 
	 * @param method
	 * @return true se il metodo e' un getter, false altrimenti
	 */
	private static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get"))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	/**
	 * Formatta una data col seguente pattern dd/MM/yyyy HH:MM:SS
	 * 
	 * @param calendar
	 * @return data formattata
	 */
	public static String formatDate(Calendar calendar) {
		String stringDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");

		if (calendar != null) {
			stringDate = sdf.format(calendar.getTime());
		}
		return stringDate;
	}
}
