package aqatl.directorysizecheck;


import java.util.Arrays;

public class FileSize {
	public final double size;
	public final Multiple multiple;

	public FileSize(double size, Multiple multiple) {
		this.size = size;
		this.multiple = multiple;
	}

	public FileSize makeHumanReadable(MultipleType type) {
		Multiple[] multiples = Multiple.values();

		int from, to;
		if (type == MultipleType.DECIMAL) {
			from = 1;
			to = multiples.length / 2 + 1;
		} else {
			from = multiples.length / 2 + 1;
			to = multiples.length;
		}
		Multiple[] usedMultiples = Arrays.copyOfRange(multiples, from, to);

		for (Multiple multiple : usedMultiples) {
			double humanReadableSize = this.size / multiple.multiplier;
			if (humanReadableSize < type.multiplier && humanReadableSize > 1.0) {
				return new FileSize(humanReadableSize, multiple);
			}
		}
		return this;
	}
}
