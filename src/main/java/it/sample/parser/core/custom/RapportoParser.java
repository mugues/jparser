/**
 *	09/giu/2011
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
package it.sample.parser.core.custom;

import static ch.lambdaj.Lambda.forEach;
import it.sample.parser.core.common.DomainStringParser;
import it.sample.parser.domain.Intestatario;
import it.sample.parser.domain.Rapporto;
import it.sample.parser.domain.annotation.RegexConstants;
import it.sample.parser.util.CommonsUtil;
import it.sample.parser.util.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Specializzazione del parser per la classe {@link Rapporto} 
 * 
 * @author Massimo Ugues
 * 
 */
public class RapportoParser extends DomainStringParser<Rapporto> {
	@Autowired
	@Qualifier("intestatarioParser")
	private DomainStringParser<Intestatario> intestatarioParser;

	public RapportoParser() {
		super(Rapporto.class);
	}

	/**
	 * Parsifica tutti i rapporti in modalita' lazy contenuti in string normalizzandone l'abi rispetto all'abi del flusso Restituisce tutte istanze di {@link Rapporto}
	 */
	@Override
	public Collection<Rapporto> parseAll(String string, boolean isOnline) {
		Collection<Rapporto> rapporti = super.parseAll(string, isOnline);

		if (isOnline) {
			String record = StringUtil.substringAfterFirstOccurrence(string, RegexConstants.ANAGRAFICA_REGEX);
			String abi = record.substring(0, 5);
			if(!abi.trim().equals("") && abi.matches("\\d{5}")){
				forEach(rapporti).setAbi(abi);
			}
		}

		return rapporti;
	}

	/**
	 * Parsifica il rapporto in modalita' lazy contenuto in string normalizzandone l'abi rispetto all'abi del flusso
	 * 
	 * @see it.alten.intesasanpaolo.contratto.parsers.common.DomainStringParser#parse(java.lang.String, boolean)
	 */
	@Override
	public Rapporto parse(String string, boolean isOnline) {
		Rapporto rapporto = null;
		String abi = null;

		if (!isOnline) {
			abi = string.substring(2, 7);
			string = string.substring(20);
		}

		rapporto = super.parse(string, isOnline);

		if ((rapporto.getAbi() == null || (rapporto.getAbi() != null && !rapporto.getAbi().matches("\\d{5}"))) && (StringUtils.isNotEmpty(abi) && abi.matches("\\d{5}"))) {
			rapporto.setAbi(abi);
		}
		return rapporto;
	}

	/**
	 * Parsifica tutti i rapporti in modalita' eager (inizializzandone le associazioni) contenuti in string normalizzandone l'abi rispetto all'abi del flusso Restituisce tutte
	 * istanze di {@link Rapporto}
	 * 
	 * @param string
	 * @param isOnline
	 * @return
	 */
	public Collection<Rapporto> parseAllEager(String string, boolean isOnline) {
		String abi = StringUtil.substringAfterFirstOccurrence(string, RegexConstants.ANAGRAFICA_REGEX).substring(0, 5);

		Collection<Rapporto> rapporti = new ArrayList<Rapporto>();
		String[] rapportiString = StringUtil.joinSplit(string, RegexConstants.RECORD_0210_REGEX);

		for (String rapportoString : rapportiString) {
			rapporti.add(parseEager(rapportoString, isOnline));
		}
		
		if(!abi.trim().equals("") && abi.matches("\\d{5}")){
			forEach(rapporti).setAbi(abi);
		}
		return rapporti;
	}

	/**
	 * Parsifica il primo rapporto modalita' eager (inizializzandone le associazioni) contenuti in string normalizzandone l'abi rispetto all'abi del flusso Restituisce tutte
	 * istanze di {@link Rapporto}
	 * 
	 * @param string
	 * @param isOnline
	 * @return
	 */

	public Rapporto parseEager(String string, boolean isOnline) {		
		String[] rapportiString = StringUtil.joinSplit(string, RegexConstants.RECORD_0210_REGEX);
		
		if (rapportiString.length > 0) {
			string = rapportiString[0];
		}

		Rapporto rapporto = super.parse(string, isOnline);		
		Rapporto rapportoEnhanced = rapporto.getTipoRapporto().newInstance(rapporto);
		CommonsUtil.copyNotNullProperties(rapporto, rapportoEnhanced);

		enhanceRapporto(rapportoEnhanced, string);

		for (String intestatarioString : StringUtil.joinSplit(string, RegexConstants.RECORD_0220_REGEX)) {
			rapportoEnhanced.addIntestatario(intestatarioParser.parse(intestatarioString, isOnline));
		}

		return rapportoEnhanced;
	}

	/**
	 * Richiama se definito il metodo enhanceRapporto a seconda dell'istanza di @link(Rapporto) che viene passato, altrimenti
	 * richiama il @link(DomainStringParser) sull'istanza corrente. 
	 * 
	 * @param rapportoEnhanced
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enhanceRapporto(Rapporto rapportoEnhanced, String string) {			
		Class<?> rapportoInterface = rapportoEnhanced.getClass();
		
		try {
			Method method = this.getClass().getDeclaredMethod("enhanceRapporto", new Class[] { rapportoInterface, String.class });
			method.invoke(this, new Object[] { rapportoEnhanced, string });

		} catch (Exception e) {
			DomainStringParser domainStringParser = new DomainStringParser(rapportoInterface);
			domainStringParser.parse(rapportoEnhanced, string, true);
		}
	}
	
	/**
	 * Specializza il metodo per poter eseguire il DomainStringParser sul super tipo @link(CartaImpl) e sul super tipo @link(CartaMultifunzione)
	 * che definiscono le properties di cui fare il parse
	 * 
	 * @param rapportoEnhanced
	 * @param string
	 */
	/*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void enhanceRapporto(CartaSuperFlash rapportoEnhanced, String string) {
		DomainStringParser<CartaImpl> cartaParser = new DomainStringParser(CartaImpl.class);
		cartaParser.parse(rapportoEnhanced, string, true);
		
		cartaParser = new DomainStringParser(CartaMultifunzione.class);
		cartaParser.parse(rapportoEnhanced, string, true);
	}
	*/
	
	/**
	 * Specializza il metodo per poter eseguire il DomainStringParser sul super tipo @link(CartaImpl) e sul super tipo @link(CartaMultifunzione)
	 * che definiscono le properties di cui fare il parse
	 * 
	 * @param rapportoEnhanced
	 * @param string
	 */
	/*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void enhanceRapporto(CartaPayPass rapportoEnhanced, String string) {
		DomainStringParser<CartaImpl> cartaParser = new DomainStringParser(CartaImpl.class);
		cartaParser.parse(rapportoEnhanced, string, true);
		
		cartaParser = new DomainStringParser(CartaMultifunzione.class);
		cartaParser.parse(rapportoEnhanced, string, true);
	}
	*/
}
