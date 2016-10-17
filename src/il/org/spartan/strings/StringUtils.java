package il.org.spartan.strings;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** A bunch of string functions.
 * @author Yossi Gil */
@Antiexample @Utility public enum StringUtils {
  // No elements in this name space
  ;
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;

  public static double atod(final String ¢) {
    return new Double(¢).doubleValue();
  }

  public static float atof(final String ¢) {
    return new Float(¢).floatValue();
  }

  public static int atoi(final String ¢) {
    return new Integer(¢).intValue();
  }

  public static long atol(final String ¢) {
    return new Long(¢).longValue();
  }

  public static String capitalize(final String ¢) {
    return ¢.length() == 0 ? ¢ : (first(¢) + "").toUpperCase() + rest(¢).toLowerCase();
  }

  /** Concatenate any number of strings.
   * @param ss a variable number of strings
   * @return the concatenation of the strings in <code>ss</code> */
  public static String cat(final String... ss) {
    final StringBuilder $ = new StringBuilder("");
    for (final String ¢ : ss)
      $.append(¢);
    return $ + "";
  }

  public static String cat(final String[]... sss) {
    final StringBuilder $ = new StringBuilder("");
    for (final String[] ¢ : sss)
      $.append(cat(¢));
    return $ + "";
  }

  public static double delta(final double a, final double d) {
    return a == d ? 0 : signum(a) != signum(d) ? Double.NaN : 2 * Math.abs(a - d) / Math.abs(a + d);
  }

  public static String dtoa(final double ¢) {
    return ¢ + "";
  }

  public static <T> boolean eq(final T a, final T b) {
    return a == null ? b == null : a.equals(b);
  }

  public static String esc(final char ¢) {
    switch (¢) {
      case '\n':
        return "\\n";
      case '\r':
        return "\\r";
      case '\t':
        return "\\t";
      case '\f':
        return "\\f";
      case '\b':
        return "\\b";
      case '\\':
        return "\\\\";
      default:
        return ¢ + "";
    }
  }

  public static String esc(final String s) {
    if (s == null)
      return "(null)";
    final StringBuilder $ = new StringBuilder(s.length());
    for (int ¢ = 0; ¢ < s.length(); ++¢)
      $.append(esc(s.charAt(¢)));
    return $ + "";
  }

  public static String expandLeadingTabs(final String s) {
    nonnull(s);
    String $ = s;
    for (;;) {
      final String newValue = $.replaceAll("(?m)^([\t]*)\t", "$1    ");
      if ($.equals(newValue))
        return $;
      $ = newValue;
    }
  }

  public static String fill(final int i, final char c) {
    return fill(i, c + "");
  }

  public static String fill(final int i, final String s) {
    final StringBuilder $ = new StringBuilder();
    for (int ¢ = 0; ¢ < i; ++¢)
      $.append(s);
    return $ + "";
  }

  public static char first(final String ¢) {
    nonnull(¢);
    positive(¢.length());
    return ¢.charAt(0);
  }

  public static String ftoa(final float ¢) {
    return ¢ + "";
  }

  public static boolean isDouble(final String s) {
    try {
      new Double(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isFloat(final String s) {
    try {
      new Float(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isInt(final String s) {
    try {
      new Integer(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isLong(final String s) {
    try {
      new Long(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static String itoa(final int ¢) {
    return ¢ + "";
  }

  public static String javaCase(final String ¢) {
    return ¢.length() == 0 ? ¢ : (first(¢) + "").toLowerCase() + rest(¢);
  }

  public static char last(final String ¢) {
    nonnull(¢);
    positive(¢.length());
    return ¢.charAt(¢.length() - 1);
  }

  public static String lowCounter(final int ¢) {
    switch (¢) {
      case -1:
        return "";
      case 0:
        return "a";
      default:
        return tolow(¢);
    }
  }

  public static String ltoa(final long ¢) {
    return ¢ + "";
  }

  /** Compute the string equivalent ordinal of a positive integer, e.g., for 1
   * return "1st", for 22, the "22nd", etc.
   * @param i a non-negative integer to convert
   * @return the ordinal string representation of <code>n</code> */
  public static String ordinal(final int i) {
    nonnegative(i);
    final String th = "th";
    switch (i % 10) {
      case 1:
        return i + (i == 11 ? th : "st");
      case 2:
        return i + (i == 12 ? th : "nd");
      default:
        return i + th;
    }
  }

  /** Wrap an object in parenthesis
   * @param ¢ a non-<code><b>null</b></code> object for wrapping in parenthesis
   * @return the result of <code>o.toString()</code> wrapped parenthesis */
  public static String paren(final Object ¢) {
    return "(" + ¢ + ")";
  }

  public static String pluralize(final int i, final String singular) {
    return pluralize(i, singular, singular + "s");
  }

  public static String pluralize(final int i, final String singular, final String plural) {
    switch (i) {
      case 0:
        return "no " + plural;
      case 1:
        return singular + "";
      case 2:
        return "two " + plural;
      case 3:
        return "three " + plural;
      case 4:
        return "four " + plural;
      case 5:
        return "five " + plural;
      case 6:
        return "six " + plural;
      case 7:
        return "seven " + plural;
      case 8:
        return "eight " + plural;
      case 9:
        return "nine " + plural;
      default:
        return i + " " + plural;
    }
  }

  public static String pretty(final String singular, final Collection<? extends Object> a) {
    return pretty(singular, singular + "s", a);
  }

  public static String pretty(final String singular, final String plural, final Collection<? extends Object> a) {
    if (a == null || a.size() <= 0)
      return "";
    if (a.size() == 1)
      return "1 " + singular + ": " + a.iterator().next() + "\n";
    String $ = a.size() + " " + plural + ":\n";
    int n = 0;
    final Once ellipsis = new Once("\t...\n");
    for (final Object ¢ : a) {
      ++n;
      $ += n > MAX_FIRST && n <= a.size() - MAX_LAST ? ellipsis : "\t" + n + ") " + ¢ + "\n";
    }
    return $;
  }

  /** Quote an object
   * @param ¢ a non-<code><b>null</b></code> object for quoting
   * @return the result of <code>o.toString()</code> wrapped with single
   *         quotes */
  public static String quote(final Object ¢) {
    return wrap('\'', (¢ + ""));
  }

  public static String repeat(final int i, final char c) {
    return repeat(i, c + "");
  }

  /** Repeat a string a fixed number of times
   * @param i a non-negative integer
   * @param s a string to repeat
   * @return a {@link String} containing <code>s</code> concatenated
   *         <code>n</code> times */
  public static String repeat(final int i, final String s) {
    final StringBuffer $ = new StringBuffer();
    for (int ¢ = 0; ¢ < i; ++¢)
      $.append(s);
    return $ + "";
  }

  /** Chop the first character of a string.
   * @param ¢ a non-<code><b>null</b></code> string of length at least one
   * @return <code>s</code> but without its first character. */
  public static String rest(final String ¢) {
    nonnull(¢);
    positive(¢.length());
    return ¢.substring(1);
  }

  public static int signum(final double ¢) {
    return ¢ == 0 ? 0 : ¢ > 0 ? 1 : -1;
  }

  public static String sprintf(final String format, final Object... args) {
    return new Formatter().format(format, args) + "";
  }

  public static String sprintf(final String[] args) {
    switch (args.length) {
      case 0:
        return "";
      case 1:
        return args[0];
      default:
        final Object os[] = new Object[args.length - 1];
        for (int ¢ = 1; ¢ < args.length; ++¢)
          os[¢ - 1] = args[¢];
        return new Formatter().format(args[0], os) + "";
    }
  }

  /** Strip the first and last character of a string.
   * @param ¢ a non-<code><b>null</b></code> string of length at least two to
   *        strip
   * @return <code>s</code> but without its first and last character. */
  public static String strip(final String ¢) {
    nonnull(¢);
    require(¢.length() >= 2);
    return ¢.substring(1, ¢.length() - 1);
  }

  public static List<String> toLines(final String s) throws IOException {
    final List<String> $ = new ArrayList<>();
    for (final BufferedReader br = new BufferedReader(new StringReader(s));;) {
      final String line = br.readLine();
      if (line == null)
        return $;
      $.add(line);
    }
  }

  public static String upCounter(final int ¢) {
    switch (¢) {
      case -1:
        return "";
      case 0:
        return "A";
      default:
        return toup(¢);
    }
  }

  public static String visualize(final String ¢) {
    return esc(¢).replaceAll(" ", "\\s");
  }

  public static String wrap(final char with, final String s) {
    return with + s + with;
  }

  public static String wrap(final String with, final String s) {
    return with + s + with;
  }

  private static String tolow(final int ¢) {
    return ¢ == 0 ? "" : tolow(¢ / 26) + (char) ('a' + ¢ % 26);
  }

  private static String toup(final int ¢) {
    return ¢ == 0 ? "" : toup(¢ / 26) + (char) ('A' + ¢ % 26);
  }
}
