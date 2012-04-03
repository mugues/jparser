/**
 *	22/dic/2010
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
package it.sample.parser.core.common;

import java.util.Collection;

/**
 * L'interfaccia generica per la definizione di un parser
 * 
 * @author Massimo Ugues
 * 
 */
public interface DomainParser<T, V> {
	T parse(V obj, boolean isOnline);
	T parse(T instance, String string, boolean isOnline);
	Collection<T> parseAll(V obj, boolean isOnline);
	
	
}
