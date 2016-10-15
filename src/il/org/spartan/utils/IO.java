package il.org.spartan.utils;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;

import il.org.spartan.streotypes.*;

/** Static methods for I/O related operations */
@Antiexample public class IO {
  public static String concatLines(final Iterable<String> ss) {
    final StringBuffer sb = new StringBuffer(1000);
    final Separator nl = new Separator("\n");
    for (final String s : ss)
      sb.append(nl).append(s);
    return sb + "";
  }

  public static String concatLines(final String... ss) {
    final StringBuffer sb = new StringBuffer(1000);
    final Separator nl = new Separator("\n");
    for (final String s : ss)
      sb.append(nl).append(s);
    return sb + "";
  }

  public static List<String> lines(final String str) throws IOException {
    final List<String> $ = new ArrayList<>();
    final BufferedReader br = new BufferedReader(new StringReader(str));
    while (true) {
      final String line = br.readLine();
      if (line == null)
        return $;
      $.add(line);
    }
  }

  public static InputStream toInputStream(final String s) {
    try {
      return new ByteArrayInputStream(s.getBytes("UTF-8"));
    } catch (final UnsupportedEncodingException e) {
      unreachable();
      return null;
    }
  }

  /** Read the contents of the given class-path file.
   * @param clazz Class - Specifies a location in the class-path tree
   * @param path Relative path to the file from the given class
   * @return Contents of the file
   * @throws IOException If an I/O error occur */
  public static String toString(final Class<?> clazz, final String path) throws IOException {
    return toString(clazz.getResourceAsStream(path));
  }

  /** Read the contents of the given stream and return it as a String
   * @param is Input stream
   * @return the entire content of <code>is</code>
   * @throws IOException If an I/O error occur */
  public static String toString(final InputStream is) throws IOException {
    return toString(new InputStreamReader(is));
  }

  /** Read the contents of the given reader and return it as a String
   * @param r Reader
   * @return the entire content of <code>r</code>
   * @throws IOException If an I/O error occur */
  public static String toString(final Reader r) throws IOException {
    final StringBuilder $ = new StringBuilder();
    for (int c = r.read(); c >= 0; c = r.read())
      $.append((char) c);
    return $ + "";
  }

  /** Write a string to a file
   * @param outputFile File to be written
   * @param ss Strings to write
   * @throws IOException If an I/O error occur */
  public static void writeLines(final File outputFile, final String... ss) throws IOException {
    final FileWriter fw = new FileWriter(outputFile);
    try {
      for (final String s : ss) {
        fw.append(s);
        fw.append("\n");
      }
    } finally {
      fw.close();
    }
  }
}
