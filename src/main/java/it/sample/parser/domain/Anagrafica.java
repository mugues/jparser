package it.sample.parser.domain;

import it.sample.parser.domain.annotation.DataEntity;
import it.sample.parser.domain.annotation.DataField;
import it.sample.parser.domain.annotation.RegexConstants;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

@DataEntity(regex = RegexConstants.ANAGRAFICA_REGEX)
public class Anagrafica implements Serializable{
	private static final long serialVersionUID = -8832923173150893985L;

	@DataField(startRangeOnline = 0, lengthOnline = 17)
	private String multichannelId;

	@DataField(startRangeOnline = 17, lengthOnline = 30)
	private String cognome;
	
	@DataField(startRangeOnline = 47, lengthOnline = 30)
	private String nome;
	
	@DataField(startRangeOnline = 77, lengthOnline = 16)
	private String codiceFiscale;
	
	public String getMultichannelId() {
		return multichannelId;
	}
	public void setMultichannelId(String multichannelId) {
		this.multichannelId = multichannelId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	@Override
	public String toString() {
		return new ToStringCreator(this).append("multichannelId=" + multichannelId).append("codiceFiscale=" + codiceFiscale).append("nome=" + nome).append("cognome=" + cognome).toString();
	}	
}
