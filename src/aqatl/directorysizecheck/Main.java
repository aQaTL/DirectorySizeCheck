package aqatl.directorysizecheck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {

	public static void main(String[] args) {
		String cwd = System.getProperty("user.dir");
		System.out.println(cwd);

		try {
			listDirectories(cwd);
		}
		catch (IOException e) {
			System.err.printf("Error: %s\n", e.getLocalizedMessage());
			System.exit(1);
		}

	}

	public static void listDirectories(String dirPath) throws IOException {
		Files.list(Paths.get(dirPath)).
				parallel().
				filter(path -> Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).
				map(File::NewFile).
				filter(Optional::isPresent).
				forEach(opt -> {
					File file = opt.get();

					File.Size size = file.getSizeHumanReadable();
					System.out.printf("%-9s%s\n", String.format("%.1f", size.size) + size.multiple.shortName,
							file.getPath().getFileName().toString());
				});
	}
}
