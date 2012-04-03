package it.sample.parser.domain;

import static it.sample.parser.domain.annotation.RegexConstants.RECORD_0210_REGEX;
import it.sample.parser.domain.annotation.DataEntity;
import it.sample.parser.domain.annotation.DataField;
import it.sample.parser.domain.annotation.IgnoreField;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.style.ToStringCreator;

@DataEntity(regex = RECORD_0210_REGEX)
public class Rapporto implements Serializable {

	private static final long serialVersionUID = -5567166522882040440L;

	protected Long codiceRapporto;

	@DataField(startRangeOnline = 1, lengthOnline = 5, startRangeBatch = 0, lenghtBatch = 5)
	@IgnoreField
	private Filiale filiale;

	@DataField(startRangeOnline = 6, lengthOnline = 4, startRangeBatch = 5, lenghtBatch = 4)
	private String categoria;

	@DataField(startRangeOnline = 10, lengthOnline = 8, startRangeBatch = 9, lenghtBatch = 8)
	private String numeroRapporto;

	@DataField(startRangeOnline = 30, lengthOnline = 5, startRangeBatch = 38, lenghtBatch = 5)
	private String abi;

	@DataField(startRangeOnline = 18, lengthOnline = 12, startRangeBatch = 17, lenghtBatch = 12)
	private String formaTecnica;

	@DataField(startRangeOnline = 94, lengthOnline = 3, startRangeBatch = 103, lenghtBatch = 3)
	private String statoRapporto;

	@DataField(startRangeOnline = 45, lengthOnline = 1, startRangeBatch = 59, lenghtBatch = 1)
	private TipologiaRapporto tipoRapporto;

	private Set<Intestatario> intestatari = new HashSet<Intestatario>();

	public Rapporto() {
		super();
	}
	
	public Rapporto(Filiale filiale, String categoria, String numeroRapporto) {
		this.filiale = filiale;
		this.categoria = categoria;
		this.numeroRapporto = numeroRapporto;
	}
	
	public Long getCodiceRapporto() {
		return codiceRapporto;
	}

	public void setCodiceRapporto(Long codiceRapporto) {
		this.codiceRapporto = codiceRapporto;
	}

	public Filiale getFiliale() {
		return filiale;
	}

	public void setFiliale(Filiale filiale) {
		this.filiale = filiale;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNumeroRapporto() {
		return numeroRapporto;
	}

	public void setNumeroRapporto(String numeroRapporto) {
		this.numeroRapporto = numeroRapporto;
	}

	public String getAbi() {
		return abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	public String getFormaTecnica() {
		return formaTecnica;
	}

	public void setFormaTecnica(String formaTecnica) {
		this.formaTecnica = formaTecnica;
	}

	public String getStatoRapporto() {
		return statoRapporto;
	}

	public void setStatoRapporto(String statoRapporto) {
		this.statoRapporto = statoRapporto;
	}

	public TipologiaRapporto getTipoRapporto() {
		return tipoRapporto;
	}

	public void setTipoRapporto(TipologiaRapporto tipoRapporto) {
		this.tipoRapporto = tipoRapporto;
	}

	public Set<Intestatario> getIntestatari() {
		return intestatari;
	}
	
	public void addIntestatario(Intestatario intestatario) {
		this.intestatari.add(intestatario);
	}

	@Override
	public String toString() {
		String intestatariString = (intestatari != null) ? Arrays.toString(intestatari.toArray()) : "";

		return new ToStringCreator(this).append("codiceRapporto" + codiceRapporto).append("abi=" + abi).append("filiale=" + filiale).append("categoria=" + categoria)
				.append("numeroRapporto=" + numeroRapporto).append("formaTecnica=" + formaTecnica).append("tipoRapporto=" + tipoRapporto).append("statoRapporto=" + statoRapporto)
				.append("intestatari=" + intestatariString).toString();
	}

}
