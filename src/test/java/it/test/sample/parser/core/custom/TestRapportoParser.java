package it.test.sample.parser.core.custom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.sample.parser.core.custom.RapportoParser;
import it.sample.parser.domain.ContoCorrente;
import it.sample.parser.domain.DepositoAmministrato;
import it.sample.parser.domain.Rapporto;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/application-context/parsers.xml" })
public class TestRapportoParser {
	String abi = "01025";
	String filiale = "00521";
	String categoria = "1000";
	String attributo = "CC1000S0";
	String conto = "00060747";	
	String ibanInterno = "IT16F0102501021100000060747";
	String ibanVisualizzato = "IT68T0306901021100000060747";
	String codiceNsg = "4200330372000";
	String statoRapporto = "022";
	String codiceServizio = "A";
	String codiceGestore = "00521A4";
	String matricolaCodiceGestore = "206752";
		
	String categoriaDepositoAmministrato = "3100";
	String contoDepositoAmministrato = "01520138";
	String attributoDepositoAmministrato = "DA3100S0";
	String codiceAdesioneDerivati = "NO";
	
	String multichannelId = "01025649900581389";

	@Autowired
	@Qualifier("rapportoParser")
	private RapportoParser rapportoParser;
	
	@Test
	public void testParseAllEager() {
		String evento005canaleIB = ">MID0100051200000010NKS1OUNK000201          CICSUSER                                                SDM0                                                                            00001819AI      S                                                           5644112011-04-2911.30.30020010741SDM1SDQ0                                                                                                                                                                                                                       >OUTESI<00000096Y1000 RICHIESTA ESEGUITA                                                        >OUTBST<00001723003060000NK01025649971576493NK6499      01025640171576493NK6401                                                                                                                          0052011-04-29-11.30.29.826003PF                                                                                          00042010001025640171576493NK6401      IB 0002560210D00521100000060747CC1000S0    0102501021FIT1611A                                 4200330372000022         NO00521A4206752 1000000607470306901021TIT68100000060747                                                                                      000830220IUGUES GIUSEPPE                GSUGPP45R27L219WINTESTATARIO        001    000830220IROSSI MIRELLA                 RSSMLL47C52D022DINTESTATARIO        002    000830220IUGUES STEFANO                 GSUSFN73D25L219RINTESTATARIO        003    000830220IUGUES MASSIMO                 GSUMSM77D12L219EINTESTATARIO        004    002560210U00521310001520138DA3100S0    0000000000     20     00521100000060747CC1000S0    4200330372000022         NO00521A4206752             0306901021TIT68100000060747                                                                                      000830220IUGUES GIUSEPPE                GSUGPP45R27L219WINTESTATARIO        001+001000830220IROSSI MIRELLA                 RSSMLL47C52D022DINTESTATARIO        002+002000830220IUGUES STEFANO                 GSUSFN73D25L219RINTESTATARIO        003+003000830220IUGUES MASSIMO                 GSUMSM77D12L219EINTESTATARIO        004+00400061031001025640171576493NK6401      IBAUPD20101214        A00061031001025640171576493NK6401      IBQBIT20080303        A00061031001025640171576493NK6401      IBQBPA20110201        A";		
		Collection<Rapporto> rapporti = rapportoParser.parseAll(evento005canaleIB, true);
		
		assertNotNull(rapporti);		
		assertTrue(rapporti.size() == 2);
		
		String codiceAbiExpetecd = "01025";
		for (Rapporto rapporto : rapporti) {
			assertEquals(codiceAbiExpetecd , rapporto.getAbi());
		}
		
		List<Rapporto> parseAllData = (List<Rapporto>) rapportoParser.parseAllEager(evento005canaleIB, true);	
		Rapporto rapporto = parseAllData.get(0);		
		
		assertTrue(rapporto instanceof ContoCorrente);
		
		System.out.println(rapporto);
		ContoCorrente contoCorrente = (ContoCorrente) rapporto;
		
		assertEquals(filiale, contoCorrente.getFiliale().getCodiceFiliale());
		assertEquals(categoria, contoCorrente.getCategoria());
		assertEquals(conto, contoCorrente.getNumeroRapporto());
		assertEquals(attributo.trim(), contoCorrente.getFormaTecnica());
		assertEquals(statoRapporto, contoCorrente.getStatoRapporto());

		assertEquals(4, contoCorrente.getIntestatari().size());
		
		rapporto = parseAllData.get(1);		
		
		assertTrue(rapporto instanceof DepositoAmministrato);
		
		System.out.println(rapporto);
		DepositoAmministrato depositoAmministrato = (DepositoAmministrato) rapporto;
		
		assertEquals(filiale, depositoAmministrato.getFiliale().getCodiceFiliale());
		assertEquals(categoriaDepositoAmministrato, depositoAmministrato.getCategoria());
		assertEquals(contoDepositoAmministrato, depositoAmministrato.getNumeroRapporto());
		assertEquals(attributoDepositoAmministrato.trim(), depositoAmministrato.getFormaTecnica());
		assertEquals(statoRapporto, depositoAmministrato.getStatoRapporto());
		
		assertEquals(4, depositoAmministrato.getIntestatari().size());
		
	}

}
