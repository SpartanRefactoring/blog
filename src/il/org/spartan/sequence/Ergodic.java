/**
 *
 */
package il.org.spartan.sequence;

import java.util.*;

import il.org.spartan.external.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 24 ביול 2011 */
public class Ergodic {
  @External(name = "from") static int FROM = 1;
  @External(name = "to") static int TO = 100;
  @External(name = "N") static int N = 20;

  public static void main(final String[] args) {
    External.Introspector.extract(args, Ergodic.class);
    System.out.println(Separate.by(make(N, FROM, TO), " "));
    System.out.println(Separate.by(makeDouble(N, 0.01, 10), " "));
  }

  public static int[] make(final int max, final int... is) {
    return makeInt(max, Iterables.toList(is));
  }

  public static double[] makeDouble(final int max, final double... ds) {
    return makeDouble(max, Iterables.toList(ds));
  }

  static int countDiff(final List<Integer> is, final float diff) {
    int $ = 0;
    for (int ¢ = 1; ¢ < is.size(); ++¢)
      if (diff(is, ¢) == diff)
        ++$;
    return $;
  }

  static int countDiffDouble(final List<Double> ds, final double diff) {
    int $ = 0;
    for (int ¢ = 1; ¢ < ds.size(); ++¢)
      if (diffDouble(ds, ¢) == diff)
        ++$;
    return $;
  }

  static float maxDiff(final List<Integer> is) {
    float $ = -1;
    for (int ¢ = 1; ¢ < is.size(); ++¢)
      if (valid(is, ¢) && diff(is, ¢) > $)
        $ = diff(is, ¢);
    return $;
  }

  static double maxDiffDouble(final List<Double> ds) {
    double $ = -1;
    for (int ¢ = 1; ¢ < ds.size(); ++¢)
      if (diffDouble(ds, ¢) > $)
        $ = diffDouble(ds, ¢);
    return $;
  }

  private static float diff(final List<Integer> is, final int i) {
    return !valid(is, i) ? -1 : (float) is.get(i).intValue() / is.get(i - 1).intValue();
  }

  private static double diffDouble(final List<Double> ds, final int i) {
    return ds.get(i).doubleValue() / ds.get(i - 1).doubleValue();
  }

  private static double[] makeDouble(final int i, final List<Double> $) {
    for (;;) {
      if ($.size() >= i)
        break;
      final Double d = selectDouble($);
      if (d == null)
        break;
      $.add(d);
    }
    return Unbox.unbox($.toArray(new Double[$.size()]));
  }

  private static int[] makeInt(final int n, final List<Integer> $) {
    for (;;) {
      if ($.size() >= n)
        break;
      final Integer i = select($);
      if (i == null)
        break;
      $.add(i);
    }
    return Unbox.it($);
  }

  private static Integer mid(final List<Integer> is, final int i) {
    return mid(is.get(i - 1).intValue(), is.get(i).intValue());
  }

  private static Integer mid(final long a, final long b) {
    return Integer.valueOf((int) Math.round(Math.sqrt(a * b)));
  }

  private static Double midDouble(final double a, final double d) {
    return Double.valueOf(Math.round(Math.sqrt(a * d)));
  }

  private static Double midDouble(final List<Double> ds, final int i) {
    return midDouble(ds.get(i - 1).doubleValue(), ds.get(i).doubleValue());
  }

  private static Integer select(final List<Integer> l) {
    final List<Integer> is = new ArrayList<>(l);
    Collections.sort(is);
    final float maxDiff = maxDiff(is);
    return maxDiff < 0 ? null : selectDiff(is, maxDiff, new Random(0).nextInt(countDiff(is, maxDiff)));
  }

  private static Integer selectDiff(final List<Integer> is, final float maxDiff, final int nextInt) {
    for (int n = 0, ¢ = 1; ¢ < is.size(); ++¢)
      if (valid(is, ¢) && diff(is, ¢) == maxDiff) {
        if (n == nextInt)
          return mid(is, ¢);
        ++n;
      }
    return null;
  }

  private static Double selectDiffDouble(final List<Double> ds, final double maxDiff, final int nextInt) {
    for (int n = 0, ¢ = 1; ¢ < ds.size(); ++¢)
      if (diffDouble(ds, ¢) == maxDiff) {
        if (n == nextInt)
          return midDouble(ds, ¢);
        ++n;
      }
    return null;
  }

  private static Double selectDouble(final List<Double> ds) {
    final List<Double> is = new ArrayList<>(ds);
    Collections.sort(is);
    final double maxDiff = maxDiffDouble(is);
    return maxDiff < 0 ? null : selectDiffDouble(is, maxDiff, new Random(0).nextInt(countDiffDouble(is, maxDiff)));
  }

  private static boolean valid(final List<Integer> is, final int i) {
    return is.get(i - 1).intValue() != is.get(i).intValue() - 1;
  }
}
