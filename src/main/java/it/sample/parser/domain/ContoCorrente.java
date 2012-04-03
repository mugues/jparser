package it.sample.parser.domain;

public class ContoCorrente extends Rapporto{
	private static final long serialVersionUID = 1760359457542327061L;

	public ContoCorrente() {
		super();
	}

	public ContoCorrente(Filiale filiale, String categoria, String numeroRapporto) {
		super(filiale, categoria, numeroRapporto);
	}

}
