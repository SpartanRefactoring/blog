package il.org.spartan.utils;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/** A class for utility methods when working with files and directories
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19 */
public class FileUtils {
  /** Returns a list of all the .java files found recursively within the
   * provided paths
   * @param paths Directories to scan
   * @return a list of absolute paths to .java files found within the provided
   *         directories. If no files were found, an empty list is returned */
  public static List<String> findAllJavaFiles(final String... paths) {
    final List<String> $ = new ArrayList<>();
    if (paths.length == 0)
      return $;
    for (final String s : paths) {
      if (s == null)
        continue;
      final File f = new File(s);
      if (f.exists() && f.isDirectory())
        iterateFiles(new File(s), $);
    }
    return $;
  }

  /** Converts the entire contents of a file into a {@link String}
   * @param f JD
   * @return a string representing the contents of a file.
   * @throws IOException in case of error */
  public static String read(final File f) throws IOException {
    final String ls = System.getProperty("line.separator");
    final StringBuilder $ = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String line;
      while ((line = reader.readLine()) != null)
        $.append(line).append(ls);
    }
    return $ + "";
  }

  /** Returns the contents of a source file
   * @param fileName The source file's path
   * @return source file's contents, or an empty string in case of an error
   * @throws IOException in case of error */
  public static String readFromFile(final String fileName) throws IOException {
    return read(Paths.get(fileName));
  }

  /** @param fileName where to write
   * @param text what to write
   * @throws FileNotFoundException in case the file could not be found */
  public static void writeToFile(final String fileName, final String text) throws FileNotFoundException {
    try (final PrintWriter p = new PrintWriter(fileName)) {
      p.write(text);
      p.flush();
    }
  }

  private static void iterateFiles(final File dir, final List<String> files) {
    if (dir == null)
      return;
    for (final File f : dir.listFiles()) {
      if (f.isDirectory())
        iterateFiles(f, files);
      if (f.isFile() && f.getName().endsWith(".java"))
        files.add(f.getAbsolutePath());
    }
  }

  private static String read(final Path p) throws IOException {
    return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
  }
}
