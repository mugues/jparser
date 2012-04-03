package it.sample.parser.domain;

public class DepositoAmministrato extends Rapporto{	
	private static final long serialVersionUID = 1760359457542327061L;

	public DepositoAmministrato() {
		super();
	}

	public DepositoAmministrato(Filiale filiale, String categoria, String numeroRapporto) {
		super(filiale, categoria, numeroRapporto);
	}

}
