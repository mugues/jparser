package it.test.sample.parser.core.common;

import static org.junit.Assert.assertEquals;
import it.sample.parser.core.common.DomainStringParser;
import it.sample.parser.domain.Anagrafica;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/application-context/parsers.xml" })
public class TestDomainStringParser {

	@Autowired
	@Qualifier("anagraficaParser")
	private DomainStringParser<Anagrafica> anagraficaParser;
	
	@Test
	public void testParseAnagrafica() {
		String flusso = "003060000NK01025649971576493UGUES                         MASSIMO                        GSUMSM77D12L219E";
		
		Anagrafica anagrafica = anagraficaParser.parse(flusso, true);
		assertEquals("01025649971576493", anagrafica.getMultichannelId());
		assertEquals("GSUMSM77D12L219", anagrafica.getCodiceFiscale());
		assertEquals("MASSIMO", anagrafica.getNome());
		assertEquals("UGUES", anagrafica.getCognome());
	}
}
