package it.sample.parser.domain.annotation;

public interface RegexConstants {
	static final String ANAGRAFICA_REGEX = ">00<NK|00...0000NK";	
	static final String RECORD_0050_REGEX = ">50<|006..0050";
	static final String RECORD_0055_REGEX = ">55<|002210055";
	static final String RECORD_0100_REGEX = ">10<|000420100";
	static final String RECORD_0210_REGEX = ">21<|0025.0210";
	static final String RECORD_0220_REGEX = ">22<|000830220";
	static final String RECORD_0230_REGEX = ">23<|000840230";
	static final String RECORD_0310_REGEX = ">31<|000610310";
}
