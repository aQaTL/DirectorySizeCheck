package aqatl.directorysizecheck;

public enum Multiple {
	Byte(1, "B"),
	Kilobyte(Byte.multiplier * 1000, "KB"),
	Megabyte(Kilobyte.multiplier * 1000, "MB"),
	Gigabyte(Megabyte.multiplier * 1000, "GB"),
	Terabyte(Gigabyte.multiplier * 1000, "TB"),
	Petabyte(Terabyte.multiplier * 1000, "PB"),

	Kibibyte(Byte.multiplier * 1024, "KiB"),
	Mebibyte(Kibibyte.multiplier * 1024, "MiB"),
	Gibibyte(Mebibyte.multiplier * 1024, "GiB"),
	Tebibyte(Gibibyte.multiplier * 1024, "TiB"),
	Pebibyte(Tebibyte.multiplier * 1024, "PiB");


	public final int multiplier;
	public final String shortName;

	Multiple(int multiplier, String shortName) {
		this.multiplier = multiplier;
		this.shortName = shortName;
	}
}
