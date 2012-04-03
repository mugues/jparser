package it.sample.parser.domain;

import it.sample.parser.domain.annotation.DataEntity;
import it.sample.parser.domain.annotation.DataField;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

@DataEntity()
public class Filiale implements Serializable{
	
	private static final long serialVersionUID = -2158554452543515811L;

	@DataField(startRangeOnline = 0, lengthOnline = 5, startRangeBatch = 0, lenghtBatch = 5)
	private String codiceFiliale;
	
	private String abi;
	private String cab;
	private String descrizioneFiliale;
	private String descrizioneBanca;
	public String getCodiceFiliale() {
		return codiceFiliale;
	}
	public void setCodiceFiliale(String codiceFiliale) {
		this.codiceFiliale = codiceFiliale;
	}
	public String getAbi() {
		return abi;
	}
	public void setAbi(String abi) {
		this.abi = abi;
	}
	public String getCab() {
		return cab;
	}
	public void setCab(String cab) {
		this.cab = cab;
	}
	public String getDescrizioneFiliale() {
		return descrizioneFiliale;
	}
	public void setDescrizioneFiliale(String descrizioneFiliale) {
		this.descrizioneFiliale = descrizioneFiliale;
	}
	public String getDescrizioneBanca() {
		return descrizioneBanca;
	}
	public void setDescrizioneBanca(String descrizioneBanca) {
		this.descrizioneBanca = descrizioneBanca;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this).append("codiceFiliale=", codiceFiliale).append("descrizioneFiliale=" + descrizioneFiliale).toString();
	}


}
