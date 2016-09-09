/**
 *
 */
package il.org.spartan.xy;

import static il.org.spartan.utils.Box.*;
import static java.lang.String.*;

import java.io.*;
import java.util.*;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since February 13, 2012 */
public class XYGnuPlotter {
  private static BufferedWriter getWriter(final Process p) {
    return new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
  }

  private static String gformat(final double d) {
    if (d == (long) d)
      return "" + (long) d;
    return String.format("%g", new Double(d));
  }

  private static Process gnuplot() {
    try {
      return Runtime.getRuntime().exec(new String[] { "/usr/bin/gnuplot", "-persist" });
    } catch (final IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private final Process process;
  private final BufferedWriter writer;
  private final Thread stdoutRedirector;
  private final Thread stderrRedirector;
  private final Settings settings;

  /** Instantiate {@link XYGnuPlotter}. */
  public XYGnuPlotter() {
    this(new Settings());
  }

  /** Instantiate {@link XYGnuPlotter}.
   * @param settings */
  public XYGnuPlotter(final Settings settings) {
    this.settings = settings;
    process = gnuplot();
    writer = getWriter(process);
    stdoutRedirector = new OutputStreamRedirector(System.out, process.getInputStream());
    stderrRedirector = new OutputStreamRedirector(System.err, process.getErrorStream());
    write(settings.before());
  }

  public void close() throws Throwable {
    process.waitFor();
    stdoutRedirector.join();
    stderrRedirector.join();
    super.finalize();
  }

  public void done() {
    write(settings.after());
    write("exit\n");
  }

  public XYGnuPlotter feed(final XYSeries s) {
    for (int i = 0; i < s.n(); i++)
      write(gformat(s.x[i]) + " " + gformat(s.y[i]) + " " + gformat(s.dy[i]) + "\n");
    write("e\n");
    return this;
  }

  public void plot() {
    write(settings.plot());
  }

  public void write(final String s) {
    try {
      writer.write(s);
      writer.flush();
    } catch (final IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static class Curve {
    private String curve;

    public Curve(final String curve) {
      this.curve = curve;
    }

    public Curve(final String curve, final double... ds) {
      this(curve, (Object[]) box(ds));
    }

    public Curve(final String curve, final Object... os) {
      this.curve = format(curve, os);
    }

    public Curve annotate(final String annotation) {
      curve += " " + annotation;
      return this;
    }

    public Curve annotate(final String annotation, final Object... os) {
      return annotate(format(annotation, os));
    }

    @Override public String toString() {
      return curve;
    }
  }

  public static class Settings {
    private String before = "";
    private String after = "";
    private final List<Curve> curves = new ArrayList<>();

    public String after() {
      return after;
    }

    public String before() {
      return before;
    }

    public Settings characterize(final String statement) {
      before += statement + ";\n";
      return this;
    }

    public Settings characterize(final String statement, final double... ds) {
      return characterize(statement, (Object[]) box(ds));
    }

    public Settings characterize(final String statement, final Object... os) {
      return characterize(format(statement, os));
    }

    public Settings finalize(final String statement) {
      after += statement + ";\n";
      return this;
    }

    public Settings finalize(final String statement, final double... ds) {
      return finalize(statement, (Object[]) box(ds));
    }

    public Settings finalize(final String statement, final Object... os) {
      return finalize(format(statement, os));
    }

    public Curve newCurve(final String curve) {
      final Curve $ = new Curve(curve);
      curves.add($);
      return $;
    }

    public Curve newCurve(final String curve, final Object... os) {
      return newCurve(format(curve, os));
    }

    public Curve newCurveD(final String curve, final double... ds) {
      return newCurve(curve, (Object[]) box(ds));
    }

    public String plot() {
      return "plot " + Separate.by(curves, ", ") + ";\n";
    }
  }
}
