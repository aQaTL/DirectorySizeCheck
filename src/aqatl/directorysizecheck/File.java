package aqatl.directorysizecheck;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class File {
	private Path path;
	private Size size;

	public File(Path path, long size) {
		this.path = path;
		this.size = new Size(size, Multiple.Byte);
	}

	public static Optional<File> NewFile(Path path) {
		try {
			long size = calculateSize(path);
			return Optional.of(new File(path, size));
		}
		catch (IOException e) {
			return Optional.empty();
		}
	}

	private static long calculateSize(Path path) throws IOException {
		final AtomicLong size = new AtomicLong(0);

		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				size.addAndGet(attrs.size());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
				return FileVisitResult.CONTINUE;
			}
		});
		return size.get();
	}

	public double getSizeIn(Multiple multiple) {
		return size.size / multiple.multiplier;
	}

	public Size getSizeHumanReadable() {
		Multiple[] mutliples = Multiple.values();
		Multiple[] decimalMultiples = Arrays.copyOfRange(mutliples, 0, mutliples.length / 2 + 1);

		for (Multiple multiple : decimalMultiples) {
			double humanReadableSize = this.size.size / multiple.multiplier;
			if (humanReadableSize < 1000) {
				return new Size(humanReadableSize, multiple);
			}
		}
		return this.size;
	}

	public Path getPath() {
		return path;
	}

	public Size getSize() {
		return size;
	}


	public class Size {
		public final double size;
		public final Multiple multiple;

		public Size(double size, Multiple multiple) {
			this.size = size;
			this.multiple = multiple;
		}
	}
}
