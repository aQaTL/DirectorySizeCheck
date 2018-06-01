package aqatl.directorysizecheck;

public enum MultipleType {
	BINARY(1024), DECIMAL(1000);

	public final int multiplier;

	MultipleType(int multiplier) {
		this.multiplier = multiplier;
	}
}
