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
package it.sample.parser.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Massimo Ugues
 *
 */
public class StringUtil {

	/**
	 * Restituisce la sottostringa a partire da regex cercando regex in string 
	 * @param string
	 * @param regex
	 * @return
	 */
	public static String substringAfterFirstOccurrence(String string, String regex) {		
		String result = string;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
				
		if (matcher.find()) {
			result = string.substring(matcher.end());			
		}
		
		return result;		
	}

	/**
	 * @param record
	 * @param regex
	 * @return
	 */
	public static String[] joinSplit(String record, String regex) {
		String[] split = record.split(regex);
		
		Collection<String> elements = new ArrayList<String>();
		
		for (int i=1; i<split.length; i++) {
			regex = (regex.indexOf("|") != -1) ? regex.substring(0, regex.indexOf("|")) : regex;
			elements.add(regex + split[i]);
		}
		return elements.toArray(new String[0]);		
	}
}
