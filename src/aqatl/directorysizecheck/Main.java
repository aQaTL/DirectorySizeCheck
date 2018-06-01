package aqatl.directorysizecheck;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {

	public static void main(String[] args) {
		CommandLine cmd = parseArgs(args);

		MultipleType type;

		if (cmd.hasOption("d") && cmd.hasOption("b")) {
			System.err.println("Only one base can be used. Launch program with one flag");
			System.exit(1);
		}
		type = cmd.hasOption("d") ? MultipleType.DECIMAL : MultipleType.BINARY;

		String cwd = System.getProperty("user.dir");
		System.out.printf("%s\n\n", cwd);

		try {
			listDirectories(cwd, type);
		}
		catch (IOException e) {
			System.err.printf("Error: %s\n", e.getLocalizedMessage());
			System.exit(1);
		}
	}

	public static void listDirectories(String dirPath, MultipleType type) throws IOException {
		Files.list(Paths.get(dirPath)).
				parallel().
				filter(path -> Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).
				map(File::NewFile).
				filter(Optional::isPresent).
				forEach(opt -> {
					File file = opt.get();
					File.Size size = file.getSizeHumanReadable(type);

					System.out.printf("%-10s%s\n", String.format("%.1f", size.size) + size.multiple.shortName,
							file.getPath().getFileName().toString());
				});
	}

	private static CommandLine parseArgs(String[] args) {
		Options options = new Options();
		options.addOption("b", false, "use base 2 multiples");
		options.addOption("d", false, "use base 10 multiples");
		options.addOption("help", false, "display usage/help");
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ds", options);
				System.exit(0);
			}

			return cmd;
		}
		catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ds", options);
			System.exit(1);

			return null;
		}
	}
}
