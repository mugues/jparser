/**
 *	11/feb/2011
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
package it.sample.parser.domain;

import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * @author Massimo Ugues
 *
 */
public enum TipologiaRapporto {
	CONTO_CORRENTE("1", ContoCorrente.class), 
	DEPOSITO_AMMINISTRATO("2", DepositoAmministrato.class);
	
	private final static Map<String, TipologiaRapporto> valueMap = new HashMap<String, TipologiaRapporto>();

	static {
		for (TipologiaRapporto tipologiaRapporto : EnumSet.allOf(TipologiaRapporto.class)) {
			valueMap.put(tipologiaRapporto.getTipoRapporto(), tipologiaRapporto);
		}
	}
	
	private final String tipoRapporto;	
	private final Class<? extends Rapporto> clazz;
	
		

	/**
	 * Constructor.
	 */
	private <T extends Rapporto> TipologiaRapporto(String tipoRapporto, Class<T> clazz) {
		this.tipoRapporto = tipoRapporto;
		this.clazz = clazz;
	}

	public String getTipoRapporto() {
		return tipoRapporto;
	}

	/**
	 * @param tipoRapporto
	 * @return
	 */
	public static TipologiaRapporto decode(String tipoRapporto) {
		return TipologiaRapporto.valueMap.get(StringUtils.trim(tipoRapporto));		
	}
	
	/**
	 * Restituisce una istanza della classe.
	 */
	public Rapporto newInstance() {
		try {
			return Rapporto.class.cast(clazz.newInstance());
		} catch (Exception e) {
			throw new UnsupportedOperationException(clazz.getName() + " non espone un costruttore privo di argomenti.");
		}
	}
	
	/**
	 * Restituisce una istanza della classe.
	 */
	public Rapporto newInstance(Rapporto rapporto) {
		try {
			Class<?>[] intArgsClass = new Class[] {Filiale.class, String.class, String.class};
			Object[] intArgs = new Object[] {rapporto.getFiliale(), rapporto.getCategoria(), rapporto.getNumeroRapporto()};
			Constructor<? extends Rapporto> constructor = clazz.getConstructor(intArgsClass);
			return Rapporto.class.cast(constructor.newInstance(intArgs));
		} catch (Exception e) {
			throw new UnsupportedOperationException(clazz.getName() + " non espone un costruttore privo di argomenti.");
		}
	}
}
