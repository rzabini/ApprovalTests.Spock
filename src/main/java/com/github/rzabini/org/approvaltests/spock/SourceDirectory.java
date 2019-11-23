package com.github.rzabini.org.approvaltests.spock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds source directory of test files.
 */
final class SourceDirectory {

    private static final String SEPARATOR = Matcher.quoteReplacement(File.separator);
    private static final String DOT = ".";
    private static final String LITERAL_DOT = Matcher.quoteReplacement(DOT);

    private SourceDirectory() {

    }

    public static Path of(final String fullClassName) {
        try (Stream<Path> walk = Files.walk(Paths.get(""))) {
            final List<Path> result = walk
                    .filter(path -> path.toString().replaceAll(SEPARATOR, DOT)
                            .matches(".*" + fullClassName + LITERAL_DOT + "(groovy|java)$"))
                    .map(Path::getParent)
                    .collect(Collectors.toList());

            assert result.size() == 1;
            return result.get(0);

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
