package it.sample.parser.domain;

import it.sample.parser.domain.annotation.DataEntity;
import it.sample.parser.domain.annotation.DataField;
import it.sample.parser.domain.annotation.RegexConstants;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

@DataEntity(regex = RegexConstants.RECORD_0220_REGEX)
public class Intestatario implements Serializable {

	private static final long serialVersionUID = -2484632693130253866L;

	@DataField(startRangeOnline = 67, lengthOnline = 3, startRangeBatch = 0, lenghtBatch = 3)
	private Integer codiceIntestatario;

	@DataField(startRangeOnline = 31, lengthOnline = 16, startRangeBatch = 33, lenghtBatch = 16)
	private String codiceFiscale;

	@DataField(startRangeOnline = 1, lengthOnline = 30, startRangeBatch = 3, lenghtBatch = 30)
	private String nominativo;

	@DataField(startRangeOnline = 47, lengthOnline = 20, startRangeBatch = 49, lenghtBatch = 20)
	private String descrizioneQualifica;

	public Integer getCodiceIntestatario() {
		return codiceIntestatario;
	}

	public void setCodiceIntestatario(Integer codiceIntestatario) {
		this.codiceIntestatario = codiceIntestatario;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNominativo() {
		return nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}

	public String getDescrizioneQualifica() {
		return descrizioneQualifica;
	}

	public void setDescrizioneQualifica(String descrizioneQualifica) {
		this.descrizioneQualifica = descrizioneQualifica;
	}

	@Override
	public String toString() {

		return new ToStringCreator(this).append("codiceIntestatario = " + codiceIntestatario).append("codiceFiscale = " + codiceFiscale).append("nominativo = " + nominativo)
				.append("descrizioneQualifica = " + descrizioneQualifica).toString();
	}

}
