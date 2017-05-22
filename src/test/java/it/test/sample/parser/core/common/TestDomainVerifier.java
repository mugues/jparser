package it.test.sample.parser.core.common;

import it.sample.parser.core.common.DomainVerifier;
import it.sample.parser.domain.Anagrafica;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestDomainVerifier
{

  private DomainVerifier<Anagrafica> domainVerifier;

  @Test
  public void verify() {

    Anagrafica anagrafica = new Anagrafica();
    anagrafica.setMultichannelId("multichannelId");

    Anagrafica anagrafica1 = new Anagrafica();
    anagrafica1.setMultichannelId("multichannelId1");

    domainVerifier = new DomainVerifier<>(Anagrafica.class);

    List<String> verify = (List<String>) domainVerifier.verify(anagrafica, anagrafica1);

    assertThat(verify.size(), is(1));
    assertThat(verify.get(0), is("multichannelId"));

  }

  @Test
  public void verifyMorePropertis() {

    Anagrafica anagrafica = new Anagrafica();
    anagrafica.setMultichannelId("multichannelId");
    anagrafica.setCognome("cognome");
    anagrafica.setNome("nome");

    Anagrafica anagrafica1 = new Anagrafica();
    anagrafica1.setMultichannelId("multichannelId");
    anagrafica1.setCognome("cognome");
    anagrafica1.setNome("nome1");

    domainVerifier = new DomainVerifier<>(Anagrafica.class);

    List<String> verify = (List<String>) domainVerifier.verify(anagrafica, anagrafica1);

    assertThat(verify.size(), is(1));
    assertThat(verify.get(0), is("nome"));



  }
}
