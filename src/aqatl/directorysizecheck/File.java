package aqatl.directorysizecheck;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class File {
	private Path path;
	private FileSize size;

	public File(Path path, long size) {
		this.path = path;
		this.size = new FileSize(size, Multiple.Byte);
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

	public double getSize(Multiple multiple) {
		return size.size / multiple.multiplier;
	}


	public Path getPath() {
		return path;
	}

	public FileSize getSize() {
		return size;
	}
}
